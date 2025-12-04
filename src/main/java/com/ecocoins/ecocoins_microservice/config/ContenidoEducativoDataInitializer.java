package com.ecocoins.ecocoins_microservice.config;

import com.ecocoins.ecocoins_microservice.model.ContenidoEducativo;
import com.ecocoins.ecocoins_microservice.repository.ContenidoEducativoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class ContenidoEducativoDataInitializer {

    private final ContenidoEducativoRepository contenidoRepository;

    public ContenidoEducativoDataInitializer(ContenidoEducativoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    @PostConstruct
    public void init() {
        if (contenidoRepository.count() == 0) {
            System.out.println("⚙️ Inicializando contenidos educativos...");

            ContenidoEducativo contenido1 = new ContenidoEducativo();
            contenido1.setTitulo("¿Qué es el reciclaje?");
            contenido1.setDescripcion("Aprende los conceptos básicos del reciclaje");
            contenido1.setContenido("El reciclaje es el proceso de convertir desechos en nuevos productos para prevenir el desperdicio de materiales potencialmente útiles, reducir el consumo de nueva materia prima y disminuir el uso de energía.");
            contenido1.setCategoria("BASICO");
            contenido1.setTipo("ARTICULO");
            contenido1.setDuracionMinutos(5);
            contenido1.setPuntosRecompensa(10);
            contenido1.setImagenUrl("https://example.com/reciclaje-basico.jpg");
            contenido1.setTags(Arrays.asList("reciclaje", "básico", "introducción"));
            contenido1.setFechaPublicacion(LocalDateTime.now());
            contenido1.setActivo(true);

            ContenidoEducativo contenido2 = new ContenidoEducativo();
            contenido2.setTitulo("Tipos de plásticos y cómo reciclarlos");
            contenido2.setDescripcion("Conoce los diferentes tipos de plásticos");
            contenido2.setContenido("Existen 7 tipos principales de plásticos identificados por números del 1 al 7. El PET (tipo 1) se usa en botellas de agua y refrescos, el HDPE (tipo 2) en envases de leche y detergentes...");
            contenido2.setCategoria("INTERMEDIO");
            contenido2.setTipo("VIDEO");
            contenido2.setVideoUrl("https://www.youtube.com/watch?v=example");
            contenido2.setDuracionMinutos(8);
            contenido2.setPuntosRecompensa(15);
            contenido2.setImagenUrl("https://example.com/plasticos.jpg");
            contenido2.setTags(Arrays.asList("plástico", "reciclaje", "tipos"));
            contenido2.setFechaPublicacion(LocalDateTime.now());
            contenido2.setActivo(true);

            ContenidoEducativo contenido3 = new ContenidoEducativo();
            contenido3.setTitulo("Compostaje en casa");
            contenido3.setDescripcion("Aprende a hacer compost con tus residuos orgánicos");
            contenido3.setContenido("El compostaje es una forma natural de reciclar materia orgánica como restos de comida y hojas. Es fácil, beneficioso para el medio ambiente y crea un fertilizante natural para tus plantas.");
            contenido3.setCategoria("AVANZADO");
            contenido3.setTipo("ARTICULO");
            contenido3.setDuracionMinutos(10);
            contenido3.setPuntosRecompensa(20);
            contenido3.setImagenUrl("https://example.com/compostaje.jpg");
            contenido3.setTags(Arrays.asList("compostaje", "orgánico", "casa"));
            contenido3.setFechaPublicacion(LocalDateTime.now());
            contenido3.setActivo(true);

            ContenidoEducativo contenido4 = new ContenidoEducativo();
            contenido4.setTitulo("Impacto ambiental de los residuos");
            contenido4.setDescripcion("Descubre cómo afectan los residuos al planeta");
            contenido4.setContenido("Cada año se generan millones de toneladas de residuos. Una botella de plástico puede tardar hasta 450 años en degradarse. El reciclaje ayuda a reducir este impacto significativamente.");
            contenido4.setCategoria("BASICO");
            contenido4.setTipo("INFOGRAFIA");
            contenido4.setDuracionMinutos(3);
            contenido4.setPuntosRecompensa(8);
            contenido4.setImagenUrl("https://example.com/impacto.jpg");
            contenido4.setTags(Arrays.asList("impacto", "medio ambiente", "conciencia"));
            contenido4.setFechaPublicacion(LocalDateTime.now());
            contenido4.setActivo(true);

            contenidoRepository.saveAll(Arrays.asList(contenido1, contenido2, contenido3, contenido4));
            System.out.println("✅ Contenidos educativos creados: " + contenidoRepository.count());
        }
    }
}
