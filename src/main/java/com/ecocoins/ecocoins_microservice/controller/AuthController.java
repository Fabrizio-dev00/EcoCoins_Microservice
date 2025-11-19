package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.dto.LoginRequest;
import com.ecocoins.ecocoins_microservice.dto.LoginResponse;
import com.ecocoins.ecocoins_microservice.dto.RegisterRequest;
import com.ecocoins.ecocoins_microservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para registro, login y gestión de tokens JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registrar un nuevo usuario
     * POST /api/auth/register
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea una cuenta nueva y retorna un token JWT")
    public ResponseEntity<ApiResponse<LoginResponse>> registrar(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.registrar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usuario registrado exitosamente", response));
    }

    /**
     * Iniciar sesión
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y retorna un token JWT")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
    }

    /**
     * Validar token
     * GET /api/auth/validate?token=xxx
     */
    @GetMapping("/validate")
    @Operation(summary = "Validar token JWT", description = "Verifica si un token JWT es válido")
    public ResponseEntity<ApiResponse<Boolean>> validarToken(@RequestParam String token) {
        boolean valido = authService.validarToken(token);

        if (valido) {
            return ResponseEntity.ok(ApiResponse.success("Token válido", true));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token inválido o expirado", false));
        }
    }

    /**
     * Endpoint de prueba (público)
     * GET /api/auth/health
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica que el servicio de autenticación está funcionando")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
                ApiResponse.success("Servicio de autenticación funcionando correctamente ✅")
        );
    }
}