package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.Notificacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacionRepository extends MongoRepository<Notificacion, String> {

    /**
     * Buscar notificaciones por usuario
     */
    List<Notificacion> findByUsuarioId(String usuarioId);

    /**
     * Buscar notificaciones no leídas de un usuario
     */
    List<Notificacion> findByUsuarioIdAndLeida(String usuarioId, boolean leida);

    /**
     * Buscar notificaciones por tipo
     */
    List<Notificacion> findByTipo(String tipo);

    /**
     * Buscar notificaciones globales (sin usuario específico)
     */
    List<Notificacion> findByUsuarioIdIsNull();

    /**
     * Contar notificaciones no leídas de un usuario
     */
    long countByUsuarioIdAndLeida(String usuarioId, boolean leida);

    /**
     * Buscar notificaciones recientes de un usuario (últimas 30)
     */
    List<Notificacion> findTop30ByUsuarioIdOrderByFechaCreacionDesc(String usuarioId);

    /**
     * Buscar notificaciones entre fechas
     */
    List<Notificacion> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Buscar notificaciones de un usuario o globales
     */
    @Query("{ $or: [ { 'usuarioId': ?0 }, { 'usuarioId': null } ] }")
    List<Notificacion> findByUsuarioIdOrGlobal(String usuarioId);

    /**
     * Buscar notificaciones no leídas de un usuario o globales
     */
    @Query("{ $and: [ { $or: [ { 'usuarioId': ?0 }, { 'usuarioId': null } ] }, { 'leida': false } ] }")
    List<Notificacion> findNoLeidasByUsuarioIdOrGlobal(String usuarioId);

    /**
     * Eliminar notificaciones antiguas (más de 90 días)
     */
    @Query(value = "{ 'fechaCreacion': { $lt: ?0 } }", delete = true)
    void deleteOlderThan(LocalDateTime fecha);
}