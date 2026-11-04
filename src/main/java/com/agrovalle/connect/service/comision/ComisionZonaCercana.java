package com.agrovalle.connect.service.comision;

import java.math.BigDecimal;
import java.util.Set;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Comisión del 3% para municipios cercanos a Cali, donde el flete es corto. La tasa es un valor de trabajo
 * del equipo, pendiente de confirmar con el Product Owner.
 */
@Component
@Order(1)
public class ComisionZonaCercana extends ComisionPorcentual {

    private static final BigDecimal TASA = new BigDecimal("0.03");
    private static final Set<String> MUNICIPIOS = Set.of(
            "cali", "palmira", "yumbo", "jamundí", "candelaria", "florida", "pradera");

    public ComisionZonaCercana() {
        super(TASA, MUNICIPIOS);
    }
}
