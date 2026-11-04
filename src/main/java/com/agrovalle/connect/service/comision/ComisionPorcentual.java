package com.agrovalle.connect.service.comision;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Set;

/**
 * Base de las estrategias que cobran un porcentaje fijo sobre la venta. Todas las subclases cumplen el mismo
 * contrato de {@link CalculadorComisionService} y son intercambiables (principio de sustitución de Liskov).
 */
public abstract class ComisionPorcentual implements CalculadorComisionService {

    private static final int ESCALA = 2;

    private final BigDecimal tasa;
    private final Set<String> municipios;

    /**
     * Crea la estrategia.
     *
     * @param tasa porcentaje expresado como fracción, por ejemplo {@code 0.03} para 3%
     * @param municipios municipios que atiende, en minúsculas
     */
    protected ComisionPorcentual(BigDecimal tasa, Set<String> municipios) {
        this.tasa = tasa;
        this.municipios = municipios;
    }

    @Override
    public boolean aplicaA(String municipio) {
        return municipio != null && municipios.contains(municipio.trim().toLowerCase(Locale.ROOT));
    }

    @Override
    public BigDecimal calcular(BigDecimal valorVenta) {
        return valorVenta.multiply(tasa).setScale(ESCALA, RoundingMode.HALF_UP);
    }
}
