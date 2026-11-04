package com.agrovalle.connect.repository;

import com.agrovalle.connect.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos de usuarios (registro de HU-01 e inicio de sesión de HU-13).
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreo(String correo);

    Optional<Usuario> findByCorreo(String correo);
}
