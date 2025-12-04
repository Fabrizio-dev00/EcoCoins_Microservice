package com.ecocoins.ecocoins_microservice.config;

import com.ecocoins.ecocoins_microservice.model.Quiz;
import com.ecocoins.ecocoins_microservice.model.Pregunta;
import com.ecocoins.ecocoins_microservice.repository.QuizRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class QuizDataInitializer {

    private final QuizRepository quizRepository;

    public QuizDataInitializer(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @PostConstruct
    public void init() {
        if (quizRepository.count() == 0) {
            System.out.println("⚙️ Inicializando quizzes...");

            // Quiz 1: Reciclaje Básico
            Quiz quiz1 = new Quiz();
            quiz1.setTitulo("Quiz: Reciclaje Básico");
            quiz1.setDescripcion("Pon a prueba tus conocimientos básicos sobre reciclaje");
            quiz1.setCategoria("BASICO");
            quiz1.setDificultad("FACIL");
            quiz1.setPuntosRecompensa(15);
            quiz1.setTiempoLimiteMinutos(10);
            quiz1.setActivo(true);

            Pregunta p1 = new Pregunta();
            p1.setPregunta("¿Qué color de contenedor se usa para papel y cartón?");
            p1.setOpciones(Arrays.asList("Azul", "Amarillo", "Verde", "Gris"));
            p1.setRespuestaCorrecta(0);
            p1.setExplicacion("El contenedor azul es para papel y cartón");

            Pregunta p2 = new Pregunta();
            p2.setPregunta("¿Cuánto tiempo tarda en degradarse una botella de plástico?");
            p2.setOpciones(Arrays.asList("1 año", "10 años", "100 años", "450 años"));
            p2.setRespuestaCorrecta(3);
            p2.setExplicacion("Una botella de plástico puede tardar hasta 450 años en degradarse");

            Pregunta p3 = new Pregunta();
            p3.setPregunta("¿Qué material NO es reciclable?");
            p3.setOpciones(Arrays.asList("Vidrio", "Papel sucio de grasa", "Latas de aluminio", "Cartón"));
            p3.setRespuestaCorrecta(1);
            p3.setExplicacion("El papel sucio de grasa no se puede reciclar porque contamina el proceso");

            quiz1.setPreguntas(Arrays.asList(p1, p2, p3));

            // Quiz 2: Plásticos
            Quiz quiz2 = new Quiz();
            quiz2.setTitulo("Quiz: Tipos de Plásticos");
            quiz2.setDescripcion("¿Conoces los diferentes tipos de plásticos?");
            quiz2.setCategoria("INTERMEDIO");
            quiz2.setDificultad("MEDIO");
            quiz2.setPuntosRecompensa(20);
            quiz2.setTiempoLimiteMinutos(15);
            quiz2.setActivo(true);

            Pregunta p4 = new Pregunta();
            p4.setPregunta("¿Qué significa PET en los plásticos?");
            p4.setOpciones(Arrays.asList(
                    "Polietileno Tereftalato",
                    "Plástico Ecológico Tratado",
                    "Poliéster Térmico",
                    "Producto Reciclable"
            ));
            p4.setRespuestaCorrecta(0);
            p4.setExplicacion("PET significa Polietileno Tereftalato, es el tipo 1 de plástico usado en botellas");

            Pregunta p5 = new Pregunta();
            p5.setPregunta("¿Cuál es el símbolo del reciclaje de plásticos?");
            p5.setOpciones(Arrays.asList(
                    "Un número dentro de un triángulo",
                    "Una hoja verde",
                    "Un círculo azul",
                    "Una estrella"
            ));
            p5.setRespuestaCorrecta(0);
            p5.setExplicacion("Los plásticos se identifican con un número del 1 al 7 dentro de un triángulo de flechas");

            Pregunta p6 = new Pregunta();
            p6.setPregunta("¿Qué tipo de plástico es el HDPE?");
            p6.setOpciones(Arrays.asList("Tipo 1", "Tipo 2", "Tipo 3", "Tipo 4"));
            p6.setRespuestaCorrecta(1);
            p6.setExplicacion("HDPE es el tipo 2, usado en envases de leche y detergentes");

            quiz2.setPreguntas(Arrays.asList(p4, p5, p6));

            // Quiz 3: Compostaje
            Quiz quiz3 = new Quiz();
            quiz3.setTitulo("Quiz: Compostaje y Orgánicos");
            quiz3.setDescripcion("¿Sabes cómo hacer compost correctamente?");
            quiz3.setCategoria("AVANZADO");
            quiz3.setDificultad("DIFICIL");
            quiz3.setPuntosRecompensa(25);
            quiz3.setTiempoLimiteMinutos(20);
            quiz3.setActivo(true);

            Pregunta p7 = new Pregunta();
            p7.setPregunta("¿Cuál de estos NO debe ir en el compost?");
            p7.setOpciones(Arrays.asList(
                    "Cáscaras de frutas",
                    "Restos de carne",
                    "Hojas secas",
                    "Café molido"
            ));
            p7.setRespuestaCorrecta(1);
            p7.setExplicacion("Los restos de carne pueden atraer plagas y generar malos olores");

            Pregunta p8 = new Pregunta();
            p8.setPregunta("¿Cuánto tiempo tarda en estar listo el compost?");
            p8.setOpciones(Arrays.asList("1 semana", "1 mes", "3-6 meses", "1 año"));
            p8.setRespuestaCorrecta(2);
            p8.setExplicacion("El compost suele estar listo entre 3 y 6 meses dependiendo de las condiciones");

            quiz3.setPreguntas(Arrays.asList(p7, p8));

            quizRepository.saveAll(Arrays.asList(quiz1, quiz2, quiz3));
            System.out.println("✅ Quizzes creados: " + quizRepository.count());
        }
    }
}
