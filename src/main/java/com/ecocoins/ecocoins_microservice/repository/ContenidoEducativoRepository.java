package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.ContenidoEducativo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContenidoEducativoRepository extends MongoRepository<ContenidoEducativo, String> {
    List<ContenidoEducativo> findByCategoria(String categoria);
    List<ContenidoEducativo> findByTipo(String tipo);
    List<ContenidoEducativo> findByActivoTrue();
}
