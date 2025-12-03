package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.service.EducacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/educacion")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Educación", description = "Contenido educativo sobre reciclaje")
public class EducacionController {

    private final EducacionService educacionService;

    public EducacionController(EducacionService educacionService) {
        this.educacionService = educacionService;
    }

    /**
     * Obtener todos los contenidos educativos
     * GET /api/educacion/contenidos
     */
    @GetMapping("/contenidos")
    @Operation(summary = "Listar contenidos", description = "Obtiene todos los contenidos educativos disponibles")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerContenidos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String tipo) {

        List<Map<String, Object>> contenidos = educacionService.obtenerContenidos(categoria, tipo);
        return ResponseEntity.ok(ApiResponse.success(contenidos));
    }

    /**
     * Obtener contenido por ID
     * GET /api/educacion/contenidos/{id}
     */
    @GetMapping("/contenidos/{id}")
    @Operation(summary = "Detalle del contenido", description = "Obtiene información detallada de un contenido educativo")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerContenidoPorId(
            @PathVariable String id) {

        Map<String, Object> contenido = educacionService.obtenerContenidoPorId(id);
        return ResponseEntity.ok(ApiResponse.success(contenido));
    }

    /**
     * Obtener progreso de un usuario
     * GET /api/educacion/progreso/{usuarioId}
     */
    @GetMapping("/progreso/{usuarioId}")
    @Operation(summary = "Progreso del usuario", description = "Obtiene el progreso de aprendizaje del usuario")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerProgreso(
            @PathVariable String usuarioId) {

        Map<String, Object> progreso = educacionService.obtenerProgresoUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(progreso));
    }

    /**
     * Marcar contenido como completado
     * POST /api/educacion/completar
     */
    @PostMapping("/completar")
    @Operation(summary = "Completar contenido", description = "Marca un contenido como completado y otorga EcoCoins")
    public ResponseEntity<ApiResponse<Map<String, Object>>> completarContenido(
            @RequestBody Map<String, String> request) {

        String usuarioId = request.get("usuarioId");
        String contenidoId = request.get("contenidoId");

        Map<String, Object> resultado = educacionService.completarContenido(usuarioId, contenidoId);

        return ResponseEntity.ok(ApiResponse.success(
                "¡Contenido completado! Has ganado EcoCoins",
                resultado
        ));
    }

    /**
     * Obtener quiz
     * GET /api/educacion/quiz/{quizId}
     */
    @GetMapping("/quiz/{quizId}")
    @Operation(summary = "Obtener quiz", description = "Obtiene un quiz educativo con sus preguntas")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerQuiz(
            @PathVariable String quizId) {

        Map<String, Object> quiz = educacionService.obtenerQuiz(quizId);
        return ResponseEntity.ok(ApiResponse.success(quiz));
    }

    /**
     * Enviar respuestas de quiz
     * POST /api/educacion/quiz/enviar
     */
    @PostMapping("/quiz/enviar")
    @Operation(summary = "Enviar quiz", description = "Envía las respuestas del quiz y obtiene resultados")
    public ResponseEntity<ApiResponse<Map<String, Object>>> enviarQuiz(
            @RequestBody Map<String, Object> request) {

        String usuarioId = (String) request.get("usuarioId");
        String quizId = (String) request.get("quizId");

        @SuppressWarnings("unchecked")
        List<Integer> respuestas = (List<Integer>) request.get("respuestas");

        Map<String, Object> resultado = educacionService.evaluarQuiz(usuarioId, quizId, respuestas);

        return ResponseEntity.ok(ApiResponse.success(
                "Quiz evaluado exitosamente",
                resultado
        ));
    }

    /**
     * Obtener categorías disponibles
     * GET /api/educacion/categorias
     */
    @GetMapping("/categorias")
    @Operation(summary = "Listar categorías", description = "Obtiene todas las categorías de contenido educativo")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> obtenerCategorias() {
        List<Map<String, String>> categorias = educacionService.obtenerCategorias();
        return ResponseEntity.ok(ApiResponse.success(categorias));
    }
}