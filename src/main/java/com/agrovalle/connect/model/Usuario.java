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
 * Datos de acceso comunes a agricultores y compradores (tabla {@code usuario}). Se crea en HU-01 y se usa
 * para autenticar en HU-13. La contraseña se guarda siempre cifrada (BCrypt), nunca en texto plano.
 */
@Entity
@Table(
    name = "usuario",
    uniqueConstraints = @UniqueConstraint(name = "uq_usuario_correo", columnNames = "correo"))
public class Usuario {

    private static final int LONGITUD_NOMBRE = 120;
    private static final int LONGITUD_CORREO = 150;
    private static final int LONGITUD_HASH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = LONGITUD_NOMBRE)
    private String nombre;

    @Column(name = "correo", nullable = false, length = LONGITUD_CORREO)
    private String correo;

    @Column(name = "contrasena_hash", nullable = false, length = LONGITUD_HASH)
    private String contrasenaHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    /** Constructor sin argumentos requerido por JPA. */
    protected Usuario() {
    }

    /**
     * Crea un usuario nuevo.
     *
     * @param nombre nombre completo
     * @param correo correo en minúsculas, único
     * @param contrasenaHash contraseña ya cifrada con BCrypt
     * @param rol rol asignado
     */
    public Usuario(String nombre, String correo, String contrasenaHash, Rol rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenaHash = contrasenaHash;
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

    public String getCorreo() {
        return correo;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public Rol getRol() {
        return rol;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }
}
