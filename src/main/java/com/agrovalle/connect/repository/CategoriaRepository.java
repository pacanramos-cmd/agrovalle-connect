package com.agrovalle.connect.repository;

import com.agrovalle.connect.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos del catálogo de categorías.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
