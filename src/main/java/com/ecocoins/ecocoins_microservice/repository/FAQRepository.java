package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.FAQ;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FAQRepository extends MongoRepository<FAQ, String> {
    List<FAQ> findByCategoria(String categoria);
    List<FAQ> findByActivoTrueOrderByOrdenAsc();
}
