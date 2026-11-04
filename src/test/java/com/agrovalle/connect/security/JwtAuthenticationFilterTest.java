package com.agrovalle.connect.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Pruebas del filtro que convierte el token JWT de la cabecera en la identidad autenticada.
 */
class JwtAuthenticationFilterTest {

    private static final String SECRETO = "clave-solo-para-pruebas-0123456789-abcdef";

    private final JwtService jwtService = new JwtService(SECRETO, 3_600_000L);
    private final JwtAuthenticationFilter filtro = new JwtAuthenticationFilter(jwtService);

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    private Authentication ejecutar(String cabecera) throws Exception {
        MockHttpServletRequest solicitud = new MockHttpServletRequest();
        if (cabecera != null) {
            solicitud.addHeader("Authorization", cabecera);
        }
        MockFilterChain cadena = new MockFilterChain();

        filtro.doFilter(solicitud, new MockHttpServletResponse(), cadena);

        assertNotNull(cadena.getRequest());
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Test
    void sinCabeceraNoAutentica() throws Exception {
        assertNull(ejecutar(null));
    }

    @Test
    void conCabeceraSinBearerNoAutentica() throws Exception {
        assertNull(ejecutar("Basic abc123"));
    }

    @Test
    void conTokenInvalidoNoAutentica() throws Exception {
        assertNull(ejecutar("Bearer token-falso"));
    }

    @Test
    void conTokenValidoAutenticaConIdYRol() throws Exception {
        Authentication autenticacion = ejecutar("Bearer " + jwtService.generarToken(7L, "AGRICULTOR"));

        assertNotNull(autenticacion);
        assertEquals("7", autenticacion.getName());
        assertEquals("ROLE_AGRICULTOR", autenticacion.getAuthorities().iterator().next().getAuthority());
    }
}
