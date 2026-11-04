package com.agrovalle.connect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Tipo de producto perteneciente a una {@link Categoria} (tabla {@code tipo_producto}).
 *
 * <p>Ejemplos: Mango, Aguacate, Tomate. Cada tipo está en exactamente una categoría
 * (3FN: el nombre de la categoría no se repite en cada producto publicado).
 */
@Entity
@Table(name = "tipo_producto")
public class TipoProducto {

    private static final int LONGITUD_NOMBRE = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = LONGITUD_NOMBRE)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    /** Constructor sin argumentos requerido por JPA. */
    protected TipoProducto() {
    }

    /**
     * Crea un tipo de producto asociado a una categoría.
     *
     * @param nombre    nombre único del tipo de producto
     * @param categoria categoría a la que pertenece
     */
    public TipoProducto(String nombre, Categoria categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}
