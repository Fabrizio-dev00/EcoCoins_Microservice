package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SoporteService {

    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    // Mock data para FAQs
    private final List<Map<String, Object>> FAQS_MOCK = crearFAQsMock();

    // Mock data para Tickets (en producción usar MongoDB)
    private final List<Map<String, Object>> TICKETS_MOCK = crearTicketsMock();
    private int ticketIdCounter = 4;

    public SoporteService(UsuarioRepository usuarioRepository,
                          NotificacionService notificacionService) {
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    // ========== FAQs ==========

    private List<Map<String, Object>> crearFAQsMock() {
        List<Map<String, Object>> faqs = new ArrayList<>();

        Map<String, Object> f1 = new HashMap<>();
        f1.put("id", "F001");
        f1.put("pregunta", "¿Cómo gano EcoCoins?");
        f1.put("respuesta", "Ganas EcoCoins reciclando materiales en los puntos habilitados. Cada tipo de material tiene una tarifa específica por kilogramo. También puedes ganar EcoCoins completando contenido educativo y participando en actividades de la comunidad.");
        f1.put("categoria", "ECOCOINS");
        f1.put("util", 45);
        faqs.add(f1);

        Map<String, Object> f2 = new HashMap<>();
        f2.put("id", "F002");
        f2.put("pregunta", "¿Qué materiales puedo reciclar?");
        f2.put("respuesta", "Puedes reciclar: Plástico (botellas PET, envases), Papel y cartón, Vidrio (botellas, frascos), Metal (latas de aluminio), Electrónicos (con autorización), y Pilas especiales. Asegúrate de que estén limpios y secos.");
        f2.put("categoria", "RECICLAJE");
        f2.put("util", 38);
        faqs.add(f2);

        Map<String, Object> f3 = new HashMap<>();
        f3.put("id", "F003");
        f3.put("pregunta", "¿Cómo canjeo mis EcoCoins?");
        f3.put("respuesta", "Ve a la sección 'Tienda' en la app, selecciona la recompensa que desees y confirma el canje. Recibirás una notificación cuando tu recompensa esté lista para recoger. El código QR de tu canje estará disponible en 'Mis Canjes'.");
        f3.put("categoria", "CANJES");
        f3.put("util", 52);
        faqs.add(f3);

        Map<String, Object> f4 = new HashMap<>();
        f4.put("id", "F004");
        f4.put("pregunta", "¿Cuánto tiempo tarda en aprobarse un canje?");
        f4.put("respuesta", "Los canjes se aprueban en un plazo de 24-48 horas hábiles. Recibirás una notificación cuando tu canje cambie de estado. Si es urgente, contacta a soporte con tu código de canje.");
        f4.put("categoria", "CANJES");
        f4.put("util", 29);
        faqs.add(f4);

        Map<String, Object> f5 = new HashMap<>();
        f5.put("id", "F005");
        f5.put("pregunta", "¿Cómo cambio mi contraseña?");
        f5.put("respuesta", "Ve a Perfil > Configuración > Cuenta > Cambiar contraseña. Necesitarás tu contraseña actual y la nueva contraseña. La nueva contraseña debe tener al menos 6 caracteres.");
        f5.put("categoria", "CUENTA");
        f5.put("util", 15);
        faqs.add(f5);

        Map<String, Object> f6 = new HashMap<>();
        f6.put("id", "F006");
        f6.put("pregunta", "¿Qué hago si el scanner no funciona?");
        f6.put("respuesta", "Verifica que: 1) Tienes permisos de cámara activados, 2) Hay buena iluminación, 3) El código QR está limpio y completo. Si el problema persiste, reinicia la app o reporta el contenedor dañado.");
        f6.put("categoria", "GENERAL");
        f6.put("util", 33);
        faqs.add(f6);

        return faqs;
    }

    public List<Map<String, Object>> obtenerFAQs(String categoria) {
        List<Map<String, Object>> faqs = new ArrayList<>(FAQS_MOCK);

        if (categoria != null && !categoria.isEmpty()) {
            faqs = faqs.stream()
                    .filter(f -> f.get("categoria").equals(categoria))
                    .collect(Collectors.toList());
        }

        // Ordenar por utilidad descendente
        faqs.sort((f1, f2) -> Integer.compare(
                (Integer) f2.get("util"),
                (Integer) f1.get("util")
        ));

        return faqs;
    }

    public Map<String, Object> obtenerFAQPorId(String id) {
        return FAQS_MOCK.stream()
                .filter(f -> f.get("id").equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("FAQ", "id", id));
    }

    public Map<String, Object> marcarFAQUtil(String id) {
        Map<String, Object> faq = obtenerFAQPorId(id);
        Integer util = (Integer) faq.get("util");
        faq.put("util", util + 1);
        return faq;
    }

    // ========== TICKETS ==========

    private List<Map<String, Object>> crearTicketsMock() {
        List<Map<String, Object>> tickets = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Ticket 1
        Map<String, Object> t1 = new HashMap<>();
        t1.put("id", "T001");
        t1.put("asunto", "Problema al escanear código QR");
        t1.put("descripcion", "El scanner no reconoce el código del contenedor en el campus");
        t1.put("categoria", "PROBLEMA_TECNICO");
        t1.put("prioridad", "ALTA");
        t1.put("estado", "EN_PROCESO");
        t1.put("fechaCreacion", LocalDateTime.now().minusDays(2).format(formatter));
        t1.put("fechaActualizacion", LocalDateTime.now().minusHours(3).format(formatter));
        t1.put("usuarioId", "USER001");
        t1.put("usuarioNombre", "Juan Pérez");

        List<Map<String, Object>> respuestas1 = new ArrayList<>();
        Map<String, Object> r1 = new HashMap<>();
        r1.put("id", "R001");
        r1.put("mensaje", "Hola Juan, ¿podrías indicarnos el código del contenedor que presenta problemas?");
        r1.put("fecha", LocalDateTime.now().minusDays(1).format(formatter));
        r1.put("esAdmin", true);
        r1.put("nombreUsuario", "Soporte EcoCoins");
        respuestas1.add(r1);

        Map<String, Object> r2 = new HashMap<>();
        r2.put("id", "R002");
        r2.put("mensaje", "Es el contenedor CONT-003 del edificio A");
        r2.put("fecha", LocalDateTime.now().minusHours(20).format(formatter));
        r2.put("esAdmin", false);
        r2.put("nombreUsuario", "Juan Pérez");
        respuestas1.add(r2);

        Map<String, Object> r3 = new HashMap<>();
        r3.put("id", "R003");
        r3.put("mensaje", "Gracias por la información. Hemos reportado el contenedor al equipo técnico. Será reparado en las próximas 24 horas.");
        r3.put("fecha", LocalDateTime.now().minusHours(3).format(formatter));
        r3.put("esAdmin", true);
        r3.put("nombreUsuario", "Soporte EcoCoins");
        respuestas1.add(r3);

        t1.put("respuestas", respuestas1);
        tickets.add(t1);

        // Ticket 2
        Map<String, Object> t2 = new HashMap<>();
        t2.put("id", "T002");
        t2.put("asunto", "No recibí mis EcoCoins");
        t2.put("descripcion", "Reciclé 5kg de plástico ayer pero no se reflejaron los EcoCoins en mi cuenta");
        t2.put("categoria", "CONSULTA_ECOCOINS");
        t2.put("prioridad", "MEDIA");
        t2.put("estado", "RESUELTO");
        t2.put("fechaCreacion", LocalDateTime.now().minusDays(3).format(formatter));
        t2.put("fechaActualizacion", LocalDateTime.now().minusDays(2).format(formatter));
        t2.put("usuarioId", "USER002");
        t2.put("usuarioNombre", "María García");

        List<Map<String, Object>> respuestas2 = new ArrayList<>();
        Map<String, Object> r4 = new HashMap<>();
        r4.put("id", "R004");
        r4.put("mensaje", "Hemos revisado tu cuenta y encontramos el registro. Los EcoCoins han sido acreditados. ¡Gracias por reciclar!");
        r4.put("fecha", LocalDateTime.now().minusDays(2).format(formatter));
        r4.put("esAdmin", true);
        r4.put("nombreUsuario", "Soporte EcoCoins");
        respuestas2.add(r4);

        t2.put("respuestas", respuestas2);
        tickets.add(t2);

        // Ticket 3
        Map<String, Object> t3 = new HashMap<>();
        t3.put("id", "T003");
        t3.put("asunto", "Sugerencia: Agregar más puntos de reciclaje");
        t3.put("descripcion", "Sería genial tener puntos de reciclaje en el estacionamiento también");
        t3.put("categoria", "SUGERENCIA");
        t3.put("prioridad", "BAJA");
        t3.put("estado", "ABIERTO");
        t3.put("fechaCreacion", LocalDateTime.now().minusHours(5).format(formatter));
        t3.put("fechaActualizacion", LocalDateTime.now().minusHours(5).format(formatter));
        t3.put("usuarioId", "USER003");
        t3.put("usuarioNombre", "Carlos López");
        t3.put("respuestas", new ArrayList<>());
        tickets.add(t3);

        return tickets;
    }

    public List<Map<String, Object>> obtenerTicketsUsuario(String usuarioId, String estado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        List<Map<String, Object>> tickets = TICKETS_MOCK.stream()
                .filter(t -> t.get("usuarioId").equals(usuarioId))
                .collect(Collectors.toList());

        if (estado != null && !estado.isEmpty()) {
            tickets = tickets.stream()
                    .filter(t -> t.get("estado").equals(estado))
                    .collect(Collectors.toList());
        }

        // Ordenar por fecha de actualización descendente
        tickets.sort((t1, t2) -> {
            String fecha1 = (String) t1.get("fechaActualizacion");
            String fecha2 = (String) t2.get("fechaActualizacion");
            return fecha2.compareTo(fecha1);
        });

        return tickets;
    }

    public Map<String, Object> obtenerTicketPorId(String id) {
        return TICKETS_MOCK.stream()
                .filter(t -> t.get("id").equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));
    }

    public Map<String, Object> crearTicket(String usuarioId, String asunto,
                                           String descripcion, String categoria, String prioridad) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        if (asunto == null || asunto.trim().isEmpty()) {
            throw new BadRequestException("El asunto es obligatorio");
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new BadRequestException("La descripción es obligatoria");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String ahora = LocalDateTime.now().format(formatter);

        Map<String, Object> ticket = new HashMap<>();
        ticket.put("id", "T" + String.format("%03d", ticketIdCounter++));
        ticket.put("asunto", asunto);
        ticket.put("descripcion", descripcion);
        ticket.put("categoria", categoria != null ? categoria : "OTRO");
        ticket.put("prioridad", prioridad != null ? prioridad : "MEDIA");
        ticket.put("estado", "ABIERTO");
        ticket.put("fechaCreacion", ahora);
        ticket.put("fechaActualizacion", ahora);
        ticket.put("usuarioId", usuarioId);
        ticket.put("usuarioNombre", usuario.getNombre());
        ticket.put("respuestas", new ArrayList<>());

        TICKETS_MOCK.add(ticket);

        // Enviar notificación
        notificacionService.enviarNotificacion(
                usuarioId,
                "🎫 Ticket creado",
                String.format("Tu ticket #%s ha sido creado. Te responderemos pronto.", ticket.get("id")),
                "info"
        );

        return ticket;
    }

    public Map<String, Object> responderTicket(String ticketId, String usuarioId, String mensaje) {
        Map<String, Object> ticket = obtenerTicketPorId(ticketId);

        if ("CERRADO".equals(ticket.get("estado"))) {
            throw new BadRequestException("No puedes responder a un ticket cerrado");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> respuestas = (List<Map<String, Object>>) ticket.get("respuestas");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", "R" + String.format("%03d", respuestas.size() + 1));
        respuesta.put("mensaje", mensaje);
        respuesta.put("fecha", LocalDateTime.now().format(formatter));
        respuesta.put("esAdmin", false);
        respuesta.put("nombreUsuario", usuario.getNombre());

        respuestas.add(respuesta);
        ticket.put("fechaActualizacion", LocalDateTime.now().format(formatter));

        return ticket;
    }

    public Map<String, Object> cambiarEstadoTicket(String ticketId, String nuevoEstado) {
        Map<String, Object> ticket = obtenerTicketPorId(ticketId);

        List<String> estadosValidos = Arrays.asList("ABIERTO", "EN_PROCESO", "RESUELTO", "CERRADO");
        if (!estadosValidos.contains(nuevoEstado)) {
            throw new BadRequestException("Estado inválido: " + nuevoEstado);
        }

        ticket.put("estado", nuevoEstado);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ticket.put("fechaActualizacion", LocalDateTime.now().format(formatter));

        // Notificar al usuario
        String usuarioId = (String) ticket.get("usuarioId");
        notificacionService.enviarNotificacion(
                usuarioId,
                "🎫 Actualización de ticket",
                String.format("Tu ticket #%s cambió a estado: %s", ticketId, nuevoEstado),
                "info"
        );

        return ticket;
    }
}