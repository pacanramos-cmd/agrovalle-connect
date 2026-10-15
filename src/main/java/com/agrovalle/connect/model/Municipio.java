package com.agrovalle.connect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Catálogo de municipios del Valle del Cauca (tabla {@code municipio}). Evita repetir el texto de
 * la ubicación en cada agricultor, lo que mantiene el modelo en tercera forma normal.
 */
@Entity
@Table(name = "municipio")
public class Municipio {

    private static final int LONGITUD_NOMBRE = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = LONGITUD_NOMBRE)
    private String nombre;

    /** Constructor sin argumentos requerido por JPA. */
    protected Municipio() {
    }

    /**
     * Crea un municipio nuevo.
     *
     * @param nombre nombre único del municipio, por ejemplo {@code Dagua}
     */
    public Municipio(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
