package com.agrovalle.connect.dto;

import com.agrovalle.connect.model.UnidadMedida;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Datos de entrada para publicar un producto (HU-02).
 *
 * @param tipo nombre de un tipo de producto del catálogo
 * @param cantidad cantidad disponible, mayor que cero
 * @param unidadMedida unidad de medida (campo JSON {@code unidad_medida})
 * @param precioUnitario precio por unidad en COP, mayor que cero (campo JSON {@code precio_unitario})
 * @param fechaCosecha fecha de cosecha (campo JSON {@code fecha_cosecha})
 */
public record ProductoRequest(
        @NotBlank String tipo,
        @NotNull @DecimalMin(value = "0.01") BigDecimal cantidad,
        @NotNull @JsonProperty("unidad_medida") UnidadMedida unidadMedida,
        @NotNull @DecimalMin(value = "0.01") @JsonProperty("precio_unitario") BigDecimal precioUnitario,
        @NotNull @JsonProperty("fecha_cosecha") LocalDate fechaCosecha) {
}
