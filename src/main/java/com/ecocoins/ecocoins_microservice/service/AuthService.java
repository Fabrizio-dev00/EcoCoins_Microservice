package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.dto.LoginRequest;
import com.ecocoins.ecocoins_microservice.dto.LoginResponse;
import com.ecocoins.ecocoins_microservice.dto.RegisterRequest;
import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.ecocoins.ecocoins_microservice.exception.ConflictException;
import com.ecocoins.ecocoins_microservice.exception.UnauthorizedException;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import com.ecocoins.ecocoins_microservice.security.JwtService;
import com.ecocoins.ecocoins_microservice.util.ValidationUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Registrar un nuevo usuario
     */
    public LoginResponse registrar(RegisterRequest request) {
        // Validaciones
        ValidationUtil.validarNombre(request.getNombre());
        ValidationUtil.validarCorreoInstitucional(request.getCorreo());
        ValidationUtil.validarContrasenia(request.getContrasenia());
        ValidationUtil.validarTelefono(request.getTelefono());

        // Verificar si el correo ya existe
        if (usuarioRepository.findByCorreo(request.getCorreo().toLowerCase()).isPresent()) {
            throw new ConflictException("Usuario", "correo", request.getCorreo());
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo().toLowerCase().trim());
        usuario.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        usuario.setRol("usuario");
        usuario.setEstado("activo");
        usuario.setEcoCoins(0);
        usuario.setConfirmado(true);
        usuario.setCarrera(request.getCarrera());
        usuario.setTelefono(request.getTelefono());
        usuario.setNivel(1); // Bronce
        usuario.setTotalReciclajes(0);
        usuario.setTotalKgReciclados(0.0);

        // Guardar en BD
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Generar token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUsuario.getCorreo());
        String token = jwtService.generateToken(userDetails);

        // Preparar respuesta
        return new LoginResponse(
                token,
                savedUsuario.getId(),
                savedUsuario.getNombre(),
                savedUsuario.getCorreo(),
                savedUsuario.getRol(),
                savedUsuario.getEcoCoins()
        );
    }

    /**
     * Iniciar sesión
     */
    public LoginResponse login(LoginRequest request) {
        // Validaciones básicas
        if (request.getCorreo() == null || request.getCorreo().trim().isEmpty()) {
            throw new BadRequestException("El correo es obligatorio");
        }

        if (request.getContrasenia() == null || request.getContrasenia().isEmpty()) {
            throw new BadRequestException("La contraseña es obligatoria");
        }

        String correo = request.getCorreo().toLowerCase().trim();

        try {
            // Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correo, request.getContrasenia())
            );

            // Si llega aquí, la autenticación fue exitosa
            Usuario usuario = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

            // Verificar estado del usuario
            if (!"activo".equals(usuario.getEstado())) {
                throw new UnauthorizedException("Tu cuenta está suspendida. Contacta al administrador.");
            }

            // Generar token JWT
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            // Preparar respuesta
            return new LoginResponse(
                    token,
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getCorreo(),
                    usuario.getRol(),
                    usuario.getEcoCoins()
            );

        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Correo o contraseña incorrectos");
        }
    }

    /**
     * Validar token (útil para verificar si un token sigue siendo válido)
     */
    public boolean validarToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.isTokenValid(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }
}