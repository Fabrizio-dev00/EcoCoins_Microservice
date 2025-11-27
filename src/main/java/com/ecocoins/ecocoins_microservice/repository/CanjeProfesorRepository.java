package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.CanjeProfesor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CanjeProfesorRepository extends MongoRepository<CanjeProfesor, String> {

    /**
     * Buscar canjes por usuario
     */
    List<CanjeProfesor> findByUsuarioId(String usuarioId);

    /**
     * Buscar canjes por profesor
     */
    List<CanjeProfesor> findByProfesorId(String profesorId);

    /**
     * Buscar canjes por estado
     */
    List<CanjeProfesor> findByEstado(String estado);

    /**
     * Buscar canjes de un usuario por estado
     */
    List<CanjeProfesor> findByUsuarioIdAndEstado(String usuarioId, String estado);

    /**
     * Buscar canjes de un profesor por estado
     */
    List<CanjeProfesor> findByProfesorIdAndEstado(String profesorId, String estado);

    /**
     * Buscar canjes pendientes de un profesor
     */
    @Query("{ 'profesorId': ?0, 'estado': 'PENDIENTE' }")
    List<CanjeProfesor> findPendientesByProfesor(String profesorId);

    /**
     * Buscar canjes activos de un usuario (PENDIENTE o ACEPTADO)
     */
    @Query("{ 'usuarioId': ?0, 'estado': { $in: ['PENDIENTE', 'ACEPTADO'] } }")
    List<CanjeProfesor> findActivosByUsuario(String usuarioId);

    /**
     * Contar canjes por usuario
     */
    long countByUsuarioId(String usuarioId);

    /**
     * Contar canjes completados de un profesor
     */
    @Query("{ 'profesorId': ?0, 'estado': 'COMPLETADO' }")
    long countCompletadosByProfesor(String profesorId);

    /**
     * Buscar últimos canjes de un usuario
     */
    List<CanjeProfesor> findTop10ByUsuarioIdOrderByFechaCanjeDesc(String usuarioId);

    /**
     * Buscar canjes entre fechas
     */
    List<CanjeProfesor> findByFechaCanjeBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Buscar canjes por tipo de servicio
     */
    List<CanjeProfesor> findByTipoServicio(String tipoServicio);
}