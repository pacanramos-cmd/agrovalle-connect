package com.agrovalle.connect.dto;

/**
 * Límites y formatos de validación compartidos por los DTO. Coinciden con las columnas y las
 * restricciones de {@code schema-agrovalle.sql}.
 */
public final class ReglasValidacion {

    public static final int LONGITUD_NOMBRE = 120;
    public static final int LONGITUD_MUNICIPIO = 60;
    public static final int LONGITUD_CORREO = 150;
    public static final int CONTRASENA_MINIMA = 8;
    /** BCrypt solo usa los primeros 72 bytes de la contraseña. */
    public static final int CONTRASENA_MAXIMA = 72;
    public static final String CEDULA_REGEX = "^[0-9]{6,10}$";

    private ReglasValidacion() {
    }
}
