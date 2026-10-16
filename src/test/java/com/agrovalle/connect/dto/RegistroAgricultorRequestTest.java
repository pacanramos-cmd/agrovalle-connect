package com.agrovalle.connect.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verifica las validaciones del DTO de registro (escenario 2 del BDD de HU-01: datos inválidos).
 */
class RegistroAgricultorRequestTest {

    private static Validator validador;

    @BeforeAll
    static void crearValidador() {
        validador = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static RegistroAgricultorRequest solicitud(String nombre, String municipio, String cedula,
            String correo, String contrasena) {
        return new RegistroAgricultorRequest(nombre, municipio, cedula, correo, contrasena);
    }

    private static Set<String> camposInvalidos(RegistroAgricultorRequest solicitud) {
        return validador.validate(solicitud).stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    @Test
    void aceptaUnaSolicitudValida() {
        assertTrue(camposInvalidos(
                solicitud("Pedro Perez", "Dagua", "1130456789", "pedro@correo.com", "clave12345")).isEmpty());
    }

    @Test
    void rechazaNombreVacio() {
        Set<String> campos = camposInvalidos(
                solicitud("  ", "Dagua", "1130456789", "pedro@correo.com", "clave12345"));

        assertTrue(campos.contains("nombre"));
    }

    @Test
    void rechazaMunicipioFaltante() {
        Set<String> campos = camposInvalidos(
                solicitud("Pedro Perez", null, "1130456789", "pedro@correo.com", "clave12345"));

        assertTrue(campos.contains("ubicacionValle"));
    }

    @Test
    void rechazaCedulaConLetrasOMuyCorta() {
        assertTrue(camposInvalidos(
                solicitud("Pedro Perez", "Dagua", "abc123456", "pedro@correo.com", "clave12345"))
                .contains("cedula"));
        assertTrue(camposInvalidos(
                solicitud("Pedro Perez", "Dagua", "12345", "pedro@correo.com", "clave12345"))
                .contains("cedula"));
    }

    @Test
    void rechazaCorreoSinFormatoValido() {
        Set<String> campos = camposInvalidos(
                solicitud("Pedro Perez", "Dagua", "1130456789", "esto-no-es-un-correo", "clave12345"));

        assertTrue(campos.contains("correo"));
    }

    @Test
    void rechazaContrasenaCorta() {
        Set<String> campos = camposInvalidos(
                solicitud("Pedro Perez", "Dagua", "1130456789", "pedro@correo.com", "corta"));

        assertTrue(campos.contains("contrasena"));
    }

    @Test
    void laContrasenaNoApareceEnElTexto() {
        String texto = solicitud("Pedro Perez", "Dagua", "1130456789", "pedro@correo.com", "clave12345")
                .toString();

        assertFalse(texto.contains("clave12345"));
    }
}
