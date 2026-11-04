package com.agrovalle.connect.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de la generación y validación de tokens JWT (HU-13).
 */
class JwtServiceTest {

    private static final String SECRETO = "clave-solo-para-pruebas-0123456789-abcdef";
    private static final long UNA_HORA_MS = 3_600_000L;

    private final JwtService servicio = new JwtService(SECRETO, UNA_HORA_MS);

    @Test
    void generaUnTokenValidoConIdYRol() {
        String token = servicio.generarToken(7L, "AGRICULTOR");

        assertTrue(servicio.esValido(token));
        assertEquals(7L, servicio.extraerUsuarioId(token));
        assertEquals("AGRICULTOR", servicio.extraerRol(token));
    }

    @Test
    void rechazaUnTokenExpirado() {
        JwtService vencido = new JwtService(SECRETO, -1000L);

        assertFalse(servicio.esValido(vencido.generarToken(7L, "AGRICULTOR")));
    }

    @Test
    void rechazaUnTokenFirmadoConOtraClave() {
        JwtService otro = new JwtService("otra-clave-distinta-0123456789-abcdefgh", UNA_HORA_MS);

        assertFalse(servicio.esValido(otro.generarToken(7L, "AGRICULTOR")));
    }

    @Test
    void rechazaUnTokenAlterado() {
        String token = servicio.generarToken(7L, "AGRICULTOR");

        assertFalse(servicio.esValido(token + "x"));
    }

    @Test
    void rechazaTextoQueNoEsUnToken() {
        assertFalse(servicio.esValido("esto-no-es-un-jwt"));
        assertFalse(servicio.esValido(""));
    }
}
