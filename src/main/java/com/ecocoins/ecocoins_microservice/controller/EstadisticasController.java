package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.service.EstadisticasService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = "*")
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
}
