package com.agrovalle.connect.exception;

/**
 * Se lanza cuando el usuario autenticado no tiene permiso para la operación (por ejemplo, no es agricultor).
 */
public class AccesoDenegadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
