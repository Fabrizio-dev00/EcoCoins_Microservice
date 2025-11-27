package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.dto.CanjeProfesorRequest;
import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.*;
import com.ecocoins.ecocoins_microservice.repository.*;
import com.ecocoins.ecocoins_microservice.util.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final RecompensaProfesorRepository recompensaProfesorRepository;
    private final CanjeProfesorRepository canjeProfesorRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public ProfesorService(
            ProfesorRepository profesorRepository,
            RecompensaProfesorRepository recompensaProfesorRepository,
            CanjeProfesorRepository canjeProfesorRepository,
            UsuarioRepository usuarioRepository,
            NotificacionService notificacionService
    ) {
        this.profesorRepository = profesorRepository;
        this.recompensaProfesorRepository = recompensaProfesorRepository;
        this.canjeProfesorRepository = canjeProfesorRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    /**
     * Listar profesores activos
     */
    public List<Profesor> listarProfesoresActivos() {
        return profesorRepository.findByActivo(true);
    }

    /**
     * Obtener profesor por ID
     */
    public Profesor obtenerPorId(String id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", id));
    }

    /**
     * Buscar profesores por especialidad
     */
    public List<Profesor> buscarPorEspecialidad(String especialidad) {
        return profesorRepository.findActivosByEspecialidad(especialidad);
    }

    /**
     * Listar recompensas (servicios) de un profesor
     */
    public List<RecompensaProfesor> listarRecompensasProfesor(String profesorId) {
        // Verificar que el profesor exista
        obtenerPorId(profesorId);
        return recompensaProfesorRepository.findDisponiblesByProfesor(profesorId);
    }

    /**
     * Canjear un servicio de profesor
     */
    @Transactional
    public Map<String, Object> canjearRecompensaProfesor(CanjeProfesorRequest request) {
        // Validaciones
        ValidationUtil.validarMongoId(request.getUsuarioId(), "ID de usuario");
        ValidationUtil.validarMongoId(request.getRecompensaProfesorId(), "ID de recompensa");

        // 1. Obtener usuario
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getUsuarioId()));

        // 2. Obtener recompensa del profesor
        RecompensaProfesor recompensa = recompensaProfesorRepository.findById(request.getRecompensaProfesorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recompensa de Profesor", "id", request.getRecompensaProfesorId()));

        // 3. Verificar que esté activa
        if (!recompensa.isActivo()) {
            throw new BadRequestException("Esta recompensa no está disponible");
        }

        // 4. Verificar stock
        if (recompensa.getStockDisponible() <= 0) {
            throw new BadRequestException("No hay cupos disponibles para este servicio");
        }

        // 5. Verificar EcoCoins suficientes
        if (usuario.getEcoCoins() < recompensa.getCostoEcoCoins()) {
            throw new BadRequestException(
                    String.format("EcoCoins insuficientes. Tienes %d pero necesitas %d",
                            usuario.getEcoCoins(), recompensa.getCostoEcoCoins())
            );
        }

        // 6. Obtener profesor
        Profesor profesor = obtenerPorId(recompensa.getProfesorId());

        // 7. Crear el canje
        CanjeProfesor canje = new CanjeProfesor();
        canje.setUsuarioId(usuario.getId());
        canje.setUsuarioNombre(usuario.getNombre());
        canje.setProfesorId(profesor.getId());
        canje.setProfesorNombre(profesor.getNombreCompleto());
        canje.setRecompensaId(recompensa.getId());
        canje.setRecompensaTitulo(recompensa.getTitulo());
        canje.setTipoServicio(recompensa.getTipo());
        canje.setCostoEcoCoins(recompensa.getCostoEcoCoins());
        canje.setEstado("PENDIENTE");
        canje.setMensaje(request.getMensaje());
        canje.setFechaPreferida(request.getFechaPreferida());
        canje.setHorarioPreferido(request.getHorarioPreferido());
        canje.setModalidad(recompensa.getModalidad());
        canje.setFechaCanje(LocalDateTime.now());

        // 8. Guardar canje
        CanjeProfesor savedCanje = canjeProfesorRepository.save(canje);

        // 9. Descontar EcoCoins del usuario
        usuario.setEcoCoins(usuario.getEcoCoins() - recompensa.getCostoEcoCoins());
        usuarioRepository.save(usuario);

        // 10. Reducir stock
        recompensa.setStockDisponible(recompensa.getStockDisponible() - 1);
        recompensaProfesorRepository.save(recompensa);

        // 11. Incrementar contador del profesor
        profesor.setTotalRecompensas(profesor.getTotalRecompensas() + 1);
        profesorRepository.save(profesor);

        // 12. Enviar notificación al estudiante
        notificacionService.enviarNotificacion(
                usuario.getId(),
                "🎓 Solicitud enviada",
                String.format("Tu solicitud de '%s' con %s ha sido enviada. El profesor te contactará pronto.",
                        recompensa.getTitulo(), profesor.getNombreCompleto()),
                "success"
        );

        // 13. Preparar respuesta
        return Map.of(
                "mensaje", "✅ Solicitud enviada exitosamente",
                "canjeId", savedCanje.getId(),
                "profesor", profesor.getNombreCompleto(),
                "servicio", recompensa.getTitulo(),
                "costoEcoCoins", recompensa.getCostoEcoCoins(),
                "nuevoBalance", usuario.getEcoCoins(),
                "estado", "PENDIENTE",
                "fechaCanje", savedCanje.getFechaCanje().toString()
        );
    }

    /**
     * Listar canjes de un usuario con profesores
     */
    public List<CanjeProfesor> listarCanjesUsuario(String usuarioId) {
        ValidationUtil.validarMongoId(usuarioId, "ID de usuario");
        return canjeProfesorRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Obtener detalle de un canje con profesor
     */
    public CanjeProfesor obtenerCanjePorId(String canjeId) {
        return canjeProfesorRepository.findById(canjeId)
                .orElseThrow(() -> new ResourceNotFoundException("Canje de Profesor", "id", canjeId));
    }

    /**
     * Listar todos los profesores (admin)
     */
    public List<Profesor> listarTodos() {
        return profesorRepository.findAll();
    }

    /**
     * Crear profesor (admin)
     */
    public Profesor crearProfesor(Profesor profesor) {
        ValidationUtil.validarNombre(profesor.getNombre());
        ValidationUtil.validarCorreoInstitucional(profesor.getCorreo());

        // Verificar si ya existe
        if (profesorRepository.findByCorreo(profesor.getCorreo()).isPresent()) {
            throw new BadRequestException("Ya existe un profesor con ese correo");
        }

        profesor.setActivo(true);
        profesor.setRating(0.0);
        profesor.setTotalResenias(0);
        profesor.setTotalRecompensas(0);
        profesor.setFechaRegistro(LocalDateTime.now());

        return profesorRepository.save(profesor);
    }

    /**
     * Listar los mejores profesores (rating >= 4.0)
     */
    public List<Profesor> listarMejorCalificados() {
        return profesorRepository.findTopRated();
    }
}