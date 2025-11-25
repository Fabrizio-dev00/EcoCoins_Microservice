package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.model.Reciclaje;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.ReciclajeRepository;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReciclajeService {

    private final ReciclajeRepository reciclajeRepository;
    private final UsuarioRepository usuarioRepository;

    // Tarifas por material (EcoCoins por kg)
    private static final Map<String, Integer> TARIFAS = Map.of(
            "Plástico", 5,
            "Papel", 3,
            "Vidrio", 7,
            "Metal", 10,
            "Cartón", 4,
            "Electrónico", 15,
            "Orgánico", 2,
            "Pilas", 20
    );

    public ReciclajeService(ReciclajeRepository reciclajeRepository, UsuarioRepository usuarioRepository) {
        this.reciclajeRepository = reciclajeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Reciclaje> listarReciclajes() {
        return reciclajeRepository.findAll();
    }

    public List<Reciclaje> listarPorUsuario(String usuarioId) {
        return reciclajeRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public Reciclaje registrarReciclaje(Reciclaje reciclaje) {
        // Calcular EcoCoins según el material y peso
        int tarifa = TARIFAS.getOrDefault(reciclaje.getTipoMaterial(), 5);
        int ecoCoins = (int) (reciclaje.getPesoKg() * tarifa);
        reciclaje.setEcoCoinsGanadas(ecoCoins);
        reciclaje.setFecha(LocalDateTime.now());

        // Guardar reciclaje
        Reciclaje saved = reciclajeRepository.save(reciclaje);

        // Actualizar EcoCoins del usuario
        Usuario usuario = usuarioRepository.findById(reciclaje.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", reciclaje.getUsuarioId()));

        usuario.setEcoCoins(usuario.getEcoCoins() + ecoCoins);
        usuario.setTotalReciclajes(usuario.getTotalReciclajes() + 1);
        usuario.setTotalKgReciclados(usuario.getTotalKgReciclados() + reciclaje.getPesoKg());
        usuarioRepository.save(usuario);

        return saved;
    }

    public void eliminarReciclaje(String id) {
        reciclajeRepository.deleteById(id);
    }

    public Reciclaje obtenerPorId(String id) {
        return reciclajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reciclaje", "id", id));
    }
}