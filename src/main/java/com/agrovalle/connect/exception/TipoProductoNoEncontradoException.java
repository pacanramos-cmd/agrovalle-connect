package com.agrovalle.connect.exception;

/**
 * Se lanza cuando el tipo de producto indicado no existe en el catálogo (HU-02).
 */
public class TipoProductoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TipoProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
