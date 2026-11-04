package com.agrovalle.connect.exception;

/**
 * Excepción lanzada cuando se solicita un producto inexistente o sin disponibilidad.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepción con el identificador del producto no encontrado.
     *
     * @param id identificador del producto
     */
    public ProductoNoEncontradoException(Long id) {
        super("Producto no encontrado: " + id);
    }

    /**
     * Crea la excepción con un mensaje personalizado.
     *
     * @param mensaje descripción del problema
     */
    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
