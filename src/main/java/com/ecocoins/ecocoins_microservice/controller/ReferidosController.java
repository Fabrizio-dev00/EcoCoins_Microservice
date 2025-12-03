package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.service.ReferidosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/referidos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Referidos", description = "Sistema de referidos y recompensas")
public class ReferidosController {

    private final ReferidosService referidosService;

    public ReferidosController(ReferidosService referidosService) {
        this.referidosService = referidosService;
    }

    /**
     * Obtener información de referidos de un usuario
     * GET /api/referidos/usuario/{usuarioId}
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Mis referidos", description = "Obtiene información de referidos del usuario")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerReferidos(
            @PathVariable String usuarioId) {

        Map<String, Object> referidos = referidosService.obtenerReferidosUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(referidos));
    }

    /**
     * Generar código de referido
     * POST /api/referidos/generar-codigo
     */
    @PostMapping("/generar-codigo")
    @Operation(summary = "Generar código", description = "Genera un código único de referido para el usuario")
    public ResponseEntity<ApiResponse<Map<String, String>>> generarCodigo(
            @RequestBody Map<String, String> request) {

        String usuarioId = request.get("usuarioId");
        String codigo = referidosService.generarCodigoReferido(usuarioId);

        return ResponseEntity.ok(ApiResponse.success(
                "Código generado exitosamente",
                Map.of("codigo", codigo)
        ));
    }

    /**
     * Registrar referido
     * POST /api/referidos/registrar
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar referido", description = "Registra un nuevo usuario usando código de referido")
    public ResponseEntity<ApiResponse<Map<String, Object>>> registrarReferido(
            @RequestBody Map<String, String> request) {

        String codigoReferido = request.get("codigoReferido");
        String nuevoUsuarioId = request.get("nuevoUsuarioId");

        Map<String, Object> resultado = referidosService.registrarReferido(
                codigoReferido, nuevoUsuarioId
        );

        return ResponseEntity.ok(ApiResponse.success(
                "Referido registrado exitosamente",
                resultado
        ));
    }

    /**
     * Validar código de referido
     * GET /api/referidos/validar/{codigo}
     */
    @GetMapping("/validar/{codigo}")
    @Operation(summary = "Validar código", description = "Valida si un código de referido existe y es válido")
    public ResponseEntity<ApiResponse<Map<String, Object>>> validarCodigo(
            @PathVariable String codigo) {

        Map<String, Object> validacion = referidosService.validarCodigoReferido(codigo);
        return ResponseEntity.ok(ApiResponse.success(validacion));
    }
}