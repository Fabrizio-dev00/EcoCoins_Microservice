package com.ecocoins.ecocoins_microservice.config;

import com.ecocoins.ecocoins_microservice.model.Ticket;
import com.ecocoins.ecocoins_microservice.repository.TicketRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class TicketDataInitializer {

    private final TicketRepository ticketRepository;

    public TicketDataInitializer(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @PostConstruct
    public void init() {
        if (ticketRepository.count() == 0) {
            System.out.println("⚙️ Inicializando tickets de ejemplo...");

            Ticket ticket1 = new Ticket();
            ticket1.setUsuarioId("user123");
            ticket1.setAsunto("No puedo canjear una recompensa");
            ticket1.setDescripcion("Intento canjear el descuento de cafetería pero me dice que no tengo suficientes EcoCoins, aunque en mi perfil aparecen 150 EC.");
            ticket1.setCategoria("CANJES");
            ticket1.setPrioridad("MEDIA");
            ticket1.setEstado("ABIERTO");
            ticket1.setFechaCreacion(LocalDateTime.now().minusDays(1));

            Ticket ticket2 = new Ticket();
            ticket2.setUsuarioId("user456");
            ticket2.setAsunto("Error al escanear código QR");
            ticket2.setDescripcion("La cámara se abre pero no detecta el código QR del punto de reciclaje de la biblioteca.");
            ticket2.setCategoria("TECNICO");
            ticket2.setPrioridad("ALTA");
            ticket2.setEstado("EN_PROCESO");
            ticket2.setFechaCreacion(LocalDateTime.now().minusHours(5));

            Ticket ticket3 = new Ticket();
            ticket3.setUsuarioId("user789");
            ticket3.setAsunto("Consulta sobre logros");
            ticket3.setDescripcion("¿Cómo puedo desbloquear el logro 'Eco Warrior'? Ya reciclé más de 50 items pero no aparece.");
            ticket3.setCategoria("LOGROS");
            ticket3.setPrioridad("BAJA");
            ticket3.setEstado("RESUELTO");
            ticket3.setFechaCreacion(LocalDateTime.now().minusDays(3));
            ticket3.setFechaActualizacion(LocalDateTime.now().minusDays(2));

            ticketRepository.saveAll(Arrays.asList(ticket1, ticket2, ticket3));
            System.out.println("✅ Tickets de ejemplo creados: " + ticketRepository.count());
        }
    }
}
