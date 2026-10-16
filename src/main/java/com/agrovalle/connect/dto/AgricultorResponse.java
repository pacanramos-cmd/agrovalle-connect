package com.agrovalle.connect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Datos de salida del registro de agricultores. Nunca incluye la contraseña ni su hash.
 *
 * @param id identificador generado por la base de datos
 * @param nombre nombre completo del agricultor
 * @param cedula cédula del agricultor
 * @param correo correo en minúsculas
 * @param ubicacionValle nombre del municipio (campo JSON {@code ubicacion_valle})
 */
public record AgricultorResponse(
        Long id,
        String nombre,
        String cedula,
        String correo,
        @JsonProperty("ubicacion_valle") String ubicacionValle) {
}
