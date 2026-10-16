package com.agrovalle.connect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada del registro de agricultores (HU-01).
 *
 * @param nombre nombre completo del agricultor
 * @param ubicacionValle nombre de un municipio del Valle del Cauca (campo JSON {@code ubicacion_valle})
 * @param cedula cédula de 6 a 10 dígitos, única
 * @param correo correo electrónico, único
 * @param contrasena contraseña de 8 a 72 caracteres; se cifra antes de guardarla
 */
public record RegistroAgricultorRequest(
        @NotBlank @Size(max = ReglasValidacion.LONGITUD_NOMBRE) String nombre,
        @NotBlank @Size(max = ReglasValidacion.LONGITUD_MUNICIPIO)
        @JsonProperty("ubicacion_valle") String ubicacionValle,
        @NotBlank @Pattern(regexp = ReglasValidacion.CEDULA_REGEX) String cedula,
        @NotBlank @Email @Size(max = ReglasValidacion.LONGITUD_CORREO) String correo,
        @NotBlank @Size(min = ReglasValidacion.CONTRASENA_MINIMA, max = ReglasValidacion.CONTRASENA_MAXIMA)
        String contrasena) {

    /** Omite la contraseña para que nunca aparezca en registros de log. */
    @Override
    public String toString() {
        return "RegistroAgricultorRequest[nombre=" + nombre + ", ubicacionValle=" + ubicacionValle
                + ", cedula=" + cedula + ", correo=" + correo + ", contrasena=***]";
    }
}
