package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.Profesor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfesorRepository extends MongoRepository<Profesor, String> {

    /**
     * Buscar profesores activos
     */
    List<Profesor> findByActivo(boolean activo);

    /**
     * Buscar por especialidad
     */
    List<Profesor> findByEspecialidadContainingIgnoreCase(String especialidad);

    /**
     * Buscar por correo
     */
    Optional<Profesor> findByCorreo(String correo);

    /**
     * Buscar por nombre o apellido
     */
    @Query("{ $or: [ { 'nombre': { $regex: ?0, $options: 'i' } }, { 'apellido': { $regex: ?0, $options: 'i' } } ] }")
    List<Profesor> findByNombreOrApellidoContaining(String busqueda);

    /**
     * Buscar profesores activos por especialidad
     */
    @Query("{ 'activo': true, 'especialidad': { $regex: ?0, $options: 'i' } }")
    List<Profesor> findActivosByEspecialidad(String especialidad);

    /**
     * Buscar los mejores calificados (rating >= 4.0)
     */
    @Query("{ 'activo': true, 'rating': { $gte: 4.0 } }")
    List<Profesor> findTopRated();

    /**
     * Contar profesores activos
     */
    long countByActivo(boolean activo);

    /**
     * Buscar profesores por rango de rating
     */
    @Query("{ 'activo': true, 'rating': { $gte: ?0, $lte: ?1 } }")
    List<Profesor> findByRatingBetween(double minRating, double maxRating);
}