package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.service.EstadisticasService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @GetMapping
    public Map<String, Object> obtenerEstadisticasGenerales() {
        return estadisticasService.obtenerEstadisticasDetalladas();
    }

    @GetMapping("/ecoins-por-usuario")
    public List<Map<String, Object>> obtenerEcoCoinsPorUsuario() {
        return estadisticasService.obtenerEcoCoinsPorUsuario();
    }

    @GetMapping("/ecoins-por-material")
    public List<Map<String, Object>> obtenerEcoCoinsPorMaterial() {
        return estadisticasService.obtenerEcoCoinsPorMaterial();
    }

    // ⭐⭐⭐ AGREGA ESTE ENDPOINT ANTES DEL DE /resumen ⭐⭐⭐
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerEstadisticasUsuario(@PathVariable String usuarioId) {

        Map<String, Object> estadisticas = new java.util.HashMap<>();

        // Resumen General
        Map<String, Object> resumenGeneral = new java.util.HashMap<>();
        resumenGeneral.put("totalReciclajes", 0);
        resumenGeneral.put("totalKgReciclados", 0.0);
        resumenGeneral.put("ecoCoinsActuales", 0);
        resumenGeneral.put("ecoCoinsGanados", 0);
        resumenGeneral.put("nivel", 1);
        resumenGeneral.put("promedioKgPorReciclaje", 0.0);

        // Distribución de materiales
        List<Map<String, Object>> distribucionMateriales = new java.util.ArrayList<>();

        // Tendencia semanal
        List<Map<String, Object>> tendenciaSemanal = new java.util.ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Map<String, Object> dia = new java.util.HashMap<>();
            dia.put("dia", "Día " + (i + 1));
            dia.put("reciclajes", 0);
            tendenciaSemanal.add(dia);
        }

        // Impacto ambiental
        Map<String, Object> impactoAmbiental = new java.util.HashMap<>();
        impactoAmbiental.put("co2Evitado", 0.0);

        Map<String, Object> equivalencias = new java.util.HashMap<>();
        equivalencias.put("arboles", 0);
        equivalencias.put("energia", 0.0);
        equivalencias.put("agua", 0.0);
        impactoAmbiental.put("equivalencias", equivalencias);

        // Comparativas
        Map<String, Object> comparativas = new java.util.HashMap<>();
        comparativas.put("promedioGeneral", 0.0);
        comparativas.put("tuPosicion", 0);
        comparativas.put("porcentajeSuperior", 0.0);

        // Rachas
        Map<String, Object> rachas = new java.util.HashMap<>();
        rachas.put("rachaActual", 0);
        rachas.put("mejorRacha", 0);

        // Construir respuesta
        estadisticas.put("resumenGeneral", resumenGeneral);
        estadisticas.put("distribucionMateriales", distribucionMateriales);
        estadisticas.put("tendenciaSemanal", tendenciaSemanal);
        estadisticas.put("impactoAmbiental", impactoAmbiental);
        estadisticas.put("comparativas", comparativas);
        estadisticas.put("rachas", rachas);

        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Obtener resumen de estadísticas de un usuario específico
     * GET /api/estadisticas/usuario/{usuarioId}/resumen
     */
    @GetMapping("/usuario/{usuarioId}/resumen")
    public Map<String, Object> obtenerResumenUsuario(@PathVariable String usuarioId) {
        Map<String, Object> resumen = new java.util.HashMap<>();

        // Datos básicos de prueba - puedes implementar la lógica real después
        resumen.put("totalReciclajes", 0);
        resumen.put("totalKgReciclados", 0.0);
        resumen.put("ecoCoinsGanados", 0);
        resumen.put("nivel", 1);
        resumen.put("posicionRanking", 0);

        return resumen;
    }
}
