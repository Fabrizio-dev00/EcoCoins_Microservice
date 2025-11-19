package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.Canje;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CanjeRepository extends MongoRepository<Canje, String> {

    /**
     * Buscar canjes por usuario
     */
    List<Canje> findByUsuarioId(String usuarioId);

    /**
     * Buscar canjes por recompensa
     */
    List<Canje> findByRecompensaId(String recompensaId);

    /**
     * Buscar canjes por estado
     */
    List<Canje> findByEstado(String estado);

    /**
     * Buscar canjes pendientes de un usuario
     */
    List<Canje> findByUsuarioIdAndEstado(String usuarioId, String estado);

    /**
     * Buscar canjes entre fechas
     */
    List<Canje> findByFechaCanjeBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Contar canjes por usuario
     */
    long countByUsuarioId(String usuarioId);

    /**
     * Contar canjes por estado
     */
    long countByEstado(String estado);

    /**
     * Buscar los últimos N canjes de un usuario
     */
    List<Canje> findTop10ByUsuarioIdOrderByFechaCanjeDesc(String usuarioId);

    /**
     * Buscar canjes recientes (últimas 24 horas)
     */
    @Query("{ 'fechaCanje': { $gte: ?0 } }")
    List<Canje> findRecientes(LocalDateTime desde);

    /**
     * Obtener canjes de hoy
     */
    @Query("{ 'fechaCanje': { $gte: ?0, $lt: ?1 } }")
    List<Canje> findCanjesDelDia(LocalDateTime inicio, LocalDateTime fin);
}