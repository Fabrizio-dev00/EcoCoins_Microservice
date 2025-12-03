package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.model.Reciclaje;
import com.ecocoins.ecocoins_microservice.service.ReciclajeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reciclajes")
public class ReciclajeController {

    private final ReciclajeService reciclajeService;

    public ReciclajeController(ReciclajeService reciclajeService) {
        this.reciclajeService = reciclajeService;
    }

    @GetMapping
    public List<Reciclaje> listar() {
        return reciclajeService.listarReciclajes();
    }

    @GetMapping("/usuario/{id}")
    public List<Reciclaje> listarPorUsuario(@PathVariable String id) {
        return reciclajeService.listarPorUsuario(id);
    }

    @PostMapping
    public Reciclaje registrar(@RequestBody Reciclaje reciclaje) {
        return reciclajeService.registrarReciclaje(reciclaje);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        reciclajeService.eliminarReciclaje(id);
    }
}
