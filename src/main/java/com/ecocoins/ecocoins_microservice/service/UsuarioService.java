package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setCorreo(usuario.getCorreo().toLowerCase().trim());

        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("❌ El correo ya está registrado.");
        }

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("usuario");
        }
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("activo");
        }

        usuario.setEcoCoins(0);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> iniciarSesion(String correo, String contrasenia) {
        return usuarioRepository.findByCorreo(correo.toLowerCase().trim())
                .filter(u -> u.getContrasenia().equals(contrasenia));
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
}
