package com.agrovalle.connect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Oferta publicada, tal como la ve el cliente de la API (HU-02 y HU-04).
 *
 * @param id identificador del producto
 * @param tipo nombre del tipo de producto
 * @param categoria categoría del producto
 * @param municipio municipio del agricultor
 * @param cantidad cantidad disponible
 * @param unidadMedida unidad de medida (campo JSON {@code unidad_medida})
 * @param precioUnitario precio por unidad en COP (campo JSON {@code precio_unitario})
 * @param fechaCosecha fecha de cosecha (campo JSON {@code fecha_cosecha})
 * @param comisionEstimada comisión estimada de la plataforma sobre la venta total (campo JSON
 *     {@code comision_estimada})
 */
public record ProductoResponse(
        Long id,
        String tipo,
        String categoria,
        String municipio,
        BigDecimal cantidad,
        @JsonProperty("unidad_medida") String unidadMedida,
        @JsonProperty("precio_unitario") BigDecimal precioUnitario,
        @JsonProperty("fecha_cosecha") LocalDate fechaCosecha,
        @JsonProperty("comision_estimada") BigDecimal comisionEstimada) {
}
