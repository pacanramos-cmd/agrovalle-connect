package com.agrovalle.connect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Perfil de productor (tabla {@code agricultor}). Es una relación 1 a 1 con {@link Usuario}: comparte su
 * identificador ({@code usuario_id}) como llave primaria y foránea a la vez.
 */
@Entity
@Table(
    name = "agricultor",
    uniqueConstraints = @UniqueConstraint(name = "uq_agricultor_cedula", columnNames = "cedula"))
public class Agricultor {

    private static final int LONGITUD_CEDULA = 10;

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "cedula", nullable = false, length = LONGITUD_CEDULA)
    private String cedula;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipio_id", nullable = false)
    private Municipio municipio;

    /** Constructor sin argumentos requerido por JPA. */
    protected Agricultor() {
    }

    /**
     * Crea el perfil de un agricultor.
     *
     * @param usuario usuario ya guardado al que pertenece el perfil
     * @param cedula cédula de 6 a 10 dígitos, única
     * @param municipio municipio del Valle del Cauca donde se ubica
     */
    public Agricultor(Usuario usuario, String cedula, Municipio municipio) {
        this.usuario = usuario;
        this.cedula = cedula;
        this.municipio = municipio;
    }

    /**
     * Identificador del agricultor, igual al del usuario asociado.
     *
     * @return la llave primaria compartida
     */
    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return usuario.getNombre();
    }

    public String getCorreo() {
        return usuario.getCorreo();
    }

    public String getCedula() {
        return cedula;
    }

    public Municipio getMunicipio() {
        return municipio;
    }
}
