package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.dto.CanjeProfesorRequest;
import com.ecocoins.ecocoins_microservice.model.CanjeProfesor;
import com.ecocoins.ecocoins_microservice.model.Profesor;
import com.ecocoins.ecocoins_microservice.model.RecompensaProfesor;
import com.ecocoins.ecocoins_microservice.service.ProfesorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profesores")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Profesores", description = "Gestión de profesores y sus servicios")
public class ProfesorController {

    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    /**
     * Listar profesores activos
     * GET /api/profesores
     */
    @GetMapping
    @Operation(summary = "Listar profesores", description = "Obtiene todos los profesores activos del sistema")
    public ResponseEntity<ApiResponse<List<Profesor>>> listarProfesores() {
        List<Profesor> profesores = profesorService.listarProfesoresActivos();
        return ResponseEntity.ok(ApiResponse.success(profesores));
    }

    /**
     * Obtener profesor por ID
     * GET /api/profesores/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Detalle de profesor", description = "Obtiene la información detallada de un profesor")
    public ResponseEntity<ApiResponse<Profesor>> obtenerProfesor(@PathVariable String id) {
        Profesor profesor = profesorService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(profesor));
    }

    /**
     * Listar servicios de un profesor
     * GET /api/profesores/{id}/recompensas
     */
    @GetMapping("/{id}/recompensas")
    @Operation(summary = "Servicios del profesor", description = "Obtiene todos los servicios disponibles de un profesor")
    public ResponseEntity<ApiResponse<List<RecompensaProfesor>>> listarServiciosProfesor(
            @PathVariable String id) {

        List<RecompensaProfesor> recompensas = profesorService.listarRecompensasProfesor(id);
        return ResponseEntity.ok(ApiResponse.success(recompensas));
    }

    /**
     * Canjear servicio de profesor
     * POST /api/profesores/canjear
     */
    @PostMapping("/canjear")
    @Operation(summary = "Canjear servicio", description = "Solicita un servicio de un profesor usando EcoCoins")
    public ResponseEntity<ApiResponse<Map<String, Object>>> canjearServicio(
            @Valid @RequestBody CanjeProfesorRequest request) {

        Map<String, Object> resultado = profesorService.canjearRecompensaProfesor(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Solicitud enviada exitosamente", resultado));
    }

    /**
     * Mis canjes con profesores
     * GET /api/profesores/canjes/usuario/{usuarioId}
     */
    @GetMapping("/canjes/usuario/{usuarioId}")
    @Operation(summary = "Mis canjes con profesores", description = "Obtiene todos los canjes de servicios de profesores de un usuario")
    public ResponseEntity<ApiResponse<List<CanjeProfesor>>> listarMisCanjes(
            @PathVariable String usuarioId) {

        List<CanjeProfesor> canjes = profesorService.listarCanjesUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(canjes));
    }

    /**
     * Detalle de un canje con profesor
     * GET /api/profesores/canjes/{canjeId}
     */
    @GetMapping("/canjes/{canjeId}")
    @Operation(summary = "Detalle de canje", description = "Obtiene el detalle de un canje con profesor")
    public ResponseEntity<ApiResponse<CanjeProfesor>> obtenerDetalleCanje(
            @PathVariable String canjeId) {

        CanjeProfesor canje = profesorService.obtenerCanjePorId(canjeId);
        return ResponseEntity.ok(ApiResponse.success(canje));
    }

    /**
     * Buscar profesores por especialidad
     * GET /api/profesores/buscar?especialidad=Matematicas
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar por especialidad", description = "Busca profesores por especialidad")
    public ResponseEntity<ApiResponse<List<Profesor>>> buscarPorEspecialidad(
            @RequestParam String especialidad) {

        List<Profesor> profesores = profesorService.buscarPorEspecialidad(especialidad);
        return ResponseEntity.ok(ApiResponse.success(profesores));
    }

    /**
     * Listar mejores profesores
     * GET /api/profesores/top
     */
    @GetMapping("/top")
    @Operation(summary = "Mejores profesores", description = "Obtiene los profesores mejor calificados (rating >= 4.0)")
    public ResponseEntity<ApiResponse<List<Profesor>>> listarMejores() {
        List<Profesor> profesores = profesorService.listarMejorCalificados();
        return ResponseEntity.ok(ApiResponse.success(profesores));
    }

    /**
     * Crear profesor (admin)
     * POST /api/profesores
     */
    @PostMapping
    @Operation(summary = "Crear profesor", description = "Registra un nuevo profesor en el sistema (solo admin)")
    public ResponseEntity<ApiResponse<Profesor>> crearProfesor(@Valid @RequestBody Profesor profesor) {
        Profesor nuevo = profesorService.crearProfesor(profesor);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Profesor creado exitosamente", nuevo));
    }
}