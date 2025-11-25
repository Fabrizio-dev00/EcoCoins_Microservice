package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.dto.CanjeRequest;
import com.ecocoins.ecocoins_microservice.model.Canje;
import com.ecocoins.ecocoins_microservice.service.CanjeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/canjes")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Canjes", description = "Gestión de canjes de recompensas")
public class CanjeController {

    private final CanjeService canjeService;

    public CanjeController(CanjeService canjeService) {
        this.canjeService = canjeService;
    }

    /**
     * Canjear una recompensa
     * POST /api/canjes/canjear
     */
    @PostMapping("/canjear")
    @Operation(summary = "Canjear recompensa", description = "Permite a un usuario canjear una recompensa por EcoCoins")
    public ResponseEntity<ApiResponse<Map<String, Object>>> canjearRecompensa(
            @Valid @RequestBody CanjeRequest request) {

        Map<String, Object> resultado = canjeService.canjearRecompensa(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Canje realizado exitosamente", resultado));
    }

    /**
     * Listar canjes de un usuario
     * GET /api/canjes/usuario/{usuarioId}
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Mis canjes", description = "Obtiene todos los canjes de un usuario")
    public ResponseEntity<ApiResponse<List<Canje>>> listarCanjesPorUsuario(
            @PathVariable String usuarioId) {

        List<Canje> canjes = canjeService.listarCanjesPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(canjes));
    }

    /**
     * Obtener un canje por ID
     * GET /api/canjes/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Detalle de canje", description = "Obtiene los detalles de un canje específico")
    public ResponseEntity<ApiResponse<Canje>> obtenerCanjePorId(@PathVariable String id) {
        Canje canje = canjeService.obtenerCanjePorId(id);
        return ResponseEntity.ok(ApiResponse.success(canje));
    }

    /**
     * Listar todos los canjes (admin)
     * GET /api/canjes
     */
    @GetMapping
    @Operation(summary = "Listar todos los canjes", description = "Obtiene todos los canjes del sistema (solo admin)")
    public ResponseEntity<ApiResponse<List<Canje>>> listarTodos() {
        List<Canje> canjes = canjeService.listarTodosCanjes();
        return ResponseEntity.ok(ApiResponse.success(canjes));
    }

    /**
     * Cambiar estado de un canje
     * PATCH /api/canjes/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado", description = "Actualiza el estado de un canje (solo admin)")
    public ResponseEntity<ApiResponse<Canje>> cambiarEstado(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String nuevoEstado = body.get("estado");
        Canje actualizado = canjeService.cambiarEstadoCanje(id, nuevoEstado);

        return ResponseEntity.ok(
                ApiResponse.success("Estado actualizado correctamente", actualizado)
        );
    }

    /**
     * Cancelar un canje
     * POST /api/canjes/{id}/cancelar
     */
    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar canje", description = "Cancela un canje y devuelve los EcoCoins al usuario")
    public ResponseEntity<ApiResponse<Map<String, Object>>> cancelarCanje(
            @PathVariable String id,
            @RequestParam String usuarioId) {

        Map<String, Object> resultado = canjeService.cancelarCanje(id, usuarioId);
        return ResponseEntity.ok(ApiResponse.success("Canje cancelado exitosamente", resultado));
    }
}