package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Notificacion;
import com.ecocoins.ecocoins_microservice.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    /**
     * Enviar notificación a un usuario específico
     */
    public Notificacion enviarNotificacion(String usuarioId, String titulo, String mensaje, String tipo) {
        Notificacion notificacion = new Notificacion(usuarioId, titulo, mensaje);
        notificacion.setTipo(tipo != null ? tipo : "info");
        return notificacionRepository.save(notificacion);
    }

    /**
     * Enviar notificación global (a todos los usuarios)
     */
    public Notificacion enviarNotificacionGlobal(String titulo, String mensaje, String tipo) {
        Notificacion notificacion = new Notificacion(null, titulo, mensaje);
        notificacion.setTipo(tipo != null ? tipo : "info");
        return notificacionRepository.save(notificacion);
    }

    /**
     * Obtener notificaciones de un usuario (incluye globales)
     */
    public List<Notificacion> obtenerNotificacionesUsuario(String usuarioId) {
        return notificacionRepository.findByUsuarioIdOrGlobal(usuarioId);
    }

    /**
     * Obtener notificaciones no leídas de un usuario
     */
    public List<Notificacion> obtenerNotificacionesNoLeidas(String usuarioId) {
        return notificacionRepository.findNoLeidasByUsuarioIdOrGlobal(usuarioId);
    }

    /**
     * Contar notificaciones no leídas
     */
    public long contarNoLeidas(String usuarioId) {
        return notificacionRepository.countByUsuarioIdAndLeida(usuarioId, false);
    }

    /**
     * Marcar notificación como leída
     */
    public Notificacion marcarComoLeida(String notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificacion", "id", notificacionId));

        if (!notificacion.isLeida()) {
            notificacion.setLeida(true);
            notificacion.setFechaLectura(LocalDateTime.now());
            return notificacionRepository.save(notificacion);
        }

        return notificacion;
    }

    /**
     * Marcar todas las notificaciones de un usuario como leídas
     */
    public int marcarTodasComoLeidas(String usuarioId) {
        List<Notificacion> noLeidas = notificacionRepository.findByUsuarioIdAndLeida(usuarioId, false);

        for (Notificacion notificacion : noLeidas) {
            notificacion.setLeida(true);
            notificacion.setFechaLectura(LocalDateTime.now());
            notificacionRepository.save(notificacion);
        }

        return noLeidas.size();
    }

    /**
     * Eliminar notificaciones antiguas (más de 90 días)
     */
    public void limpiarNotificacionesAntiguas() {
        LocalDateTime hace90Dias = LocalDateTime.now().minusDays(90);
        notificacionRepository.deleteOlderThan(hace90Dias);
    }

    /**
     * Obtener estadísticas de notificaciones
     */
    public Map<String, Object> obtenerEstadisticas() {
        long total = notificacionRepository.count();

        return Map.of(
                "totalNotificaciones", total,
                "notificacionesPorTipo", Map.of(
                        "info", notificacionRepository.findByTipo("info").size(),
                        "success", notificacionRepository.findByTipo("success").size(),
                        "warning", notificacionRepository.findByTipo("warning").size(),
                        "error", notificacionRepository.findByTipo("error").size(),
                        "promo", notificacionRepository.findByTipo("promo").size()
                )
        );
    }
}