package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.Reciclaje;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReciclajeRepository extends MongoRepository<Reciclaje, String> {
    List<Reciclaje> findByUsuarioId(String usuarioId);
    List<Reciclaje> findByUsuarioIdAndFechaGreaterThanEqual(String usuarioId, LocalDateTime fecha);
}
