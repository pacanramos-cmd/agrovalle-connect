package com.agrovalle.connect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

/**
 * Agricultor registrado en la plataforma (tabla {@code agricultor}). Se crea en HU-01 y se usa para
 * autenticar en HU-13. La contraseña se guarda siempre cifrada (BCrypt), nunca en texto plano.
 */
@Entity
@Table(
    name = "agricultor",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_agricultor_cedula", columnNames = "cedula"),
        @UniqueConstraint(name = "uq_agricultor_correo", columnNames = "correo")
    })
public class Agricultor {

    private static final int LONGITUD_NOMBRE = 120;
    private static final int LONGITUD_CEDULA = 10;
    private static final int LONGITUD_CORREO = 150;
    private static final int LONGITUD_HASH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = LONGITUD_NOMBRE)
    private String nombre;

    @Column(name = "cedula", nullable = false, length = LONGITUD_CEDULA)
    private String cedula;

    @Column(name = "correo", nullable = false, length = LONGITUD_CORREO)
    private String correo;

    @Column(name = "contrasena_hash", nullable = false, length = LONGITUD_HASH)
    private String contrasenaHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipio_id", nullable = false)
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    /** Constructor sin argumentos requerido por JPA. */
    protected Agricultor() {
    }

    /**
     * Crea un agricultor nuevo.
     *
     * @param nombre nombre completo
     * @param cedula cédula, de 6 a 10 dígitos, única
     * @param correo correo en minúsculas, único
     * @param contrasenaHash contraseña ya cifrada con BCrypt
     * @param municipio municipio del Valle del Cauca donde se ubica
     * @param rol rol asignado
     */
    public Agricultor(String nombre, String cedula, String correo, String contrasenaHash,
            Municipio municipio, Rol rol) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
        this.contrasenaHash = contrasenaHash;
        this.municipio = municipio;
        this.rol = rol;
    }

    @PrePersist
    void asignarFechaCreacion() {
        if (creadoEn == null) {
            creadoEn = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public Rol getRol() {
        return rol;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }
}
