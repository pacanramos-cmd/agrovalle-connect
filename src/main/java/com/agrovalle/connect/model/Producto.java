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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Cosecha publicada por un agricultor (tabla {@code producto}, HU-02). Su inventario alimenta HU-07 y sus
 * filtros, HU-04. La finca es opcional y pertenece a HU-06, por lo que todavía no se mapea.
 */
@Entity
@Table(name = "producto")
public class Producto {

    private static final int PRECISION_CANTIDAD = 10;
    private static final int PRECISION_PRECIO = 12;
    private static final int ESCALA = 2;
    private static final int LONGITUD_UNIDAD = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agricultor_id", nullable = false)
    private Agricultor agricultor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_producto_id", nullable = false)
    private TipoProducto tipoProducto;

    @Column(name = "cantidad_disponible", nullable = false, precision = PRECISION_CANTIDAD, scale = ESCALA)
    private BigDecimal cantidadDisponible;

    @Column(name = "unidad_medida", nullable = false, length = LONGITUD_UNIDAD)
    private String unidadMedida;

    @Column(name = "precio_unitario", nullable = false, precision = PRECISION_PRECIO, scale = ESCALA)
    private BigDecimal precioUnitario;

    @Column(name = "fecha_cosecha", nullable = false)
    private LocalDate fechaCosecha;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    /** Constructor sin argumentos requerido por JPA. */
    protected Producto() {
    }

    /**
     * Crea una oferta activa.
     *
     * @param agricultor productor que publica
     * @param tipoProducto tipo de producto del catálogo
     * @param cantidadDisponible cantidad disponible, mayor que cero
     * @param unidadMedida unidad: KG, LIBRA, TONELADA, LITRO o UNIDAD
     * @param precioUnitario precio por unidad en pesos colombianos, mayor que cero
     * @param fechaCosecha fecha de cosecha, no anterior a la fecha de publicación
     */
    public Producto(Agricultor agricultor, TipoProducto tipoProducto, BigDecimal cantidadDisponible,
            String unidadMedida, BigDecimal precioUnitario, LocalDate fechaCosecha) {
        this.agricultor = agricultor;
        this.tipoProducto = tipoProducto;
        this.cantidadDisponible = cantidadDisponible;
        this.unidadMedida = unidadMedida;
        this.precioUnitario = precioUnitario;
        this.fechaCosecha = fechaCosecha;
        this.activo = true;
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

    public Agricultor getAgricultor() {
        return agricultor;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public BigDecimal getCantidadDisponible() {
        return cantidadDisponible;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public LocalDate getFechaCosecha() {
        return fechaCosecha;
    }

    public boolean isActivo() {
        return activo;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }
}
