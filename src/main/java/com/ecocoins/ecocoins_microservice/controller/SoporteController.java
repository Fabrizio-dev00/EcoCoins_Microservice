package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.service.SoporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/soporte")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Soporte", description = "Sistema de soporte con FAQs y tickets")
public class SoporteController {

    private final SoporteService soporteService;

    public SoporteController(SoporteService soporteService) {
        this.soporteService = soporteService;
    }

    // ========== FAQs ==========

    /**
     * Obtener todas las FAQs
     * GET /api/soporte/faqs
     */
    @GetMapping("/faqs")
    @Operation(summary = "Listar FAQs", description = "Obtiene todas las preguntas frecuentes")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerFAQs(
            @RequestParam(required = false) String categoria) {

        List<Map<String, Object>> faqs = soporteService.obtenerFAQs(categoria);
        return ResponseEntity.ok(ApiResponse.success(faqs));
    }

    /**
     * Obtener FAQ por ID
     * GET /api/soporte/faqs/{id}
     */
    @GetMapping("/faqs/{id}")
    @Operation(summary = "Detalle de FAQ", description = "Obtiene información detallada de una FAQ")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerFAQPorId(
            @PathVariable String id) {

        Map<String, Object> faq = soporteService.obtenerFAQPorId(id);
        return ResponseEntity.ok(ApiResponse.success(faq));
    }

    /**
     * Marcar FAQ como útil
     * POST /api/soporte/faqs/{id}/util
     */
    @PostMapping("/faqs/{id}/util")
    @Operation(summary = "Marcar como útil", description = "Incrementa el contador de utilidad de una FAQ")
    public ResponseEntity<ApiResponse<Map<String, Object>>> marcarFAQUtil(
            @PathVariable String id) {

        Map<String, Object> faq = soporteService.marcarFAQUtil(id);
        return ResponseEntity.ok(ApiResponse.success("Gracias por tu feedback", faq));
    }

    // ========== TICKETS ==========

    /**
     * Obtener tickets de un usuario
     * GET /api/soporte/tickets/usuario/{usuarioId}
     */
    @GetMapping("/tickets/usuario/{usuarioId}")
    @Operation(summary = "Mis tickets", description = "Obtiene todos los tickets del usuario")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerTicketsUsuario(
            @PathVariable String usuarioId,
            @RequestParam(required = false) String estado) {

        List<Map<String, Object>> tickets = soporteService.obtenerTicketsUsuario(usuarioId, estado);
        return ResponseEntity.ok(ApiResponse.success(tickets));
    }

    /**
     * Obtener ticket por ID
     * GET /api/soporte/tickets/{id}
     */
    @GetMapping("/tickets/{id}")
    @Operation(summary = "Detalle del ticket", description = "Obtiene información completa de un ticket")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerTicketPorId(
            @PathVariable String id) {

        Map<String, Object> ticket = soporteService.obtenerTicketPorId(id);
        return ResponseEntity.ok(ApiResponse.success(ticket));
    }

    /**
     * Crear nuevo ticket
     * POST /api/soporte/tickets
     */
    @PostMapping("/tickets")
    @Operation(summary = "Crear ticket", description = "Crea un nuevo ticket de soporte")
    public ResponseEntity<ApiResponse<Map<String, Object>>> crearTicket(
            @RequestBody Map<String, String> request) {

        String usuarioId = request.get("usuarioId");
        String asunto = request.get("asunto");
        String descripcion = request.get("descripcion");
        String categoria = request.get("categoria");
        String prioridad = request.get("prioridad");

        Map<String, Object> ticket = soporteService.crearTicket(
                usuarioId, asunto, descripcion, categoria, prioridad
        );

        return ResponseEntity.ok(ApiResponse.success(
                "Ticket creado exitosamente. Te responderemos pronto.",
                ticket
        ));
    }

    /**
     * Responder a un ticket
     * POST /api/soporte/tickets/{id}/responder
     */
    @PostMapping("/tickets/{id}/responder")
    @Operation(summary = "Responder ticket", description = "Agrega una respuesta a un ticket")
    public ResponseEntity<ApiResponse<Map<String, Object>>> responderTicket(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {

        String usuarioId = request.get("usuarioId");
        String mensaje = request.get("mensaje");

        Map<String, Object> ticket = soporteService.responderTicket(id, usuarioId, mensaje);

        return ResponseEntity.ok(ApiResponse.success(
                "Respuesta agregada exitosamente",
                ticket
        ));
    }

    /**
     * Cambiar estado del ticket
     * PUT /api/soporte/tickets/{id}/estado
     */
    @PutMapping("/tickets/{id}/estado")
    @Operation(summary = "Cambiar estado", description = "Actualiza el estado de un ticket (solo admin)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> cambiarEstadoTicket(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {

        String nuevoEstado = request.get("estado");

        Map<String, Object> ticket = soporteService.cambiarEstadoTicket(id, nuevoEstado);

        return ResponseEntity.ok(ApiResponse.success(
                "Estado actualizado exitosamente",
                ticket
        ));
    }
}