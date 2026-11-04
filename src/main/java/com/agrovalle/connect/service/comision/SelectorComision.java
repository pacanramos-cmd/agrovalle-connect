package com.agrovalle.connect.service.comision;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Contexto del patrón Strategy: elige la primera estrategia que atiende el municipio y calcula la comisión.
 * Depende de la abstracción {@link CalculadorComisionService}, no de las clases concretas (inversión de
 * dependencias).
 */
@Service
public class SelectorComision {

    private final List<CalculadorComisionService> estrategias;

    /**
     * Crea el selector con las estrategias registradas en Spring, en orden de prioridad.
     *
     * @param estrategias estrategias disponibles
     */
    @Autowired
    public SelectorComision(List<CalculadorComisionService> estrategias) {
        this.estrategias = estrategias;
    }

    /**
     * Calcula la comisión de una venta según el municipio del agricultor.
     *
     * @param municipio municipio del agricultor
     * @param valorVenta valor total de la venta en COP
     * @return la comisión en COP
     * @throws IllegalStateException si ninguna estrategia atiende el municipio
     */
    public BigDecimal calcular(String municipio, BigDecimal valorVenta) {
        return estrategias.stream()
                .filter(estrategia -> estrategia.aplicaA(municipio))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay estrategia de comisión para " + municipio))
                .calcular(valorVenta);
    }
}
