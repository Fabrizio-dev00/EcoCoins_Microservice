package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.dto.CanjeRequest;
import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Canje;
import com.ecocoins.ecocoins_microservice.model.Recompensa;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.CanjeRepository;
import com.ecocoins.ecocoins_microservice.repository.RecompensaRepository;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import com.ecocoins.ecocoins_microservice.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CanjeService {

    private final CanjeRepository canjeRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecompensaRepository recompensaRepository;
    private final NotificacionService notificacionService;

    public CanjeService(
            CanjeRepository canjeRepository,
            UsuarioRepository usuarioRepository,
            RecompensaRepository recompensaRepository,
            NotificacionService notificacionService
    ) {
        this.canjeRepository = canjeRepository;
        this.usuarioRepository = usuarioRepository;
        this.recompensaRepository = recompensaRepository;
        this.notificacionService = notificacionService;
    }

    @PostConstruct
    public void inicializarDatosPrueba() {
        // Solo crear si no hay canjes
        if (canjeRepository.count() == 0) {
            System.out.println("⚙️ Inicializando datos de prueba para canjes...");

            // Buscar un usuario existente
            List<Usuario> usuarios = usuarioRepository.findAll();
            if (!usuarios.isEmpty()) {
                Usuario usuario = usuarios.get(0);

                // Buscar una recompensa existente
                List<Recompensa> recompensas = recompensaRepository.findAll();
                if (!recompensas.isEmpty()) {
                    Recompensa recompensa = recompensas.get(0);

                    // Crear canje de prueba
                    Canje canje = new Canje();
                    canje.setUsuarioId(usuario.getId());
                    canje.setUsuarioNombre(usuario.getNombre());
                    canje.setRecompensaId(recompensa.getId());
                    canje.setRecompensaNombre(recompensa.getNombre());
                    canje.setCostoEcoCoins(recompensa.getCostoEcoCoins());
                    canje.setEstado("completado");
                    canje.setDireccionEntrega("Campus universitario");
                    canje.setTelefonoContacto("555-1234");
                    canje.setFechaCanje(LocalDateTime.now().minusDays(2));
                    canje.setFechaEntrega(LocalDateTime.now().minusDays(1));

                    canjeRepository.save(canje);

                    System.out.println("✅ Colección 'canjes' creada con datos de prueba");
                }
            }
        }
    }

    /**
     * Canjear una recompensa
     */
    @Transactional
    public Map<String, Object> canjearRecompensa(CanjeRequest request) {
        // Validaciones
        ValidationUtil.validarMongoId(request.getUsuarioId(), "ID de usuario");
        ValidationUtil.validarMongoId(request.getRecompensaId(), "ID de recompensa");

        // 1. Obtener usuario
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getUsuarioId()));

        // 2. Obtener recompensa
        Recompensa recompensa = recompensaRepository.findById(request.getRecompensaId())
                .orElseThrow(() -> new ResourceNotFoundException("Recompensa", "id", request.getRecompensaId()));

        // 3. Verificar stock
        if (recompensa.getStock() <= 0) {
            throw new BadRequestException("La recompensa no tiene stock disponible");
        }

        // 4. Verificar EcoCoins suficientes
        if (usuario.getEcoCoins() < recompensa.getCostoEcoCoins()) {
            throw new BadRequestException(
                    String.format("EcoCoins insuficientes. Tienes %d pero necesitas %d",
                            usuario.getEcoCoins(), recompensa.getCostoEcoCoins())
            );
        }

        // 5. Crear el canje
        Canje canje = new Canje();
        canje.setUsuarioId(usuario.getId());
        canje.setUsuarioNombre(usuario.getNombre());
        canje.setRecompensaId(recompensa.getId());
        canje.setRecompensaNombre(recompensa.getNombre());
        canje.setCostoEcoCoins(recompensa.getCostoEcoCoins());
        canje.setEstado("pendiente");
        canje.setDireccionEntrega(request.getDireccionEntrega());
        canje.setTelefonoContacto(request.getTelefonoContacto());
        canje.setObservaciones(request.getObservaciones());
        canje.setFechaCanje(LocalDateTime.now());

        // 6. Guardar canje
        Canje savedCanje = canjeRepository.save(canje);

        // 7. Descontar EcoCoins del usuario
        usuario.setEcoCoins(usuario.getEcoCoins() - recompensa.getCostoEcoCoins());
        usuarioRepository.save(usuario);

        // 8. Reducir stock de la recompensa
        recompensa.setStock(recompensa.getStock() - 1);
        recompensaRepository.save(recompensa);

        // 9. Enviar notificación al usuario
        notificacionService.enviarNotificacion(
                usuario.getId(),
                "🎁 ¡Canje exitoso!",
                String.format("Has canjeado '%s' por %d EcoCoins. Te contactaremos pronto.",
                        recompensa.getNombre(), recompensa.getCostoEcoCoins()),
                "success"
        );

        // 10. Preparar respuesta
        return Map.of(
                "mensaje", "✅ ¡Canje realizado exitosamente!",
                "canjeId", savedCanje.getId(),
                "recompensa", recompensa.getNombre(),
                "costoEcoCoins", recompensa.getCostoEcoCoins(),
                "nuevoBalance", usuario.getEcoCoins(),
                "estado", "pendiente",
                "fechaCanje", savedCanje.getFechaCanje().toString()
        );
    }

    /**
     * Listar canjes de un usuario
     */
    public List<Canje> listarCanjesPorUsuario(String usuarioId) {
        ValidationUtil.validarMongoId(usuarioId, "ID de usuario");
        return canjeRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Obtener un canje por ID
     */
    public Canje obtenerCanjePorId(String canjeId) {
        return canjeRepository.findById(canjeId)
                .orElseThrow(() -> new ResourceNotFoundException("Canje", "id", canjeId));
    }

    /**
     * Listar todos los canjes
     */
    public List<Canje> listarTodosCanjes() {
        return canjeRepository.findAll();
    }

    /**
     * Cambiar estado de un canje
     */
    public Canje cambiarEstadoCanje(String canjeId, String nuevoEstado) {
        Canje canje = obtenerCanjePorId(canjeId);

        // Validar estado
        if (!List.of("pendiente", "completado", "cancelado", "entregado").contains(nuevoEstado)) {
            throw new BadRequestException("Estado inválido: " + nuevoEstado);
        }

        canje.setEstado(nuevoEstado);

        if ("entregado".equals(nuevoEstado)) {
            canje.setFechaEntrega(LocalDateTime.now());
        }

        Canje updated = canjeRepository.save(canje);

        // Notificar al usuario
        notificacionService.enviarNotificacion(
                canje.getUsuarioId(),
                "📦 Actualización de tu canje",
                String.format("El estado de tu canje '%s' ha cambiado a: %s",
                        canje.getRecompensaNombre(), nuevoEstado),
                "info"
        );

        return updated;
    }

    /**
     * Cancelar un canje (solo si está pendiente)
     */
    @Transactional
    public Map<String, Object> cancelarCanje(String canjeId, String usuarioId) {
        Canje canje = obtenerCanjePorId(canjeId);

        // Verificar que sea del usuario
        if (!canje.getUsuarioId().equals(usuarioId)) {
            throw new BadRequestException("No puedes cancelar un canje que no es tuyo");
        }

        // Solo se puede cancelar si está pendiente
        if (!"pendiente".equals(canje.getEstado())) {
            throw new BadRequestException("Solo puedes cancelar canjes en estado 'pendiente'");
        }

        // Cambiar estado
        canje.setEstado("cancelado");
        canjeRepository.save(canje);

        // Devolver EcoCoins al usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        usuario.setEcoCoins(usuario.getEcoCoins() + canje.getCostoEcoCoins());
        usuarioRepository.save(usuario);

        // Devolver stock a la recompensa
        Recompensa recompensa = recompensaRepository.findById(canje.getRecompensaId())
                .orElseThrow(() -> new ResourceNotFoundException("Recompensa", "id", canje.getRecompensaId()));

        recompensa.setStock(recompensa.getStock() + 1);
        recompensaRepository.save(recompensa);

        return Map.of(
                "mensaje", "Canje cancelado exitosamente",
                "ecoCoinsDevueltos", canje.getCostoEcoCoins(),
                "nuevoBalance", usuario.getEcoCoins()
        );
    }
}