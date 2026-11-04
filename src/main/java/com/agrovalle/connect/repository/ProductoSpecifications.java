package com.agrovalle.connect.repository;

import com.agrovalle.connect.model.Producto;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

/**
 * Criterios de consulta reutilizables sobre {@link Producto}. Permiten combinar solo los filtros que el
 * comprador envía (patrón Specification), sin escribir una consulta por cada combinación.
 */
public final class ProductoSpecifications {

    private ProductoSpecifications() {
    }

    /**
     * Solo ofertas activas.
     *
     * @return el criterio de actividad
     */
    public static Specification<Producto> activo() {
        return (raiz, consulta, constructor) -> constructor.isTrue(raiz.get("activo"));
    }

    /**
     * Ofertas cuyo agricultor está en el municipio indicado, sin distinguir mayúsculas.
     *
     * @param municipio nombre del municipio
     * @return el criterio por municipio
     */
    public static Specification<Producto> deMunicipio(String municipio) {
        return (raiz, consulta, constructor) -> constructor.equal(
                constructor.lower(raiz.get("agricultor").get("municipio").get("nombre")),
                municipio.toLowerCase(Locale.ROOT));
    }

    /**
     * Ofertas cuyo tipo de producto pertenece a la categoría indicada, sin distinguir mayúsculas.
     *
     * @param categoria nombre de la categoría
     * @return el criterio por categoría
     */
    public static Specification<Producto> deCategoria(String categoria) {
        return (raiz, consulta, constructor) -> constructor.equal(
                constructor.lower(raiz.get("tipoProducto").get("categoria").get("nombre")),
                categoria.toLowerCase(Locale.ROOT));
    }
}
