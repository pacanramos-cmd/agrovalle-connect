package com.agrovalle.connect.service.comision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del patrón Strategy de comisiones: cada zona calcula su porcentaje y el selector elige la
 * estrategia según el municipio.
 */
class ComisionTest {

    private final List<CalculadorComisionService> estrategias =
            List.of(new ComisionZonaCercana(), new ComisionZonaIntermedia(), new ComisionZonaLejana());
    private final SelectorComision selector = new SelectorComision(estrategias);

    @Test
    void zonaCercanaCobraTresPorCiento() {
        CalculadorComisionService estrategia = new ComisionZonaCercana();

        assertTrue(estrategia.aplicaA("Palmira"));
        assertTrue(estrategia.aplicaA("  JAMUNDÍ "));
        assertFalse(estrategia.aplicaA("Dagua"));
        assertFalse(estrategia.aplicaA(null));
        assertEquals(new BigDecimal("3000.00"), estrategia.calcular(new BigDecimal("100000")));
    }

    @Test
    void zonaIntermediaCobraCuatroPorCiento() {
        CalculadorComisionService estrategia = new ComisionZonaIntermedia();

        assertTrue(estrategia.aplicaA("Dagua"));
        assertTrue(estrategia.aplicaA("Buga"));
        assertFalse(estrategia.aplicaA("Palmira"));
        assertEquals(new BigDecimal("4000.00"), estrategia.calcular(new BigDecimal("100000")));
    }

    @Test
    void zonaLejanaAtiendeCualquierMunicipioYCobraCincoPorCiento() {
        CalculadorComisionService estrategia = new ComisionZonaLejana();

        assertTrue(estrategia.aplicaA("Cartago"));
        assertEquals(new BigDecimal("5000.00"), estrategia.calcular(new BigDecimal("100000")));
    }

    @Test
    void elSelectorEligeLaEstrategiaDelMunicipio() {
        BigDecimal venta = new BigDecimal("200000");

        assertEquals(new BigDecimal("6000.00"), selector.calcular("Palmira", venta));
        assertEquals(new BigDecimal("8000.00"), selector.calcular("Buga", venta));
        assertEquals(new BigDecimal("10000.00"), selector.calcular("Cartago", venta));
    }

    @Test
    void todasLasEstrategiasSonSustituiblesYNuncaDevuelvenNegativos() {
        for (CalculadorComisionService estrategia : estrategias) {
            BigDecimal comision = estrategia.calcular(new BigDecimal("12345.67"));

            assertEquals(2, comision.scale());
            assertTrue(comision.signum() >= 0);
        }
    }

    @Test
    void sinEstrategiasDisponiblesElSelectorFalla() {
        SelectorComision vacio = new SelectorComision(List.of());
        BigDecimal venta = BigDecimal.TEN;

        assertThrows(IllegalStateException.class, () -> vacio.calcular("Dagua", venta));
    }
}
