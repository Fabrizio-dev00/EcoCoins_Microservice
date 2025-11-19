package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import com.ecocoins.ecocoins_microservice.exception.ConflictException;
import com.ecocoins.ecocoins_microservice.util.ValidationUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario registrarUsuario(Usuario usuario) {
        // Validaciones
        ValidationUtil.validarNombre(usuario.getNombre());
        ValidationUtil.validarCorreoInstitucional(usuario.getCorreo());
        ValidationUtil.validarContrasenia(usuario.getContrasenia());

        usuario.setCorreo(usuario.getCorreo().toLowerCase().trim());

        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new ConflictException("Usuario", "correo", usuario.getCorreo());
        }

        // Encriptar contraseña
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("usuario");
        }
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("activo");
        }

        usuario.setEcoCoins(0);
        usuario.setNivel(1);
        usuario.setTotalReciclajes(0);
        usuario.setTotalKgReciclados(0.0);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> iniciarSesion(String correo, String contrasenia) {
        return usuarioRepository.findByCorreo(correo.toLowerCase().trim())
                .filter(u -> passwordEncoder.matches(contrasenia, u.getContrasenia()));
    }

    public Usuario actualizarEstado(String id, String nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEstado(nuevoEstado);
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado para eliminar");
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario obtenerPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}