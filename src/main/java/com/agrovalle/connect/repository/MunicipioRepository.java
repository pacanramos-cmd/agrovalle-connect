package com.agrovalle.connect.repository;

import com.agrovalle.connect.model.Municipio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos del catálogo de municipios.
 */
@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    Optional<Municipio> findByNombreIgnoreCase(String nombre);
}
