package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByUsuarioId(String usuarioId);
    List<Ticket> findByEstado(String estado);
    List<Ticket> findByCategoria(String categoria);
}
