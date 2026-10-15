package com.agrovalle.connect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Catálogo de roles de la plataforma (tabla {@code rol}). El rol viaja en el token JWT de HU-13.
 */
@Entity
@Table(name = "rol")
public class Rol {

    private static final int LONGITUD_NOMBRE = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = LONGITUD_NOMBRE)
    private String nombre;

    /** Constructor sin argumentos requerido por JPA. */
    protected Rol() {
    }

    /**
     * Crea un rol nuevo.
     *
     * @param nombre nombre único del rol, por ejemplo {@code AGRICULTOR}
     */
    public Rol(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
