package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.ecocoins.ecocoins_microservice.exception.ConflictException;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Contenedor;
import com.ecocoins.ecocoins_microservice.repository.ContenedorRepository;
import com.ecocoins.ecocoins_microservice.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    public ContenedorService(ContenedorRepository contenedorRepository) {
        this.contenedorRepository = contenedorRepository;
    }

    /**
     * Listar todos los contenedores
     */
    public List<Contenedor> listarTodos() {
        return contenedorRepository.findAll();
    }

    /**
     * Listar contenedores activos
     */
    public List<Contenedor> listarActivos() {
        return contenedorRepository.findActivos();
    }

    /**
     * Obtener contenedor por ID
     */
    public Contenedor obtenerPorId(String id) {
        return contenedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contenedor", "id", id));
    }

    /**
     * Obtener contenedor por código
     */
    public Contenedor obtenerPorCodigo(String codigo) {
        return contenedorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Contenedor", "codigo", codigo));
    }

    /**
     * Crear nuevo contenedor
     */
    public Contenedor crear(Contenedor contenedor) {
        // Validaciones
        ValidationUtil.validarNoVacio(contenedor.getCodigo(), "código");
        ValidationUtil.validarNoVacio(contenedor.getTipoMaterial(), "tipo de material");
        ValidationUtil.validarNoVacio(contenedor.getUbicacion(), "ubicación");

        if (contenedor.getCapacidadMaxKg() <= 0) {
            throw new BadRequestException("La capacidad máxima debe ser mayor a 0");
        }

        // Verificar que el código no exista
        if (contenedorRepository.existsByCodigo(contenedor.getCodigo())) {
            throw new ConflictException("Contenedor", "código", contenedor.getCodigo());
        }

        // Inicializar valores
        contenedor.setEstado("activo");
        contenedor.setCapacidadActualKg(0.0);
        contenedor.setFechaInstalacion(LocalDateTime.now());

        return contenedorRepository.save(contenedor);
    }

    /**
     * Actualizar contenedor
     */
    public Contenedor actualizar(String id, Contenedor contenedorActualizado) {
        Contenedor contenedor = obtenerPorId(id);

        // Actualizar campos
        if (contenedorActualizado.getTipoMaterial() != null) {
            contenedor.setTipoMaterial(contenedorActualizado.getTipoMaterial());
        }
        if (contenedorActualizado.getUbicacion() != null) {
            contenedor.setUbicacion(contenedorActualizado.getUbicacion());
        }
        if (contenedorActualizado.getEstado() != null) {
            contenedor.setEstado(contenedorActualizado.getEstado());
        }
        if (contenedorActualizado.getCapacidadMaxKg() > 0) {
            contenedor.setCapacidadMaxKg(contenedorActualizado.getCapacidadMaxKg());
        }
        if (contenedorActualizado.getPuntoRecoleccionId() != null) {
            contenedor.setPuntoRecoleccionId(contenedorActualizado.getPuntoRecoleccionId());
        }

        return contenedorRepository.save(contenedor);
    }

    /**
     * Vaciar contenedor (resetear capacidad actual)
     */
    public Contenedor vaciar(String id) {
        Contenedor contenedor = obtenerPorId(id);

        contenedor.setCapacidadActualKg(0.0);
        contenedor.setUltimaRecogida(LocalDateTime.now());
        contenedor.setEstado("activo");

        return contenedorRepository.save(contenedor);
    }

    /**
     * Cambiar estado del contenedor
     */
    public Contenedor cambiarEstado(String id, String nuevoEstado) {
        Contenedor contenedor = obtenerPorId(id);

        // Validar estado
        if (!List.of("activo", "mantenimiento", "inactivo", "lleno").contains(nuevoEstado)) {
            throw new BadRequestException("Estado inválido: " + nuevoEstado);
        }

        contenedor.setEstado(nuevoEstado);

        if ("mantenimiento".equals(nuevoEstado)) {
            contenedor.setUltimoMantenimiento(LocalDateTime.now());
        }

        return contenedorRepository.save(contenedor);
    }

    /**
     * Eliminar contenedor
     */
    public void eliminar(String id) {
        if (!contenedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contenedor", "id", id);
        }
        contenedorRepository.deleteById(id);
    }

    /**
     * Listar contenedores llenos
     */
    public List<Contenedor> listarLlenos() {
        return contenedorRepository.findLlenos();
    }

    /**
     * Listar contenedores por tipo de material
     */
    public List<Contenedor> listarPorTipoMaterial(String tipoMaterial) {
        return contenedorRepository.findByTipoMaterial(tipoMaterial);
    }

    /**
     * Obtener estadísticas de contenedores
     */
    public Map<String, Object> obtenerEstadisticas() {
        long total = contenedorRepository.count();
        long activos = contenedorRepository.countByEstado("activo");
        long llenos = contenedorRepository.findLlenos().size();
        long mantenimiento = contenedorRepository.countByEstado("mantenimiento");

        return Map.of(
                "totalContenedores", total,
                "activos", activos,
                "llenos", llenos,
                "enMantenimiento", mantenimiento,
                "inactivos", total - activos - llenos - mantenimiento
        );
    }
}