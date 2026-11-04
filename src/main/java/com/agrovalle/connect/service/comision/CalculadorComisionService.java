package com.agrovalle.connect.service.comision;

import java.math.BigDecimal;

/**
 * Estrategia de cálculo de la comisión de la plataforma según el municipio del Valle del Cauca (patrón
 * Strategy). Agregar una zona nueva es crear otra implementación, sin modificar el código ya probado
 * (principio abierto/cerrado).
 */
public interface CalculadorComisionService {

    /**
     * Indica si esta estrategia atiende el municipio dado.
     *
     * @param municipio nombre del municipio
     * @return {@code true} si la estrategia aplica
     */
    boolean aplicaA(String municipio);

    /**
     * Calcula la comisión sobre el valor de la venta.
     *
     * @param valorVenta valor total de la venta en COP
     * @return la comisión en COP, con dos decimales y nunca negativa
     */
    BigDecimal calcular(BigDecimal valorVenta);
}
