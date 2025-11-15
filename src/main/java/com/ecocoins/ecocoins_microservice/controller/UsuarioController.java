package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }


    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String contrasenia = body.get("contrasenia");

        return usuarioService.iniciarSesion(correo, contrasenia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body((Usuario) Map.of("mensaje", "Credenciales inválidas")));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Usuario> cambiarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
        Usuario actualizado = usuarioService.actualizarEstado(id, body.get("estado"));
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
