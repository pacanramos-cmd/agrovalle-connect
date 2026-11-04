package com.agrovalle.connect.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Observador del evento {@link ProductoPublicadoEvent}: deja trazabilidad de cada publicación sin que
 * {@code ProductoService} conozca a quien reacciona. Otros observadores (notificaciones de HU-12) se agregan
 * sin modificar el servicio.
 */
@Component
public class ProductoPublicadoListener {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ProductoPublicadoListener.class);

    /**
     * Registra la publicación.
     *
     * @param evento datos de la publicación
     */
    @EventListener
    public void alPublicarProducto(ProductoPublicadoEvent evento) {
        REGISTRO.info("Producto {} ({}) publicado por el agricultor {}", evento.productoId(),
                evento.tipoProducto(), evento.agricultorId());
    }
}
