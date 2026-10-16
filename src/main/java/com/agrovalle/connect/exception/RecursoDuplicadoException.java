package com.agrovalle.connect.exception;

/**
 * Se lanza cuando se intenta registrar un dato que debe ser único (cédula o correo) y ya existe.
 */
public class RecursoDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
