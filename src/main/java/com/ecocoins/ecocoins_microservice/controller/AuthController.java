package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.dto.LoginRequest;
import com.ecocoins.ecocoins_microservice.dto.RegisterRequest;
import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro y autenticación con Firebase")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ✅ CORREGIDO: Registrar usuario usando RegisterRequest
     * POST /api/auth/register
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en MongoDB con Firebase UID")
    public ResponseEntity<ApiResponse<Usuario>> registrarUsuario(
            @Valid @RequestBody RegisterRequest request) {

        log.info("📝 Solicitud de registro recibida");

        String correo = request.getCorreo();
        String nombre = request.getNombre();
        String contrasenia = request.getContrasenia();
        String carrera = request.getCarrera();
        String firebaseUid = request.getFirebaseUid();

        // Verificar si el correo ya existe
        if (usuarioRepository.findByCorreo(correo.toLowerCase()).isPresent()) {
            log.warn("⚠️ Intento de registro con correo duplicado: {}", correo);
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Este correo electrónico ya está registrado"));
        }

        try {
            // Crear nuevo usuario en MongoDB
            Usuario usuario = new Usuario();
            usuario.setCorreo(correo.toLowerCase());
            usuario.setNombre(nombre);
            usuario.setContrasenia(passwordEncoder.encode(contrasenia));
            usuario.setCarrera(carrera != null ? carrera : "No especificada");
            usuario.setFirebaseUid(firebaseUid);
            usuario.setEstado("activo");
            usuario.setRol("usuario");
            usuario.setEcoCoins(0);
            usuario.setNivel(1);
            usuario.setTotalReciclajes(0);
            usuario.setTotalKgReciclados(0.0);

            Usuario saved = usuarioRepository.save(usuario);

            log.info("✅ Usuario registrado exitosamente - Email: {}", correo);

            // Ocultar contraseña antes de enviar respuesta
            saved.setContrasenia(null);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Usuario registrado exitosamente", saved));

        } catch (Exception e) {
            log.error("❌ Error al registrar usuario: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al registrar usuario: " + e.getMessage()));
        }
    }

    /**
     * ✅ CORREGIDO: Login de usuario usando LoginRequest
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con email y contraseña")
    public ResponseEntity<ApiResponse<Usuario>> login(
            @Valid @RequestBody LoginRequest request) {

        log.info("🔐 Solicitud de login recibida");

        String correo = request.getCorreo();
        String contrasenia = request.getContrasenia();

        try {
            // Buscar usuario por correo
            Usuario usuario = usuarioRepository.findByCorreo(correo.toLowerCase())
                    .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

            // Verificar contraseña
            if (!passwordEncoder.matches(contrasenia, usuario.getContrasenia())) {
                log.warn("⚠️ Intento de login fallido para: {}", correo);
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Credenciales inválidas"));
            }

            // Verificar estado del usuario
            if (!"activo".equals(usuario.getEstado())) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Usuario inactivo o bloqueado"));
            }

            log.info("✅ Login exitoso - Email: {}", correo);

            // Ocultar contraseña
            usuario.setContrasenia(null);

            return ResponseEntity.ok(ApiResponse.success("Login exitoso", usuario));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Credenciales inválidas"));

        } catch (Exception e) {
            log.error("❌ Error en login: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al iniciar sesión"));
        }
    }

    /**
     * Sincronizar usuario de Firebase con MongoDB
     * POST /api/auth/sync
     */
    @PostMapping("/sync")
    @Operation(summary = "Sincronizar usuario", description = "Crea o actualiza usuario en MongoDB después del registro en Firebase")
    public ResponseEntity<ApiResponse<Usuario>> sincronizarUsuario(
            @RequestBody Map<String, String> request) {

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
     * GET /api/auth/perfil
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
     * Obtener perfil del usuario por ID
     * GET /api/auth/perfil/{usuarioId}
     */
    @GetMapping("/perfil/{usuarioId}")
    @Operation(summary = "Obtener perfil por ID", description = "Obtiene los datos de un usuario por su ID")
    public ResponseEntity<ApiResponse<Usuario>> obtenerPerfilPorId(
            @PathVariable String usuarioId) {

        try {
            log.info("🔍 Buscando perfil - ID: {}", usuarioId);

            // Buscar usuario en MongoDB por ID
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Usuario no encontrado con ID: " + usuarioId));

            // Ocultar contraseña
            usuario.setContrasenia(null);

            return ResponseEntity.ok(ApiResponse.success(usuario));

        } catch (ResourceNotFoundException e) {
            log.error("❌ Usuario no encontrado: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            log.error("❌ Error al obtener perfil: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener perfil: " + e.getMessage()));
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
                ApiResponse.success("✅ Servicio de autenticación funcionando")
        );
    }
}
