package com.agrovalle.connect.mapper;

import com.agrovalle.connect.dto.ProductoRequest;
import com.agrovalle.connect.dto.ProductoResponse;
import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Producto;
import com.agrovalle.connect.model.TipoProducto;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Convierte entre los DTO de productos y la entidad {@link Producto} (patrones Data Mapper y Factory Method).
 */
@Component
public class ProductoMapper {

    /**
     * Construye la oferta a partir de la solicitud y de las entidades ya resueltas por el servicio.
     *
     * @param solicitud datos recibidos
     * @param agricultor productor que publica
     * @param tipo tipo de producto del catálogo
     * @return el producto listo para guardar
     */
    public Producto aEntidad(ProductoRequest solicitud, Agricultor agricultor, TipoProducto tipo) {
        return new Producto(agricultor, tipo, solicitud.cantidad(), solicitud.unidadMedida().name(),
                solicitud.precioUnitario(), solicitud.fechaCosecha());
    }

    /**
     * Convierte la entidad en la respuesta de la API.
     *
     * @param producto entidad guardada
     * @param comisionEstimada comisión calculada por la estrategia del municipio
     * @return la respuesta pública
     */
    public ProductoResponse aRespuesta(Producto producto, BigDecimal comisionEstimada) {
        return new ProductoResponse(producto.getId(), producto.getTipoProducto().getNombre(),
                producto.getTipoProducto().getCategoria().getNombre(),
                producto.getAgricultor().getMunicipio().getNombre(), producto.getCantidadDisponible(),
                producto.getUnidadMedida(), producto.getPrecioUnitario(), producto.getFechaCosecha(),
                comisionEstimada);
    }
}
