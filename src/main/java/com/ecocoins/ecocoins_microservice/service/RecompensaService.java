package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.model.Recompensa;
import com.ecocoins.ecocoins_microservice.repository.RecompensaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecompensaService {

    @Autowired
    private RecompensaRepository recompensaRepository;

    public List<Recompensa> listarRecompensas() {
        return recompensaRepository.findAll();
    }

    public Optional<Recompensa> obtenerPorId(String id) {
        return recompensaRepository.findById(id);
    }

    public Recompensa crearRecompensa(Recompensa recompensa) {
        return recompensaRepository.save(recompensa);
    }

    public Optional<Recompensa> actualizarRecompensa(String id, Recompensa recompensaActualizada) {
        return recompensaRepository.findById(id).map(recompensa -> {
            recompensa.setNombre(recompensaActualizada.getNombre());
            recompensa.setDescripcion(recompensaActualizada.getDescripcion());
            recompensa.setCostoEcoCoins(recompensaActualizada.getCostoEcoCoins());
            recompensa.setStock(recompensaActualizada.getStock());
            return recompensaRepository.save(recompensa);
        });
    }

    public boolean eliminarRecompensa(String id) {
        if (recompensaRepository.existsById(id)) {
            recompensaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean canjearRecompensa(String id) {
        Optional<Recompensa> opt = recompensaRepository.findById(id);
        if (opt.isPresent()) {
            Recompensa r = opt.get();
            if (r.getStock() > 0) {
                r.setStock(r.getStock() - 1);
                recompensaRepository.save(r);
                return true;
            }
        }
        return false;
    }
}
