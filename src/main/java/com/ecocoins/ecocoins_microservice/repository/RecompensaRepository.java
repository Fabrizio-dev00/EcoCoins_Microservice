package com.ecocoins.ecocoins_microservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ecocoins.ecocoins_microservice.model.Recompensa;

import java.util.List;


public interface RecompensaRepository extends MongoRepository<Recompensa, String> {
    List<Recompensa> findByNombreContainingIgnoreCase(String nombre);
    List<Recompensa> findByCostoEcoCoinsLessThanEqual(int costo);
    List<Recompensa> findByStockGreaterThan(int stock);
}
