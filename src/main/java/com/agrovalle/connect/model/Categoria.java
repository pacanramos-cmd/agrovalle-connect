package com.agrovalle.connect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Catálogo de categorías de productos agrícolas (tabla {@code categoria}).
 *
 * <p>Ejemplos: Frutas, Verduras, Tubérculos, Granos y cereales, Café.
 * Las categorías son la base del filtro de HU-04.
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    private static final int LONGITUD_NOMBRE = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = LONGITUD_NOMBRE)
    private String nombre;

    /** Constructor sin argumentos requerido por JPA. */
    protected Categoria() {
    }

    /**
     * Crea una categoría de producto.
     *
     * @param nombre nombre único de la categoría
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
