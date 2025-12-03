package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.service.LogrosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logros")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Logros", description = "Sistema de logros y achievements")
public class LogrosController {

    private final LogrosService logrosService;

    public LogrosController(LogrosService logrosService) {
        this.logrosService = logrosService;
    }

    /**
     * Obtener resumen de logros de un usuario
     * GET /api/logros/usuario/{usuarioId}/resumen
     */
    @GetMapping("/usuario/{usuarioId}/resumen")
    @Operation(summary = "Resumen de logros", description = "Obtiene un resumen de los logros del usuario")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerResumenLogros(
            @PathVariable String usuarioId) {

        Map<String, Object> logrosData = logrosService.obtenerLogrosUsuario(usuarioId);

        // Extraer solo el resumen (sin la lista completa de logros)
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalLogros", logrosData.get("totalLogros"));
        resumen.put("logrosDesbloqueados", logrosData.get("logrosCompletados"));
        resumen.put("porcentajeCompletado", logrosData.get("progresoPorcentaje"));
        resumen.put("ecoCoinsGanados", logrosData.get("totalEcoCoinsGanados"));

        return ResponseEntity.ok(ApiResponse.success(resumen));
    }

    /**
     * Obtener todos los logros disponibles
     * GET /api/logros
     */
    @GetMapping
    @Operation(summary = "Listar logros", description = "Obtiene todos los logros del sistema")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listarLogros() {
        List<Map<String, Object>> logros = logrosService.obtenerTodosLosLogros();
        return ResponseEntity.ok(ApiResponse.success(logros));
    }

    /**
     * Obtener logros de un usuario
     * GET /api/logros/usuario/{usuarioId}
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Logros del usuario", description = "Obtiene los logros del usuario con su progreso")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerLogrosUsuario(
            @PathVariable String usuarioId) {

        Map<String, Object> logrosUsuario = logrosService.obtenerLogrosUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(logrosUsuario));
    }

    /**
     * Verificar y actualizar logros de un usuario
     * POST /api/logros/usuario/{usuarioId}/verificar
     */
    @PostMapping("/usuario/{usuarioId}/verificar")
    @Operation(summary = "Verificar logros", description = "Verifica y actualiza los logros desbloqueados del usuario")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> verificarLogros(
            @PathVariable String usuarioId) {

        List<Map<String, Object>> logrosNuevos = logrosService.verificarYActualizarLogros(usuarioId);

        if (logrosNuevos.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("No hay nuevos logros desbloqueados", logrosNuevos));
        }

        return ResponseEntity.ok(ApiResponse.success(
                String.format("¡%d nuevo(s) logro(s) desbloqueado(s)!", logrosNuevos.size()),
                logrosNuevos
        ));
    }
}