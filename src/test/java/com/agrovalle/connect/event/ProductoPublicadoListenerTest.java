package com.agrovalle.connect.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

/**
 * Prueba del observador del evento de publicación de productos.
 */
class ProductoPublicadoListenerTest {

    @Test
    void registraLaPublicacionSinFallar() {
        ProductoPublicadoListener observador = new ProductoPublicadoListener();

        assertDoesNotThrow(() -> observador.alPublicarProducto(new ProductoPublicadoEvent(1L, 7L, "Mango")));
    }
}
