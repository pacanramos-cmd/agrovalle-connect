package com.agrovalle.connect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada para el inicio de sesión (HU-13).
 *
 * @param correo    correo electrónico registrado en la plataforma
 * @param contrasena contraseña en texto plano; el sistema la compara contra el hash BCrypt
 */
public record LoginRequest(
        @NotBlank @Email @Size(max = ReglasValidacion.LONGITUD_CORREO) String correo,
        @NotBlank @Size(
                min = ReglasValidacion.CONTRASENA_MINIMA,
                max = ReglasValidacion.CONTRASENA_MAXIMA)
        String contrasena) {

    /** Oculta la contraseña del texto de log. */
    @Override
    public String toString() {
        return "LoginRequest[correo=" + correo + ", contrasena=***]";
    }
}
