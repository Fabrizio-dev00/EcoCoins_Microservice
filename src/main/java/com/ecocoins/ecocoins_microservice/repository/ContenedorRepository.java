package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.Contenedor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContenedorRepository extends MongoRepository<Contenedor, String> {

    /**
     * Buscar contenedor por código QR
     */
    Optional<Contenedor> findByCodigo(String codigo);

    /**
     * Verificar si existe un contenedor con ese código
     */
    boolean existsByCodigo(String codigo);

    /**
     * Buscar contenedores por tipo de material
     */
    List<Contenedor> findByTipoMaterial(String tipoMaterial);

    /**
     * Buscar contenedores por estado
     */
    List<Contenedor> findByEstado(String estado);

    /**
     * Buscar contenedores por punto de recolección
     */
    List<Contenedor> findByPuntoRecoleccionId(String puntoRecoleccionId);

    /**
     * Buscar contenedores activos
     */
    @Query("{ 'estado': 'activo' }")
    List<Contenedor> findActivos();

    /**
     * Buscar contenedores llenos (capacidad >= 90%)
     */
    @Query("{ $expr: { $gte: [ '$capacidadActualKg', { $multiply: [ '$capacidadMaxKg', 0.9 ] } ] } }")
    List<Contenedor> findLlenos();

    /**
     * Buscar contenedores que necesitan mantenimiento
     * (más de 30 días sin mantenimiento)
     */
    @Query("{ 'ultimoMantenimiento': { $lt: ?0 } }")
    List<Contenedor> findNecesitanMantenimiento(java.time.LocalDateTime fecha);

    /**
     * Buscar contenedores por ubicación (búsqueda de texto)
     */
    List<Contenedor> findByUbicacionContainingIgnoreCase(String ubicacion);

    /**
     * Contar contenedores por estado
     */
    long countByEstado(String estado);

    /**
     * Contar contenedores por tipo de material
     */
    long countByTipoMaterial(String tipoMaterial);
}