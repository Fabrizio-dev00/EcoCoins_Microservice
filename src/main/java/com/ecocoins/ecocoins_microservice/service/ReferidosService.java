package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ReferidosService {

    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    // Recompensas
    private static final int ECOCOINS_REFERIDOR = 50;
    private static final int ECOCOINS_NUEVO_USUARIO = 25;

    // Almacenamiento temporal de códigos (en producción usar Redis o DB)
    private final Map<String, String> codigosReferidos = new HashMap<>();

    public ReferidosService(UsuarioRepository usuarioRepository,
                            NotificacionService notificacionService) {
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    /**
     * Obtener información de referidos de un usuario
     */
    public Map<String, Object> obtenerReferidosUsuario(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        // Obtener o generar código
        String codigo = obtenerOGenerarCodigo(usuarioId);

        // Buscar usuarios referidos (simulación - en producción necesitas campo en Usuario)
        List<Map<String, Object>> referidos = obtenerListaReferidos(usuarioId);

        int totalReferidos = referidos.size();
        int totalEcoCoinsGanados = totalReferidos * ECOCOINS_REFERIDOR;

        return Map.of(
                "codigoReferido", codigo,
                "totalReferidos", totalReferidos,
                "totalEcoCoinsGanados", totalEcoCoinsGanados,
                "ecocoinsPorReferido", ECOCOINS_REFERIDOR,
                "ecocoinsNuevoUsuario", ECOCOINS_NUEVO_USUARIO,
                "referidos", referidos
        );
    }

    /**
     * Generar código de referido único
     */
    public String generarCodigoReferido(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        // Generar código único basado en el usuario
        String codigo = generarCodigoUnico(usuario);
        codigosReferidos.put(codigo, usuarioId);

        return codigo;
    }

    /**
     * Registrar un referido
     */
    @Transactional
    public Map<String, Object> registrarReferido(String codigoReferido, String nuevoUsuarioId) {
        // Validar código
        if (!codigosReferidos.containsKey(codigoReferido)) {
            throw new BadRequestException("Código de referido inválido");
        }

        String referidorId = codigosReferidos.get(codigoReferido);

        // Obtener usuarios
        Usuario referidor = usuarioRepository.findById(referidorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario referidor", "id", referidorId));

        Usuario nuevoUsuario = usuarioRepository.findById(nuevoUsuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Nuevo usuario", "id", nuevoUsuarioId));

        // Dar recompensas
        referidor.setEcoCoins(referidor.getEcoCoins() + ECOCOINS_REFERIDOR);
        nuevoUsuario.setEcoCoins(nuevoUsuario.getEcoCoins() + ECOCOINS_NUEVO_USUARIO);

        usuarioRepository.save(referidor);
        usuarioRepository.save(nuevoUsuario);

        // Enviar notificaciones
        notificacionService.enviarNotificacion(
                referidorId,
                "🎉 ¡Nuevo referido!",
                String.format("¡%s se unió con tu código! Ganaste +%d EcoCoins",
                        nuevoUsuario.getNombre(), ECOCOINS_REFERIDOR),
                "success"
        );

        notificacionService.enviarNotificacion(
                nuevoUsuarioId,
                "🎁 ¡Bienvenido!",
                String.format("Gracias por usar el código de %s. ¡Ganaste +%d EcoCoins de regalo!",
                        referidor.getNombre(), ECOCOINS_NUEVO_USUARIO),
                "success"
        );

        return Map.of(
                "mensaje", "Referido registrado exitosamente",
                "referidor", referidor.getNombre(),
                "ecoCoinsReferidor", ECOCOINS_REFERIDOR,
                "ecoCoinsNuevoUsuario", ECOCOINS_NUEVO_USUARIO
        );
    }

    /**
     * Validar código de referido
     */
    public Map<String, Object> validarCodigoReferido(String codigo) {
        if (!codigosReferidos.containsKey(codigo)) {
            return Map.of(
                    "valido", false,
                    "mensaje", "Código de referido inválido"
            );
        }

        String usuarioId = codigosReferidos.get(codigo);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElse(null);

        if (usuario == null) {
            return Map.of(
                    "valido", false,
                    "mensaje", "Usuario referidor no encontrado"
            );
        }

        return Map.of(
                "valido", true,
                "mensaje", "Código válido",
                "referidor", usuario.getNombre(),
                "recompensa", ECOCOINS_NUEVO_USUARIO
        );
    }

    /**
     * Generar código único para el usuario
     */
    private String generarCodigoUnico(Usuario usuario) {
        String base = usuario.getNombre()
                .replaceAll("[^a-zA-Z0-9]", "")
                .toUpperCase();

        if (base.length() > 6) {
            base = base.substring(0, 6);
        }

        // Agregar números aleatorios
        Random random = new Random();
        int numero = 1000 + random.nextInt(9000);

        return base + numero;
    }

    /**
     * Obtener o generar código para un usuario
     */
    private String obtenerOGenerarCodigo(String usuarioId) {
        // Buscar si ya tiene código
        for (Map.Entry<String, String> entry : codigosReferidos.entrySet()) {
            if (entry.getValue().equals(usuarioId)) {
                return entry.getKey();
            }
        }

        // Si no tiene, generar uno nuevo
        return generarCodigoReferido(usuarioId);
    }

    /**
     * Obtener lista de referidos (simulación)
     * En producción, necesitarías un campo "referidoPor" en Usuario
     */
    private List<Map<String, Object>> obtenerListaReferidos(String usuarioId) {
        // Simulación - en producción hacer query real
        List<Map<String, Object>> referidos = new ArrayList<>();

        // Ejemplo de datos mock
        referidos.add(Map.of(
                "nombre", "María García",
                "fechaRegistro", "2024-11-15",
                "ecoCoinsGanados", ECOCOINS_REFERIDOR
        ));

        referidos.add(Map.of(
                "nombre", "Carlos López",
                "fechaRegistro", "2024-11-20",
                "ecoCoinsGanados", ECOCOINS_REFERIDOR
        ));

        return referidos;
    }
}