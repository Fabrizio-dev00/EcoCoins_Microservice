package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.model.Notificacion;
import com.ecocoins.ecocoins_microservice.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Notificaciones", description = "Sistema de notificaciones en tiempo real")
public class NotificacionesController {

    private final NotificacionService notificacionService;

    public NotificacionesController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    /**
     * Obtener notificaciones de un usuario
     * GET /api/notificaciones/usuario/{usuarioId}
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Mis notificaciones", description = "Obtiene todas las notificaciones del usuario")
    public ResponseEntity<ApiResponse<List<Notificacion>>> obtenerNotificaciones(
            @PathVariable String usuarioId) {

        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(notificaciones));
    }

    /**
     * Obtener notificaciones no leídas
     * GET /api/notificaciones/usuario/{usuarioId}/no-leidas
     */
    @GetMapping("/usuario/{usuarioId}/no-leidas")
    @Operation(summary = "Notificaciones no leídas", description = "Obtiene solo las notificaciones no leídas")
    public ResponseEntity<ApiResponse<List<Notificacion>>> obtenerNoLeidas(
            @PathVariable String usuarioId) {

        List<Notificacion> noLeidas = notificacionService.obtenerNotificacionesNoLeidas(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(noLeidas));
    }

    /**
     * Contar notificaciones no leídas
     * GET /api/notificaciones/usuario/{usuarioId}/contar
     */
    @GetMapping("/usuario/{usuarioId}/contar")
    @Operation(summary = "Contar no leídas", description = "Cuenta las notificaciones no leídas")
    public ResponseEntity<ApiResponse<Map<String, Long>>> contarNoLeidas(
            @PathVariable String usuarioId) {

        long count = notificacionService.contarNoLeidas(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("noLeidas", count)));
    }

    /**
     * Marcar notificación como leída
     * PUT /api/notificaciones/{notificacionId}/leer
     */
    @PutMapping("/{notificacionId}/leer")
    @Operation(summary = "Marcar como leída", description = "Marca una notificación como leída")
    public ResponseEntity<ApiResponse<Notificacion>> marcarComoLeida(
            @PathVariable String notificacionId) {

        Notificacion notificacion = notificacionService.marcarComoLeida(notificacionId);
        return ResponseEntity.ok(ApiResponse.success("Notificación marcada como leída", notificacion));
    }

    /**
     * Marcar todas como leídas
     * PUT /api/notificaciones/usuario/{usuarioId}/leer-todas
     */
    @PutMapping("/usuario/{usuarioId}/leer-todas")
    @Operation(summary = "Marcar todas como leídas", description = "Marca todas las notificaciones del usuario como leídas")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> marcarTodasComoLeidas(
            @PathVariable String usuarioId) {

        int marcadas = notificacionService.marcarTodasComoLeidas(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(
                String.format("%d notificaciones marcadas como leídas", marcadas),
                Map.of("marcadas", marcadas)
        ));
    }

    /**
     * Enviar notificación (admin)
     * POST /api/notificaciones/enviar
     */
    @PostMapping("/enviar")
    @Operation(summary = "Enviar notificación", description = "Envía una notificación a un usuario (solo admin)")
    public ResponseEntity<ApiResponse<Notificacion>> enviarNotificacion(
            @RequestBody Map<String, String> request) {

        String usuarioId = request.get("usuarioId");
        String titulo = request.get("titulo");
        String mensaje = request.get("mensaje");
        String tipo = request.getOrDefault("tipo", "info");

        Notificacion notificacion = notificacionService.enviarNotificacion(
                usuarioId, titulo, mensaje, tipo
        );

        return ResponseEntity.ok(ApiResponse.success("Notificación enviada", notificacion));
    }

    /**
     * Enviar notificación global (admin)
     * POST /api/notificaciones/enviar-global
     */
    @PostMapping("/enviar-global")
    @Operation(summary = "Enviar notificación global", description = "Envía una notificación a todos los usuarios (solo admin)")
    public ResponseEntity<ApiResponse<Notificacion>> enviarNotificacionGlobal(
            @RequestBody Map<String, String> request) {

        String titulo = request.get("titulo");
        String mensaje = request.get("mensaje");
        String tipo = request.getOrDefault("tipo", "info");

        Notificacion notificacion = notificacionService.enviarNotificacionGlobal(
                titulo, mensaje, tipo
        );

        return ResponseEntity.ok(ApiResponse.success("Notificación global enviada", notificacion));
    }
}