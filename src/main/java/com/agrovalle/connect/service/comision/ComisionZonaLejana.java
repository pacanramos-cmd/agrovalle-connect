package com.agrovalle.connect.service.comision;

import java.math.BigDecimal;
import java.util.Set;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Comisión del 5% para el resto de municipios del Valle del Cauca. Atiende cualquier municipio, por lo que se
 * evalúa de último y funciona como estrategia por defecto.
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ComisionZonaLejana extends ComisionPorcentual {

    private static final BigDecimal TASA = new BigDecimal("0.05");

    public ComisionZonaLejana() {
        super(TASA, Set.of());
    }

    @Override
    public boolean aplicaA(String municipio) {
        return true;
    }
}
