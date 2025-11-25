package com.ecocoins.ecocoins_microservice.repository;

import com.ecocoins.ecocoins_microservice.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByFirebaseUid(String firebaseUid);
}
