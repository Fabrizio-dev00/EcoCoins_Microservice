package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.service.RecompensaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecocoins.ecocoins_microservice.model.Recompensa;
import java.util.List;

@RestController
@RequestMapping("/api/recompensas")
public class RecompensaController {

    @Autowired
    private RecompensaService recompensaService;

    @GetMapping
    public ResponseEntity<List<Recompensa>> listarRecompensas() {
        return ResponseEntity.ok(recompensaService.listarRecompensas());
    }

    // ⭐ ENDPOINT DISPONIBLES
    @GetMapping("/disponibles")
    public ResponseEntity<List<Recompensa>> obtenerRecompensasDisponibles() {
        List<Recompensa> recompensas = recompensaService.listarRecompensas();
        // Filtrar solo las que tienen stock > 0
        List<Recompensa> disponibles = recompensas.stream()
                .filter(r -> r.getStock() > 0)
                .toList();
        return ResponseEntity.ok(disponibles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recompensa> obtenerPorId(@PathVariable String id) {
        return recompensaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Recompensa> crearRecompensa(@RequestBody Recompensa recompensa) {
        return ResponseEntity.ok(recompensaService.crearRecompensa(recompensa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recompensa> actualizarRecompensa(
            @PathVariable String id,
            @RequestBody Recompensa recompensaActualizada) {
        return recompensaService.actualizarRecompensa(id, recompensaActualizada)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecompensa(@PathVariable String id) {
        boolean eliminado = recompensaService.eliminarRecompensa(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/canjear")
    public ResponseEntity<String> canjearRecompensa(@PathVariable String id) {
        boolean exito = recompensaService.canjearRecompensa(id);
        if (exito) {
            return ResponseEntity.ok("Recompensa canjeada correctamente ✅");
        }
        return ResponseEntity.badRequest().body("No hay stock disponible ❌");
    }
}
