package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.exception.ConflictException;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
// ⭐ ELIMINADA LA LÍNEA: @CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para registro y autenticación con Firebase")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Sincronizar usuario de Firebase con MongoDB
     * POST /api/auth/sync
     */
    @PostMapping("/sync")
    @Operation(summary = "Sincronizar usuario", description = "Crea o actualiza usuario en MongoDB después del registro en Firebase")
    public ResponseEntity<ApiResponse<Usuario>> sincronizarUsuario(
            @RequestBody Map<String, String> request) {

        // ⭐ LOG TEMPORAL PARA CONFIRMAR QUE EL CÓDIGO SE ACTUALIZÓ
        System.out.println("🟢🟢🟢 BACKEND ACTUALIZADO - CORS CORREGIDO 🟢🟢🟢");

        String firebaseUid = request.get("firebaseUid");
        String email = request.get("email");
        String nombre = request.get("nombre");
        String carrera = request.get("carrera");

        log.info("📝 Sincronizando usuario - UID: {}, Email: {}", firebaseUid, email);

        // Verificar si ya existe por UID
        Usuario usuario = usuarioRepository.findByFirebaseUid(firebaseUid)
                .orElse(null);

        if (usuario == null) {
            // Verificar si existe por correo (migración)
            usuario = usuarioRepository.findByCorreo(email.toLowerCase())
                    .orElse(null);

            if (usuario != null) {
                // Usuario existente, actualizar UID
                log.info("🔄 Usuario existente encontrado, actualizando UID");
                usuario.setFirebaseUid(firebaseUid);
            } else {
                // Crear nuevo usuario
                log.info("➕ Creando nuevo usuario");
                usuario = new Usuario();
                usuario.setFirebaseUid(firebaseUid);
                usuario.setCorreo(email.toLowerCase());
                usuario.setNombre(nombre);
                usuario.setCarrera(carrera);
                usuario.setEstado("activo");
                usuario.setRol("usuario");
                usuario.setEcoCoins(0);
                usuario.setNivel(1);
                usuario.setTotalReciclajes(0);
                usuario.setTotalKgReciclados(0.0);
            }
        } else {
            // Usuario ya existe, actualizar datos si es necesario
            log.info("✅ Usuario ya sincronizado");
            if (nombre != null) usuario.setNombre(nombre);
            if (carrera != null) usuario.setCarrera(carrera);
        }

        Usuario saved = usuarioRepository.save(usuario);

        log.info("✅ Usuario sincronizado exitosamente - ID: {}", saved.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usuario sincronizado exitosamente", saved));
    }

    /**
     * Obtener perfil del usuario autenticado
     * GET /api/usuarios/perfil
     */
    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil", description = "Obtiene los datos del usuario autenticado")
    public ResponseEntity<ApiResponse<Usuario>> obtenerPerfil(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7); // Remover "Bearer "

            // Verificar token con Firebase
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();

            log.info("🔍 Buscando perfil - UID: {}", uid);

            // Buscar usuario en MongoDB
            Usuario usuario = usuarioRepository.findByFirebaseUid(uid)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Usuario no encontrado. Por favor, sincroniza tu cuenta."));

            return ResponseEntity.ok(ApiResponse.success(usuario));

        } catch (ResourceNotFoundException e) {
            log.error("❌ Usuario no encontrado: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            log.error("❌ Error al obtener perfil: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token inválido o expirado"));
        }
    }

    /**
     * Health check
     * GET /api/auth/health
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica que el servicio esté funcionando")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
                ApiResponse.success("✅ Servicio de autenticación funcionando con Firebase")
        );
    }
}