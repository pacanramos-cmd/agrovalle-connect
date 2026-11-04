package com.agrovalle.connect.repository;

import com.agrovalle.connect.model.TipoProducto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos del catálogo de tipos de producto.
 */
@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long> {

    Optional<TipoProducto> findByNombreIgnoreCase(String nombre);
}
