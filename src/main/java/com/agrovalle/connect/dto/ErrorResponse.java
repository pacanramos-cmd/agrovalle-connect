package com.agrovalle.connect.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Respuesta de error estándar para los códigos 4xx y 5xx.
 *
 * <p>El campo {@code detalles} solo se serializa cuando no es nulo, para evitar
 * ruido en errores simples.
 *
 * @param estado   código de estado HTTP
 * @param error    descripción corta del error
 * @param detalles lista de mensajes de validación por campo (puede ser {@code null})
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int estado,
        String error,
        List<String> detalles) {

    /**
     * Crea una respuesta de error sin detalles de campo.
     *
     * @param estado código HTTP
     * @param error  descripción del error
     * @return instancia sin detalles
     */
    public static ErrorResponse of(int estado, String error) {
        return new ErrorResponse(estado, error, null);
    }

    /**
     * Crea una respuesta de error con lista de detalles de validación.
     *
     * @param estado   código HTTP
     * @param error    descripción del error
     * @param detalles lista de mensajes por campo
     * @return instancia con detalles
     */
    public static ErrorResponse of(int estado, String error, List<String> detalles) {
        return new ErrorResponse(estado, error, detalles);
    }
}
