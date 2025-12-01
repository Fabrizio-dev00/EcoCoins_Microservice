package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.service.MapaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mapa")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Mapa", description = "Puntos de reciclaje en el mapa")
public class MapaController {

    private final MapaService mapaService;

    public MapaController(MapaService mapaService) {
        this.mapaService = mapaService;
    }

    /**
     * Obtener todos los puntos de reciclaje
     * GET /api/mapa/puntos
     */
    @GetMapping("/puntos")
    @Operation(summary = "Listar puntos", description = "Obtiene todos los puntos de reciclaje disponibles")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerPuntos(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String estado) {

        List<Map<String, Object>> puntos = mapaService.obtenerPuntosReciclaje(tipo, estado);
        return ResponseEntity.ok(ApiResponse.success(puntos));
    }

    /**
     * Obtener punto por ID
     * GET /api/mapa/puntos/{id}
     */
    @GetMapping("/puntos/{id}")
    @Operation(summary = "Detalle del punto", description = "Obtiene información detallada de un punto de reciclaje")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerPuntoPorId(
            @PathVariable String id) {

        Map<String, Object> punto = mapaService.obtenerPuntoPorId(id);
        return ResponseEntity.ok(ApiResponse.success(punto));
    }

    /**
     * Buscar puntos cercanos
     * GET /api/mapa/puntos/cercanos
     */
    @GetMapping("/puntos/cercanos")
    @Operation(summary = "Puntos cercanos", description = "Busca puntos de reciclaje cerca de una ubicación")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> buscarCercanos(
            @RequestParam double latitud,
            @RequestParam double longitud,
            @RequestParam(defaultValue = "5.0") double radioKm) {

        List<Map<String, Object>> puntosCercanos = mapaService.buscarPuntosCercanos(
                latitud, longitud, radioKm
        );

        return ResponseEntity.ok(ApiResponse.success(puntosCercanos));
    }

    /**
     * Filtrar por tipo de material
     * GET /api/mapa/puntos/filtrar
     */
    @GetMapping("/puntos/filtrar")
    @Operation(summary = "Filtrar puntos", description = "Filtra puntos por tipo de material aceptado")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> filtrarPorMaterial(
            @RequestParam String material) {

        List<Map<String, Object>> puntosFiltrados = mapaService.filtrarPorMaterial(material);
        return ResponseEntity.ok(ApiResponse.success(puntosFiltrados));
    }
}