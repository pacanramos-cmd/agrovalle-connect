package com.agrovalle.connect.exception;

/**
 * Se lanza cuando el municipio indicado en el registro no existe en el catálogo del Valle del Cauca.
 */
public class MunicipioNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MunicipioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
