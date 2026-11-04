package com.agrovalle.connect.exception;

/**
 * Se lanza cuando la fecha de cosecha de un producto es anterior a la fecha actual (HU-02).
 */
public class FechaCosechaInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FechaCosechaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
