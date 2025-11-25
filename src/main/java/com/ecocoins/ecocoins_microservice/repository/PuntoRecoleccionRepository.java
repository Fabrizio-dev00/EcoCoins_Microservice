package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.PuntoRecoleccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PuntoRecoleccionRepository extends MongoRepository<PuntoRecoleccion, String> {

    /**
     * Buscar puntos de recolección activos
     */
    List<PuntoRecoleccion> findByActivo(boolean activo);

    /**
     * Buscar por nombre (parcial, sin importar mayúsculas)
     */
    List<PuntoRecoleccion> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar por ubicación (parcial, sin importar mayúsculas)
     */
    List<PuntoRecoleccion> findByUbicacionContainingIgnoreCase(String ubicacion);

    /**
     * Buscar puntos que acepten un tipo de material específico
     */
    @Query("{ 'tiposMaterialesAceptados': ?0 }")
    List<PuntoRecoleccion> findByTipoMaterialAceptado(String tipoMaterial);

    /**
     * Buscar puntos activos que acepten un material específico
     */
    @Query("{ 'activo': true, 'tiposMaterialesAceptados': ?0 }")
    List<PuntoRecoleccion> findActivosByTipoMaterial(String tipoMaterial);

    /**
     * Buscar puntos cercanos (requiere índice geoespacial)
     * Nota: Para implementar búsqueda por cercanía necesitas índice 2dsphere
     */
    @Query("{ 'latitud': { $gte: ?0, $lte: ?1 }, 'longitud': { $gte: ?2, $lte: ?3 }, 'activo': true }")
    List<PuntoRecoleccion> findByCoordenadasCercanas(
            double minLat, double maxLat,
            double minLon, double maxLon
    );

    /**
     * Contar puntos activos
     */
    long countByActivo(boolean activo);

    /**
     * Buscar por responsable
     */
    List<PuntoRecoleccion> findByResponsableContainingIgnoreCase(String responsable);
}