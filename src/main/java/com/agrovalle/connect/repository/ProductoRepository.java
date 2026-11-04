package com.agrovalle.connect.repository;

import com.agrovalle.connect.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos de productos. Los filtros opcionales de HU-04 se componen con
 * {@link ProductoSpecifications}.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
}
