package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.model.Contenedor;
import com.ecocoins.ecocoins_microservice.service.ContenedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contenedores")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Contenedores", description = "Gestión de contenedores de reciclaje")
public class ContenedorController {

    private final ContenedorService contenedorService;

    public ContenedorController(ContenedorService contenedorService) {
        this.contenedorService = contenedorService;
    }

    /**
     * Listar todos los contenedores
     * GET /api/contenedores
     */
    @GetMapping
    @Operation(summary = "Listar contenedores", description = "Obtiene todos los contenedores del sistema")
    public ResponseEntity<ApiResponse<List<Contenedor>>> listarTodos() {
        List<Contenedor> contenedores = contenedorService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success(contenedores));
    }

    /**
     * Listar contenedores activos
     * GET /api/contenedores/activos
     */
    @GetMapping("/activos")
    @Operation(summary = "Listar activos", description = "Obtiene solo los contenedores activos")
    public ResponseEntity<ApiResponse<List<Contenedor>>> listarActivos() {
        List<Contenedor> contenedores = contenedorService.listarActivos();
        return ResponseEntity.ok(ApiResponse.success(contenedores));
    }

    /**
     * Obtener contenedor por ID
     * GET /api/contenedores/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID", description = "Obtiene un contenedor específico por su ID")
    public ResponseEntity<ApiResponse<Contenedor>> obtenerPorId(@PathVariable String id) {
        Contenedor contenedor = contenedorService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(contenedor));
    }

    /**
     * Obtener contenedor por código
     * GET /api/contenedores/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Obtener por código", description = "Busca un contenedor por su código QR")
    public ResponseEntity<ApiResponse<Contenedor>> obtenerPorCodigo(@PathVariable String codigo) {
        Contenedor contenedor = contenedorService.obtenerPorCodigo(codigo);
        return ResponseEntity.ok(ApiResponse.success(contenedor));
    }

    /**
     * Crear nuevo contenedor
     * POST /api/contenedores
     */
    @PostMapping
    @Operation(summary = "Crear contenedor", description = "Registra un nuevo contenedor en el sistema (solo admin)")
    public ResponseEntity<ApiResponse<Contenedor>> crear(@RequestBody Contenedor contenedor) {
        Contenedor nuevo = contenedorService.crear(contenedor);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Contenedor creado exitosamente", nuevo));
    }

    /**
     * Actualizar contenedor
     * PUT /api/contenedores/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar contenedor", description = "Actualiza la información de un contenedor (solo admin)")
    public ResponseEntity<ApiResponse<Contenedor>> actualizar(
            @PathVariable String id,
            @RequestBody Contenedor contenedor) {

        Contenedor actualizado = contenedorService.actualizar(id, contenedor);
        return ResponseEntity.ok(ApiResponse.success("Contenedor actualizado", actualizado));
    }

    /**
     * Vaciar contenedor
     * POST /api/contenedores/{id}/vaciar
     */
    @PostMapping("/{id}/vaciar")
    @Operation(summary = "Vaciar contenedor", description = "Resetea la capacidad actual del contenedor (solo admin)")
    public ResponseEntity<ApiResponse<Contenedor>> vaciar(@PathVariable String id) {
        Contenedor vaciado = contenedorService.vaciar(id);
        return ResponseEntity.ok(ApiResponse.success("Contenedor vaciado exitosamente", vaciado));
    }

    /**
     * Cambiar estado del contenedor
     * PATCH /api/contenedores/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado", description = "Cambia el estado de un contenedor (solo admin)")
    public ResponseEntity<ApiResponse<Contenedor>> cambiarEstado(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String nuevoEstado = body.get("estado");
        Contenedor actualizado = contenedorService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(ApiResponse.success("Estado actualizado", actualizado));
    }

    /**
     * Eliminar contenedor
     * DELETE /api/contenedores/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar contenedor", description = "Elimina un contenedor del sistema (solo admin)")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable String id) {
        contenedorService.eliminar(id);

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Contenedor eliminado exitosamente");
        response.setData(null);

        return ResponseEntity.ok(response);
    }

    /**
     * Listar contenedores llenos
     * GET /api/contenedores/llenos
     */
    @GetMapping("/llenos")
    @Operation(summary = "Listar llenos", description = "Obtiene los contenedores que están llenos o cerca de llenarse")
    public ResponseEntity<ApiResponse<List<Contenedor>>> listarLlenos() {
        List<Contenedor> llenos = contenedorService.listarLlenos();
        return ResponseEntity.ok(ApiResponse.success(llenos));
    }

    /**
     * Obtener estadísticas de contenedores
     * GET /api/contenedores/estadisticas
     */
    @GetMapping("/estadisticas")
    @Operation(summary = "Estadísticas", description = "Obtiene estadísticas generales de los contenedores")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerEstadisticas() {
        Map<String, Object> stats = contenedorService.obtenerEstadisticas();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}