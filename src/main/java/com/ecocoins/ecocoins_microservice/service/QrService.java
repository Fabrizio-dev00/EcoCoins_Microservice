package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.dto.ReciclajeQrRequest;
import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Contenedor;
import com.ecocoins.ecocoins_microservice.model.Reciclaje;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.ContenedorRepository;
import com.ecocoins.ecocoins_microservice.repository.ReciclajeRepository;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import com.ecocoins.ecocoins_microservice.util.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class QrService {

    private final ContenedorRepository contenedorRepository;
    private final ReciclajeRepository reciclajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    // Tarifas de EcoCoins por kg según material
    private static final Map<String, Integer> TARIFAS = Map.of(
            "Plástico", 5,
            "Papel", 3,
            "Vidrio", 7,
            "Metal", 10,
            "Cartón", 4,
            "Electrónico", 15,
            "Orgánico", 2,
            "Pilas", 20
    );

    public QrService(
            ContenedorRepository contenedorRepository,
            ReciclajeRepository reciclajeRepository,
            UsuarioRepository usuarioRepository,
            NotificacionService notificacionService
    ) {
        this.contenedorRepository = contenedorRepository;
        this.reciclajeRepository = reciclajeRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    /**
     * Validar que el código QR existe y está activo
     */
    public Contenedor validarQr(String codigoQr) {
        Contenedor contenedor = contenedorRepository.findByCodigo(codigoQr)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contenedor con código '" + codigoQr + "' no encontrado"
                ));

        // Verificar estado
        if (!"activo".equals(contenedor.getEstado())) {
            throw new BadRequestException("⚠️ Este contenedor no está disponible: " + contenedor.getEstado());
        }

        // Verificar si está lleno
        if (contenedor.estaLleno()) {
            throw new BadRequestException("⚠️ Contenedor lleno. Por favor usa otro.");
        }

        return contenedor;
    }

    /**
     * Registrar reciclaje escaneando QR
     */
    @Transactional
    public Map<String, Object> registrarReciclajeConQr(ReciclajeQrRequest request) {
        // Validaciones
        ValidationUtil.validarMongoId(request.getUsuarioId(), "ID de usuario");
        ValidationUtil.validarNoVacio(request.getContenedorCodigo(), "código del contenedor");
        ValidationUtil.validarPeso(request.getPesoKg());

        // 1. Validar contenedor
        Contenedor contenedor = validarQr(request.getContenedorCodigo());

        // 2. Validar usuario
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getUsuarioId()));

        // 3. Calcular EcoCoins
        int tarifa = TARIFAS.getOrDefault(contenedor.getTipoMaterial(), 5);
        int ecoCoinsGanadas = (int) (request.getPesoKg() * tarifa);

        // Bonus por primera vez del día
        if (esReciclajeDelDia(usuario.getId())) {
            ecoCoinsGanadas += 5; // Bonus de 5 EcoCoins
        }

        // 4. Crear reciclaje
        Reciclaje reciclaje = new Reciclaje();
        reciclaje.setUsuarioId(usuario.getId());
        reciclaje.setTipoMaterial(contenedor.getTipoMaterial());
        reciclaje.setPesoKg(request.getPesoKg());
        reciclaje.setEcoCoinsGanadas(ecoCoinsGanadas);
        reciclaje.setFecha(LocalDateTime.now());
        reciclaje.setContenedorCodigo(contenedor.getCodigo());
        reciclaje.setPuntoRecoleccion(contenedor.getUbicacion());
        reciclaje.setFotoUrl(request.getFotoUrl());
        reciclaje.setObservaciones(request.getObservaciones());
        reciclaje.setVerificado(false);

        // Guardar reciclaje
        Reciclaje saved = reciclajeRepository.save(reciclaje);

        // 5. Actualizar EcoCoins del usuario
        usuario.setEcoCoins(usuario.getEcoCoins() + ecoCoinsGanadas);
        usuario.setTotalReciclajes(usuario.getTotalReciclajes() + 1);
        usuario.setTotalKgReciclados(usuario.getTotalKgReciclados() + request.getPesoKg());

        // Actualizar nivel del usuario
        actualizarNivel(usuario);

        usuarioRepository.save(usuario);

        // 6. Actualizar capacidad del contenedor
        contenedor.setCapacidadActualKg(
                contenedor.getCapacidadActualKg() + request.getPesoKg()
        );
        contenedorRepository.save(contenedor);

        // 7. Enviar notificación
        notificacionService.enviarNotificacion(
                usuario.getId(),
                "🎉 ¡Reciclaje registrado!",
                String.format("Has ganado +%d EcoCoins por reciclar %.2f kg de %s",
                        ecoCoinsGanadas, request.getPesoKg(), contenedor.getTipoMaterial()),
                "success"
        );

        // 8. Preparar respuesta
        return Map.of(
                "mensaje", "✅ ¡Reciclaje registrado exitosamente!",
                "reciclajeId", saved.getId(),
                "ecoCoinsGanadas", ecoCoinsGanadas,
                "nuevoBalance", usuario.getEcoCoins(),
                "tipoMaterial", contenedor.getTipoMaterial(),
                "ubicacion", contenedor.getUbicacion(),
                "totalReciclajes", usuario.getTotalReciclajes(),
                "nivel", getNombreNivel(usuario.getNivel())
        );
    }

    /**
     * Obtener información del contenedor por código QR
     */
    public Map<String, Object> obtenerInfoContenedor(String codigoQr) {
        Contenedor contenedor = validarQr(codigoQr);

        return Map.of(
                "contenedorId", contenedor.getId(),
                "codigo", contenedor.getCodigo(),
                "tipoMaterial", contenedor.getTipoMaterial(),
                "ubicacion", contenedor.getUbicacion(),
                "estado", contenedor.getEstado(),
                "capacidadDisponible", contenedor.getCapacidadMaxKg() - contenedor.getCapacidadActualKg(),
                "porcentajeLlenado", contenedor.getPorcentajeLlenado(),
                "tarifaPorKg", TARIFAS.getOrDefault(contenedor.getTipoMaterial(), 5)
        );
    }

    /**
     * Verificar si el usuario ya recicló hoy
     */
    private boolean esReciclajeDelDia(String usuarioId) {
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        List<Reciclaje> reciclajesHoy = reciclajeRepository.findByUsuarioIdAndFechaGreaterThanEqual(
                usuarioId, inicioDia
        );
        return reciclajesHoy.isEmpty();
    }

    /**
     * Actualizar nivel del usuario según sus EcoCoins
     */
    private void actualizarNivel(Usuario usuario) {
        int ecoCoins = usuario.getEcoCoins();
        int nivelActual = usuario.getNivel();
        int nuevoNivel;

        if (ecoCoins >= 1000) {
            nuevoNivel = 4; // Platino
        } else if (ecoCoins >= 500) {
            nuevoNivel = 3; // Oro
        } else if (ecoCoins >= 200) {
            nuevoNivel = 2; // Plata
        } else {
            nuevoNivel = 1; // Bronce
        }

        // Si subió de nivel, enviar notificación
        if (nuevoNivel > nivelActual) {
            usuario.setNivel(nuevoNivel);
            notificacionService.enviarNotificacion(
                    usuario.getId(),
                    "🎊 ¡Subiste de nivel!",
                    String.format("¡Felicidades! Ahora eres nivel %s", getNombreNivel(nuevoNivel)),
                    "success"
            );
        }
    }

    /**
     * Obtener nombre del nivel
     */
    private String getNombreNivel(int nivel) {
        return switch (nivel) {
            case 1 -> "Bronce";
            case 2 -> "Plata";
            case 3 -> "Oro";
            case 4 -> "Platino";
            default -> "Desconocido";
        };
    }

    /**
     * Obtener tarifas de todos los materiales
     */
    public Map<String, Integer> obtenerTarifas() {
        return TARIFAS;
    }
}