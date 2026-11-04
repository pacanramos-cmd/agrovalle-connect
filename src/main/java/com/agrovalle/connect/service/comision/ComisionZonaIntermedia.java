package com.agrovalle.connect.service.comision;

import java.math.BigDecimal;
import java.util.Set;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Comisión del 4% para municipios a distancia media, como Dagua y Buga. La tasa es un valor de trabajo del
 * equipo, pendiente de confirmar con el Product Owner.
 */
@Component
@Order(1)
public class ComisionZonaIntermedia extends ComisionPorcentual {

    private static final BigDecimal TASA = new BigDecimal("0.04");
    private static final Set<String> MUNICIPIOS = Set.of(
            "dagua", "buga", "guacarí", "ginebra", "vijes", "la cumbre", "yotoco", "restrepo", "calima",
            "tuluá", "bugalagrande");

    public ComisionZonaIntermedia() {
        super(TASA, MUNICIPIOS);
    }
}
