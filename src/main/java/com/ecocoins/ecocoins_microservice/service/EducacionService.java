package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EducacionService {

    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    // Simulación de contenidos completados por usuario (en producción usar MongoDB)
    private final Map<String, Set<String>> contenidosCompletados = new HashMap<>();

    public EducacionService(UsuarioRepository usuarioRepository,
                            NotificacionService notificacionService) {
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    /**
     * Obtener todos los contenidos educativos (mock data)
     */
    private List<Map<String, Object>> obtenerContenidosMock() {
        List<Map<String, Object>> contenidos = new ArrayList<>();

        // Contenido 1: Artículo
        Map<String, Object> c1 = new HashMap<>();
        c1.put("id", "C001");
        c1.put("titulo", "Introducción al Reciclaje");
        c1.put("descripcion", "Aprende los conceptos básicos del reciclaje y su importancia");
        c1.put("tipo", "ARTICULO");
        c1.put("categoria", "RECICLAJE_BASICO");
        c1.put("dificultad", "PRINCIPIANTE");
        c1.put("duracionMinutos", 5);
        c1.put("imagenUrl", "https://example.com/intro-reciclaje.jpg");
        c1.put("puntosClave", Arrays.asList(
                "El reciclaje reduce la cantidad de residuos",
                "Ayuda a conservar recursos naturales",
                "Disminuye la contaminación ambiental"
        ));
        c1.put("recompensaEcoCoins", 20);
        contenidos.add(c1);

        // Contenido 2: Guía
        Map<String, Object> c2 = new HashMap<>();
        c2.put("id", "C002");
        c2.put("titulo", "Cómo Separar Residuos Correctamente");
        c2.put("descripcion", "Guía paso a paso para separar tus residuos en casa");
        c2.put("tipo", "GUIA");
        c2.put("categoria", "SEPARACION_RESIDUOS");
        c2.put("dificultad", "PRINCIPIANTE");
        c2.put("duracionMinutos", 10);
        c2.put("imagenUrl", "https://example.com/separar-residuos.jpg");
        c2.put("puntosClave", Arrays.asList(
                "Identifica los tipos de materiales",
                "Usa contenedores diferenciados",
                "Limpia los envases antes de reciclar"
        ));
        c2.put("recompensaEcoCoins", 30);
        contenidos.add(c2);

        // Contenido 3: Video
        Map<String, Object> c3 = new HashMap<>();
        c3.put("id", "C003");
        c3.put("titulo", "El Impacto del Plástico en los Océanos");
        c3.put("descripcion", "Documental sobre la contaminación plástica marina");
        c3.put("tipo", "VIDEO");
        c3.put("categoria", "IMPACTO_AMBIENTAL");
        c3.put("dificultad", "INTERMEDIO");
        c3.put("duracionMinutos", 15);
        c3.put("imagenUrl", "https://example.com/plastico-oceanos.jpg");
        c3.put("puntosClave", Arrays.asList(
                "8 millones de toneladas de plástico llegan al océano anualmente",
                "Afecta a más de 800 especies marinas",
                "El plástico tarda cientos de años en degradarse"
        ));
        c3.put("recompensaEcoCoins", 50);
        contenidos.add(c3);

        // Contenido 4: Quiz
        Map<String, Object> c4 = new HashMap<>();
        c4.put("id", "C004");
        c4.put("titulo", "Quiz: ¿Cuánto Sabes de Reciclaje?");
        c4.put("descripcion", "Pon a prueba tus conocimientos sobre reciclaje");
        c4.put("tipo", "QUIZ");
        c4.put("categoria", "RECICLAJE_BASICO");
        c4.put("dificultad", "PRINCIPIANTE");
        c4.put("duracionMinutos", 10);
        c4.put("imagenUrl", "https://example.com/quiz-reciclaje.jpg");
        c4.put("puntosClave", Arrays.asList(
                "10 preguntas sobre reciclaje básico",
                "Respuestas con explicación",
                "Certificado al completar"
        ));
        c4.put("recompensaEcoCoins", 100);
        contenidos.add(c4);

        // Contenido 5: Artículo Avanzado
        Map<String, Object> c5 = new HashMap<>();
        c5.put("id", "C005");
        c5.put("titulo", "Economía Circular: El Futuro del Consumo");
        c5.put("descripcion", "Descubre cómo la economía circular transforma la industria");
        c5.put("tipo", "ARTICULO");
        c5.put("categoria", "ECONOMIA_CIRCULAR");
        c5.put("dificultad", "AVANZADO");
        c5.put("duracionMinutos", 20);
        c5.put("imagenUrl", "https://example.com/economia-circular.jpg");
        c5.put("puntosClave", Arrays.asList(
                "Modelo económico regenerativo",
                "Reduce, reutiliza, recicla",
                "Crea valor a partir de residuos"
        ));
        c5.put("recompensaEcoCoins", 75);
        contenidos.add(c5);

        // Contenido 6: Infografía
        Map<String, Object> c6 = new HashMap<>();
        c6.put("id", "C006");
        c6.put("titulo", "10 Consejos para Reducir tu Huella de Carbono");
        c6.put("descripcion", "Infografía con tips prácticos para el día a día");
        c6.put("tipo", "INFOGRAFIA");
        c6.put("categoria", "CONSEJOS_PRACTICOS");
        c6.put("dificultad", "PRINCIPIANTE");
        c6.put("duracionMinutos", 5);
        c6.put("imagenUrl", "https://example.com/huella-carbono.jpg");
        c6.put("puntosClave", Arrays.asList(
                "Usa transporte público o bicicleta",
                "Reduce el consumo de carne",
                "Apaga dispositivos no utilizados"
        ));
        c6.put("recompensaEcoCoins", 40);
        contenidos.add(c6);

        return contenidos;
    }

    /**
     * Obtener contenidos con filtros
     */
    public List<Map<String, Object>> obtenerContenidos(String categoria, String tipo) {
        List<Map<String, Object>> contenidos = obtenerContenidosMock();

        if (categoria != null && !categoria.isEmpty()) {
            contenidos = contenidos.stream()
                    .filter(c -> c.get("categoria").equals(categoria))
                    .collect(Collectors.toList());
        }

        if (tipo != null && !tipo.isEmpty()) {
            contenidos = contenidos.stream()
                    .filter(c -> c.get("tipo").equals(tipo))
                    .collect(Collectors.toList());
        }

        return contenidos;
    }

    /**
     * Obtener contenido por ID
     */
    public Map<String, Object> obtenerContenidoPorId(String id) {
        return obtenerContenidosMock().stream()
                .filter(c -> c.get("id").equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Contenido educativo", "id", id));
    }

    /**
     * Obtener progreso del usuario
     */
    public Map<String, Object> obtenerProgresoUsuario(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        Set<String> completados = contenidosCompletados.getOrDefault(usuarioId, new HashSet<>());
        List<Map<String, Object>> todosContenidos = obtenerContenidosMock();

        int totalContenidos = todosContenidos.size();
        int contenidosCompletadosCount = completados.size();

        int ecoCoinsGanados = todosContenidos.stream()
                .filter(c -> completados.contains(c.get("id")))
                .mapToInt(c -> (Integer) c.get("recompensaEcoCoins"))
                .sum();

        return Map.of(
                "totalContenidos", totalContenidos,
                "contenidosCompletados", contenidosCompletadosCount,
                "progresoPorcentaje", totalContenidos > 0
                        ? (contenidosCompletadosCount * 100) / totalContenidos
                        : 0,
                "ecoCoinsGanados", ecoCoinsGanados
        );
    }

    /**
     * Completar contenido
     */
    @Transactional
    public Map<String, Object> completarContenido(String usuarioId, String contenidoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        Map<String, Object> contenido = obtenerContenidoPorId(contenidoId);

        // Verificar si ya fue completado
        Set<String> completados = contenidosCompletados.computeIfAbsent(
                usuarioId, k -> new HashSet<>());

        if (completados.contains(contenidoId)) {
            throw new BadRequestException("Este contenido ya fue completado anteriormente");
        }

        // Marcar como completado
        completados.add(contenidoId);

        // Otorgar EcoCoins
        Integer recompensa = (Integer) contenido.get("recompensaEcoCoins");
        usuario.setEcoCoins(usuario.getEcoCoins() + recompensa);
        usuarioRepository.save(usuario);

        // Enviar notificación
        notificacionService.enviarNotificacion(
                usuarioId,
                "📚 ¡Contenido completado!",
                String.format("Has completado '%s' y ganado +%d EcoCoins",
                        contenido.get("titulo"), recompensa),
                "success"
        );

        return Map.of(
                "mensaje", "Contenido completado exitosamente",
                "contenido", contenido.get("titulo"),
                "ecoCoinsGanados", recompensa,
                "nuevoBalance", usuario.getEcoCoins()
        );
    }

    /**
     * Obtener quiz
     */
    public Map<String, Object> obtenerQuiz(String quizId) {
        // Mock data del quiz
        if (!"C004".equals(quizId)) {
            throw new ResourceNotFoundException("Quiz", "id", quizId);
        }

        List<Map<String, Object>> preguntas = new ArrayList<>();

        Map<String, Object> p1 = new HashMap<>();
        p1.put("id", 1);
        p1.put("pregunta", "¿Qué significa reciclar?");
        p1.put("opciones", Arrays.asList(
                "Tirar todo a la basura",
                "Transformar residuos en nuevos productos",
                "Quemar los desechos",
                "Enterrar la basura"
        ));
        p1.put("respuestaCorrecta", 1);
        p1.put("explicacion", "Reciclar es el proceso de transformar residuos en nuevos productos útiles");
        preguntas.add(p1);

        Map<String, Object> p2 = new HashMap<>();
        p2.put("id", 2);
        p2.put("pregunta", "¿Cuál de estos materiales NO se puede reciclar fácilmente?");
        p2.put("opciones", Arrays.asList(
                "Papel",
                "Vidrio",
                "Unicel (poliestireno)",
                "Aluminio"
        ));
        p2.put("respuestaCorrecta", 2);
        p2.put("explicacion", "El unicel es difícil de reciclar debido a su composición química");
        preguntas.add(p2);

        Map<String, Object> p3 = new HashMap<>();
        p3.put("id", 3);
        p3.put("pregunta", "¿De qué color es el contenedor para papel?");
        p3.put("opciones", Arrays.asList(
                "Verde",
                "Azul",
                "Amarillo",
                "Gris"
        ));
        p3.put("respuestaCorrecta", 1);
        p3.put("explicacion", "El contenedor azul es para papel y cartón");
        preguntas.add(p3);

        return Map.of(
                "id", quizId,
                "titulo", "Quiz: ¿Cuánto Sabes de Reciclaje?",
                "descripcion", "Pon a prueba tus conocimientos",
                "preguntas", preguntas,
                "totalPreguntas", preguntas.size(),
                "tiempoLimiteMinutos", 10,
                "recompensaEcoCoins", 100
        );
    }

    /**
     * Evaluar quiz
     */
    @Transactional
    public Map<String, Object> evaluarQuiz(String usuarioId, String quizId, List<Integer> respuestas) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        Map<String, Object> quiz = obtenerQuiz(quizId);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> preguntas = (List<Map<String, Object>>) quiz.get("preguntas");

        int respuestasCorrectas = 0;
        for (int i = 0; i < preguntas.size(); i++) {
            Map<String, Object> pregunta = preguntas.get(i);
            Integer respuestaCorrecta = (Integer) pregunta.get("respuestaCorrecta");

            if (i < respuestas.size() && respuestas.get(i).equals(respuestaCorrecta)) {
                respuestasCorrectas++;
            }
        }

        int totalPreguntas = preguntas.size();
        int puntuacion = (respuestasCorrectas * 100) / totalPreguntas;

        // Otorgar EcoCoins si aprueba (70% o más)
        int ecoCoinsGanados = 0;
        boolean aprobado = puntuacion >= 70;

        if (aprobado) {
            ecoCoinsGanados = (Integer) quiz.get("recompensaEcoCoins");
            usuario.setEcoCoins(usuario.getEcoCoins() + ecoCoinsGanados);
            usuarioRepository.save(usuario);

            // Marcar quiz como completado
            Set<String> completados = contenidosCompletados.computeIfAbsent(
                    usuarioId, k -> new HashSet<>());
            completados.add(quizId);

            notificacionService.enviarNotificacion(
                    usuarioId,
                    "🎉 ¡Quiz aprobado!",
                    String.format("Has obtenido %d%% y ganado +%d EcoCoins", puntuacion, ecoCoinsGanados),
                    "success"
            );
        }

        return Map.of(
                "puntuacion", puntuacion,
                "totalPreguntas", totalPreguntas,
                "respuestasCorrectas", respuestasCorrectas,
                "aprobado", aprobado,
                "ecoCoinsGanados", ecoCoinsGanados,
                "nuevoBalance", usuario.getEcoCoins(),
                "certificado", aprobado ? "Certificado generado" : null
        );
    }

    /**
     * Obtener categorías
     */
    public List<Map<String, String>> obtenerCategorias() {
        List<Map<String, String>> categorias = new ArrayList<>();

        categorias.add(Map.of(
                "id", "RECICLAJE_BASICO",
                "nombre", "Reciclaje Básico",
                "icono", "♻️"
        ));

        categorias.add(Map.of(
                "id", "SEPARACION_RESIDUOS",
                "nombre", "Separación de Residuos",
                "icono", "🗑️"
        ));

        categorias.add(Map.of(
                "id", "IMPACTO_AMBIENTAL",
                "nombre", "Impacto Ambiental",
                "icono", "🌍"
        ));

        categorias.add(Map.of(
                "id", "ECONOMIA_CIRCULAR",
                "nombre", "Economía Circular",
                "icono", "🔄"
        ));

        categorias.add(Map.of(
                "id", "CONSEJOS_PRACTICOS",
                "nombre", "Consejos Prácticos",
                "icono", "💡"
        ));

        return categorias;
    }
}