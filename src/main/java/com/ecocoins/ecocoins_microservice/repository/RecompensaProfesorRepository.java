package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.RecompensaProfesor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RecompensaProfesorRepository extends MongoRepository<RecompensaProfesor, String> {

    /**
     * Buscar recompensas de un profesor específico
     */
    List<RecompensaProfesor> findByProfesorId(String profesorId);

    /**
     * Buscar recompensas activas de un profesor
     */
    List<RecompensaProfesor> findByProfesorIdAndActivo(String profesorId, boolean activo);

    /**
     * Buscar por tipo de servicio
     */
    List<RecompensaProfesor> findByTipo(String tipo);

    /**
     * Buscar recompensas activas por tipo
     */
    @Query("{ 'activo': true, 'tipo': ?0 }")
    List<RecompensaProfesor> findActivasByTipo(String tipo);

    /**
     * Buscar recompensas con stock disponible
     */
    @Query("{ 'activo': true, 'stockDisponible': { $gt: 0 } }")
    List<RecompensaProfesor> findConStockDisponible();

    /**
     * Buscar recompensas por rango de precio
     */
    @Query("{ 'activo': true, 'costoEcoCoins': { $gte: ?0, $lte: ?1 } }")
    List<RecompensaProfesor> findByPrecioEntre(int minPrecio, int maxPrecio);

    /**
     * Buscar recompensas activas de un profesor con stock
     */
    @Query("{ 'profesorId': ?0, 'activo': true, 'stockDisponible': { $gt: 0 } }")
    List<RecompensaProfesor> findDisponiblesByProfesor(String profesorId);

    /**
     * Buscar por modalidad
     */
    List<RecompensaProfesor> findByModalidadAndActivo(String modalidad, boolean activo);
}