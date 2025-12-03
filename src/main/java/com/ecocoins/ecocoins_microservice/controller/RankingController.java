package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ranking")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ranking", description = "Sistema de ranking y tabla de posiciones")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    /**
     * Obtener ranking global (histórico)
     * GET /api/ranking/global
     */
    @GetMapping("/global")
    @Operation(summary = "Obtener ranking global", description = "Obtiene el ranking histórico de todos los usuarios")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerRankingGlobal() {
        List<Map<String, Object>> ranking = rankingService.obtenerRankingPorPeriodo("HISTORICO");
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    /**
     * Obtener ranking semanal
     * GET /api/ranking/semanal
     */
    @GetMapping("/semanal")
    @Operation(summary = "Obtener ranking semanal", description = "Obtiene el ranking de la última semana")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerRankingSemanal() {
        List<Map<String, Object>> ranking = rankingService.obtenerRankingPorPeriodo("SEMANAL");
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    /**
     * Obtener ranking mensual
     * GET /api/ranking/mensual
     */
    @GetMapping("/mensual")
    @Operation(summary = "Obtener ranking mensual", description = "Obtiene el ranking del último mes")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerRankingMensual() {
        List<Map<String, Object>> ranking = rankingService.obtenerRankingPorPeriodo("MENSUAL");
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    /**
     * Obtener ranking por periodo (genérico - mantener por compatibilidad)
     * GET /api/ranking/{periodo}
     */
    @GetMapping("/{periodo}")
    @Operation(summary = "Obtener ranking", description = "Obtiene el ranking de usuarios por periodo (SEMANAL, MENSUAL, HISTORICO)")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerRanking(
            @PathVariable String periodo) {

        List<Map<String, Object>> ranking = rankingService.obtenerRankingPorPeriodo(periodo);
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    /**
     * Obtener posición de un usuario
     * GET /api/ranking/usuario/{usuarioId}/posicion
     */
    @GetMapping("/usuario/{usuarioId}/posicion")
    @Operation(summary = "Posición del usuario", description = "Obtiene la posición del usuario en el ranking actual")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerPosicionUsuario(
            @PathVariable String usuarioId,
            @RequestParam(defaultValue = "MENSUAL") String periodo) {

        Map<String, Object> posicion = rankingService.obtenerPosicionUsuario(usuarioId, periodo);
        return ResponseEntity.ok(ApiResponse.success(posicion));
    }

    /**
     * Obtener top 3 (podio)
     * GET /api/ranking/podio/{periodo}
     */
    @GetMapping("/podio/{periodo}")
    @Operation(summary = "Top 3 del ranking", description = "Obtiene los 3 primeros lugares del ranking")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerPodio(
            @PathVariable String periodo) {

        List<Map<String, Object>> podio = rankingService.obtenerTop3(periodo);
        return ResponseEntity.ok(ApiResponse.success(podio));
    }
}
