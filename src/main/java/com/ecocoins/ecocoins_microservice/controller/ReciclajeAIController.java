package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.dto.ValidacionIARequest;
import com.ecocoins.ecocoins_microservice.dto.ValidationResponse;
import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Contenedor;
import com.ecocoins.ecocoins_microservice.model.Reciclaje;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.ContenedorRepository;
import com.ecocoins.ecocoins_microservice.repository.ReciclajeRepository;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import com.ecocoins.ecocoins_microservice.service.GeminiAIService;
import com.ecocoins.ecocoins_microservice.service.NotificacionService;
import com.ecocoins.ecocoins_microservice.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reciclajes")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reciclaje con IA", description = "Validación de materiales con Inteligencia Artificial")
public class ReciclajeAIController {

    private final GeminiAIService claudeAIService;
    private final UsuarioRepository usuarioRepository;
    private final ContenedorRepository contenedorRepository;
    private final ReciclajeRepository reciclajeRepository;
    private final NotificacionService notificacionService;

    // Tarifas base de EcoCoins por kg
    private static final Map<String, Integer> TARIFAS = Map.of(
            "Plastico", 5,
            "Papel", 3,
            "Vidrio", 7,
            "Metal", 10,
            "Carton", 4
    );

    public ReciclajeAIController(
            GeminiAIService claudeAIService,
            UsuarioRepository usuarioRepository,
            ContenedorRepository contenedorRepository,
            ReciclajeRepository reciclajeRepository,
            NotificacionService notificacionService
    ) {
        this.claudeAIService = claudeAIService;
        this.usuarioRepository = usuarioRepository;
        this.contenedorRepository = contenedorRepository;
        this.reciclajeRepository = reciclajeRepository;
        this.notificacionService = notificacionService;
    }

    /**
     * Validar material con IA y registrar reciclaje si es válido
     * POST /api/reciclajes/validar-ia
     */
    @PostMapping("/validar-ia")
    @Transactional
    @Operation(
            summary = "Validar y registrar reciclaje con IA",
            description = "Valida el material con Claude AI y registra el reciclaje si es válido"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> validarYRegistrar(
            @Valid @RequestBody ValidacionIARequest request) {

        log.info("🎯 Iniciando validación con IA - Usuario: {}, Material: {}",
                request.getUsuarioId(), request.getTipoMaterialEsperado());

        // Validaciones básicas
        ValidationUtil.validarMongoId(request.getUsuarioId(), "ID de usuario");
        ValidationUtil.validarNoVacio(request.getTipoMaterialEsperado(), "tipo de material");
        ValidationUtil.validarPeso(request.getPesoKg());

        // 1. Validar usuario
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getUsuarioId()));

        // 2. Validar contenedor (si se proporciona)
        Contenedor contenedor = null;
        if (request.getContenedorCodigo() != null && !request.getContenedorCodigo().isEmpty()) {
            contenedor = contenedorRepository.findByCodigo(request.getContenedorCodigo())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Contenedor", "codigo", request.getContenedorCodigo()));

            // Verificar que el contenedor sea del mismo tipo de material
            if (!contenedor.getTipoMaterial().equalsIgnoreCase(request.getTipoMaterialEsperado())) {
                throw new BadRequestException(
                        String.format("El contenedor es para %s, pero intentas depositar %s",
                                contenedor.getTipoMaterial(), request.getTipoMaterialEsperado())
                );
            }
        }

        // 3. Validar con Claude AI
        ValidationResponse validation = claudeAIService.validateMaterial(
                request.getImagenBase64(),
                request.getTipoMaterialEsperado()
        );

        // 4. Si NO es válido, retornar error
        if (!validation.isEsValido()) {
            log.warn("⚠️ Material NO válido - Confianza: {}% - {}",
                    validation.getConfianza(), validation.getExplicacion());

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            String.format("❌ Material NO válido: %s", validation.getExplicacion()),
                            Map.of(
                                    "esValido", false,
                                    "confianza", validation.getConfianza(),
                                    "materialDetectado", validation.getMaterialDetectado(),
                                    "explicacion", validation.getExplicacion()
                            )
                    ));
        }

        // 5. Material VÁLIDO - Calcular EcoCoins
        int tarifaBase = TARIFAS.getOrDefault(request.getTipoMaterialEsperado(), 5);
        int ecoCoinsBase = (int) (request.getPesoKg() * tarifaBase);

        // 6. Aplicar bonus por confianza alta
        int bonusConfianza = 0;
        if (validation.getConfianza() >= 95) {
            bonusConfianza = (int) (ecoCoinsBase * 0.2); // +20%
        } else if (validation.getConfianza() >= 85) {
            bonusConfianza = (int) (ecoCoinsBase * 0.1); // +10%
        }

        int ecoCoinsTotal = ecoCoinsBase + bonusConfianza;

        // 7. Registrar reciclaje
        Reciclaje reciclaje = new Reciclaje();
        reciclaje.setUsuarioId(usuario.getId());
        reciclaje.setTipoMaterial(request.getTipoMaterialEsperado());
        reciclaje.setPesoKg(request.getPesoKg());
        reciclaje.setEcoCoinsGanadas(ecoCoinsTotal);
        reciclaje.setFecha(LocalDateTime.now());
        reciclaje.setVerificado(true);
        reciclaje.setVerificadoPor("IA-Claude");

        if (contenedor != null) {
            reciclaje.setContenedorCodigo(contenedor.getCodigo());
            reciclaje.setPuntoRecoleccion(contenedor.getUbicacion());

            // Actualizar capacidad del contenedor
            contenedor.setCapacidadActualKg(contenedor.getCapacidadActualKg() + request.getPesoKg());
            contenedorRepository.save(contenedor);
        }

        Reciclaje savedReciclaje = reciclajeRepository.save(reciclaje);

        // 8. Actualizar usuario
        usuario.setEcoCoins(usuario.getEcoCoins() + ecoCoinsTotal);
        usuario.setTotalReciclajes(usuario.getTotalReciclajes() + 1);
        usuario.setTotalKgReciclados(usuario.getTotalKgReciclados() + request.getPesoKg());
        usuarioRepository.save(usuario);

        // 9. Enviar notificación
        String mensajeNotificacion = String.format(
                "¡Material validado con IA! Ganaste +%d EcoCoins por reciclar %.2f kg de %s",
                ecoCoinsTotal, request.getPesoKg(), request.getTipoMaterialEsperado()
        );

        if (bonusConfianza > 0) {
            mensajeNotificacion += String.format(" (¡Bonus de +%d EcoCoins por alta confianza!)", bonusConfianza);
        }

        notificacionService.enviarNotificacion(
                usuario.getId(),
                "🤖 ¡Reciclaje validado con IA!",
                mensajeNotificacion,
                "success"
        );

        // 10. Preparar respuesta
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("mensaje", "✅ Material validado y reciclaje registrado exitosamente");
        resultado.put("esValido", true);
        resultado.put("confianza", validation.getConfianza());
        resultado.put("materialDetectado", validation.getMaterialDetectado());
        resultado.put("explicacion", validation.getExplicacion());
        resultado.put("ecoCoinsBase", ecoCoinsBase);
        resultado.put("bonusConfianza", bonusConfianza);
        resultado.put("ecoCoinsTotal", ecoCoinsTotal);
        resultado.put("reciclajeId", savedReciclaje.getId());
        resultado.put("nuevoBalance", usuario.getEcoCoins());

        log.info("✅ Reciclaje registrado exitosamente - ID: {}, EcoCoins: {} (+{} bonus)",
                savedReciclaje.getId(), ecoCoinsTotal, bonusConfianza);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Material validado con IA", resultado));
    }
}