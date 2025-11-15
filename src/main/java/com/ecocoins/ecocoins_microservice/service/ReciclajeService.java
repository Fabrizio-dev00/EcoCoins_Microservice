package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.model.Reciclaje;
import com.ecocoins.ecocoins_microservice.repository.ReciclajeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReciclajeService {

    private final ReciclajeRepository reciclajeRepository;

    public ReciclajeService(ReciclajeRepository reciclajeRepository) {
        this.reciclajeRepository = reciclajeRepository;
    }

    public List<Reciclaje> listarReciclajes() {
        return reciclajeRepository.findAll();
    }

    public List<Reciclaje> listarPorUsuario(String usuarioId) {
        return reciclajeRepository.findByUsuarioId(usuarioId);
    }

    public Reciclaje registrarReciclaje(Reciclaje reciclaje) {
        return reciclajeRepository.save(reciclaje);
    }

    public void eliminarReciclaje(String id) {
        reciclajeRepository.deleteById(id);
    }
}
