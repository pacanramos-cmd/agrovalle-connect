package com.agrovalle.connect.repository;

import com.agrovalle.connect.model.Agricultor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos de agricultores. Las consultas se derivan del nombre del método.
 */
@Repository
public interface AgricultorRepository extends JpaRepository<Agricultor, Long> {

    boolean existsByCedula(String cedula);

    boolean existsByCorreo(String correo);

    Optional<Agricultor> findByCorreo(String correo);
}
