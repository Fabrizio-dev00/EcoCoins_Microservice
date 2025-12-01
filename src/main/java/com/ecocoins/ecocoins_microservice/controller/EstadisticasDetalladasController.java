package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.service.EstadisticasDetalladasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas/detalladas")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Estadísticas Detalladas", description = "Análisis completo del impacto ambiental")
public class EstadisticasDetalladasController {

    private final EstadisticasDetalladasService estadisticasService;

    public EstadisticasDetalladasController(EstadisticasDetalladasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    /**
     * Obtener estadísticas detalladas de un usuario
     * GET /api/estadisticas/detalladas/{usuarioId}
     */
    @GetMapping("/{usuarioId}")
    @Operation(summary = "Estadísticas completas", description = "Obtiene todas las estadísticas detalladas del usuario")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerEstadisticasCompletas(
            @PathVariable String usuarioId) {

        Map<String, Object> estadisticas = estadisticasService.obtenerEstadisticasCompletas(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(estadisticas));
    }

    /**
     * Obtener resumen general
     * GET /api/estadisticas/detalladas/{usuarioId}/resumen
     */
    @GetMapping("/{usuarioId}/resumen")
    @Operation(summary = "Resumen general", description = "Obtiene el resumen general de estadísticas")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerResumen(
            @PathVariable String usuarioId) {

        Map<String, Object> resumen = estadisticasService.obtenerResumenGeneral(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(resumen));
    }

    /**
     * Obtener distribución por materiales
     * GET /api/estadisticas/detalladas/{usuarioId}/materiales
     */
    @GetMapping("/{usuarioId}/materiales")
    @Operation(summary = "Distribución por materiales", description = "Obtiene la distribución de reciclajes por tipo de material")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerDistribucionMateriales(
            @PathVariable String usuarioId) {

        Map<String, Object> distribucion = estadisticasService.obtenerDistribucionMateriales(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(distribucion));
    }

    /**
     * Obtener impacto ambiental
     * GET /api/estadisticas/detalladas/{usuarioId}/impacto
     */
    @GetMapping("/{usuarioId}/impacto")
    @Operation(summary = "Impacto ambiental", description = "Calcula el impacto ambiental del usuario")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerImpactoAmbiental(
            @PathVariable String usuarioId) {

        Map<String, Object> impacto = estadisticasService.calcularImpactoAmbiental(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(impacto));
    }
}