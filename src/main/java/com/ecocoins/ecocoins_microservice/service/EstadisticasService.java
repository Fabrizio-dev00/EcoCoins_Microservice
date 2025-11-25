package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.model.Reciclaje;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.ReciclajeRepository;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    private final UsuarioRepository usuarioRepository;
    private final ReciclajeRepository reciclajeRepository;

    public EstadisticasService(UsuarioRepository usuarioRepository, ReciclajeRepository reciclajeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.reciclajeRepository = reciclajeRepository;
    }

    public Map<String, Object> obtenerEstadisticasDetalladas() {
        Map<String, Object> estadisticas = new HashMap<>();

        long totalUsuarios = usuarioRepository.count();
        long totalReciclajes = reciclajeRepository.count();
        int totalEcoCoins = usuarioRepository.findAll().stream()
                .mapToInt(Usuario::getEcoCoins)
                .sum();

        estadisticas.put("totalUsuarios", totalUsuarios);
        estadisticas.put("totalReciclajes", totalReciclajes);
        estadisticas.put("totalEcoCoins", totalEcoCoins);

        long activos = usuarioRepository.findAll().stream()
                .filter(u -> "activo".equalsIgnoreCase(u.getEstado()))
                .count();

        long suspendidos = totalUsuarios - activos;
        estadisticas.put("usuariosActivos", activos);
        estadisticas.put("usuariosSuspendidos", suspendidos);

        List<Reciclaje> reciclajes = reciclajeRepository.findAll();
        Map<String, Long> materiales = reciclajes.stream()
                .collect(Collectors.groupingBy(Reciclaje::getTipoMaterial, Collectors.counting()));

        List<Map<String, Object>> topMateriales = materiales.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("tipoMaterial", e.getKey());
                    item.put("cantidad", e.getValue());
                    return item;
                })
                .collect(Collectors.toList());

        estadisticas.put("materialesTop", topMateriales);

        return estadisticas;
    }

    public List<Map<String, Object>> obtenerEcoCoinsPorUsuario() {
        return usuarioRepository.findAll().stream()
                .map(u -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("nombre", u.getNombre());
                    data.put("ecoCoins", u.getEcoCoins());
                    return data;
                })
                .sorted((a, b) -> ((Integer) b.get("ecoCoins")) - ((Integer) a.get("ecoCoins")))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> obtenerEcoCoinsPorMaterial() {
        List<Reciclaje> reciclajes = reciclajeRepository.findAll();

        Map<String, Long> materiales = reciclajes.stream()
                .collect(Collectors.groupingBy(Reciclaje::getTipoMaterial, Collectors.counting()));

        return materiales.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("tipoMaterial", e.getKey());
                    item.put("cantidad", e.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }
}
