package com.ecocoins.ecocoins_microservice.config;

import com.ecocoins.ecocoins_microservice.model.FAQ;
import com.ecocoins.ecocoins_microservice.repository.FAQRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FAQDataInitializer {

    private final FAQRepository faqRepository;

    public FAQDataInitializer(FAQRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    @PostConstruct
    public void init() {
        if (faqRepository.count() == 0) {
            System.out.println("⚙️ Inicializando FAQs...");

            FAQ faq1 = new FAQ();
            faq1.setPregunta("¿Cómo gano EcoCoins?");
            faq1.setRespuesta("Ganas EcoCoins al reciclar materiales en los puntos de recolección del campus. Cada material tiene un valor específico. También puedes ganar EcoCoins completando quizzes educativos y desbloqueando logros especiales.");
            faq1.setCategoria("ECOCOINS");
            faq1.setOrden(1);
            faq1.setActivo(true);

            FAQ faq2 = new FAQ();
            faq2.setPregunta("¿Qué puedo canjear con mis EcoCoins?");
            faq2.setRespuesta("Puedes canjear tus EcoCoins por recompensas como descuentos en la cafetería, productos ecológicos, plantas, entradas a eventos, y más. Revisa la sección de Recompensas para ver todas las opciones disponibles y sus costos.");
            faq2.setCategoria("CANJES");
            faq2.setOrden(2);
            faq2.setActivo(true);

            FAQ faq3 = new FAQ();
            faq3.setPregunta("¿Dónde están los puntos de reciclaje?");
            faq3.setRespuesta("Puedes encontrar todos los puntos de reciclaje en el campus usando el mapa interactivo en la sección 'Mapa'. Allí verás ubicaciones exactas, horarios de atención y qué materiales acepta cada punto.");
            faq3.setCategoria("RECICLAJE");
            faq3.setOrden(3);
            faq3.setActivo(true);

            FAQ faq4 = new FAQ();
            faq4.setPregunta("¿Cómo escaneo un material para reciclarlo?");
            faq4.setRespuesta("Ve a la sección 'Reciclar', escanea el código QR del punto de reciclaje, toma una foto del material que vas a reciclar, selecciona el tipo de material y espera la validación. Recibirás tus EcoCoins automáticamente una vez validado.");
            faq4.setCategoria("RECICLAJE");
            faq4.setOrden(4);
            faq4.setActivo(true);

            FAQ faq5 = new FAQ();
            faq5.setPregunta("¿Qué son los logros?");
            faq5.setRespuesta("Los logros son reconocimientos que obtienes al alcanzar metas específicas, como reciclar cierta cantidad de materiales, completar quizzes educativos, o alcanzar niveles en el ranking. Cada logro te da EcoCoins extra como recompensa.");
            faq5.setCategoria("LOGROS");
            faq5.setOrden(5);
            faq5.setActivo(true);

            FAQ faq6 = new FAQ();
            faq6.setPregunta("¿Cómo subo en el ranking?");
            faq6.setRespuesta("Tu posición en el ranking depende de la cantidad total de EcoCoins que hayas ganado. Recicla más materiales, completa quizzes educativos y desbloquea logros para acumular más EcoCoins y subir de posición.");
            faq6.setCategoria("RANKING");
            faq6.setOrden(6);
            faq6.setActivo(true);

            FAQ faq7 = new FAQ();
            faq7.setPregunta("¿Puedo cancelar un canje?");
            faq7.setRespuesta("Sí, puedes cancelar un canje solo si está en estado 'pendiente'. Los EcoCoins se devolverán automáticamente a tu cuenta. Si el canje ya fue procesado o entregado, no podrás cancelarlo.");
            faq7.setCategoria("CANJES");
            faq7.setOrden(7);
            faq7.setActivo(true);

            FAQ faq8 = new FAQ();
            faq8.setPregunta("¿Qué materiales puedo reciclar?");
            faq8.setRespuesta("Puedes reciclar papel, cartón, plásticos (tipos 1-7), vidrio, metal, electrónicos y residuos orgánicos. Cada punto de recolección acepta materiales específicos, verifica en el mapa qué acepta cada uno.");
            faq8.setCategoria("RECICLAJE");
            faq8.setOrden(8);
            faq8.setActivo(true);

            FAQ faq9 = new FAQ();
            faq9.setPregunta("¿Los EcoCoins expiran?");
            faq9.setRespuesta("No, tus EcoCoins no tienen fecha de expiración. Puedes acumularlos y usarlos cuando quieras canjear alguna recompensa.");
            faq9.setCategoria("ECOCOINS");
            faq9.setOrden(9);
            faq9.setActivo(true);

            FAQ faq10 = new FAQ();
            faq10.setPregunta("¿Cómo contacto con soporte?");
            faq10.setRespuesta("Puedes crear un ticket de soporte desde la sección 'Ayuda'. Describe tu problema y nuestro equipo te responderá lo antes posible. También puedes consultar esta sección de preguntas frecuentes.");
            faq10.setCategoria("SOPORTE");
            faq10.setOrden(10);
            faq10.setActivo(true);

            faqRepository.saveAll(Arrays.asList(faq1, faq2, faq3, faq4, faq5, faq6, faq7, faq8, faq9, faq10));
            System.out.println("✅ FAQs creadas: " + faqRepository.count());
        }
    }
}
