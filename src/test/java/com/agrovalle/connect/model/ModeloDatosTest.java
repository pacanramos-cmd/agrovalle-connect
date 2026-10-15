package com.agrovalle.connect.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verifica que el mapeo JPA respeta las reglas del modelo de datos (HU-01 y HU-13): llaves
 * generadas, unicidad de cédula y correo, y obligatoriedad de municipio y rol.
 */
@SpringBootTest
@Transactional
class ModeloDatosTest {

    @PersistenceContext
    private EntityManager entityManager;

    private Municipio municipio;
    private Rol rol;

    @BeforeEach
    void prepararCatalogos() {
        municipio = new Municipio("Dagua");
        rol = new Rol("AGRICULTOR");
        entityManager.persist(municipio);
        entityManager.persist(rol);
    }

    private Agricultor agricultor(String cedula, String correo) {
        return new Agricultor("Pedro Perez", cedula, correo, "hash-de-prueba", municipio, rol);
    }

    @Test
    void guardaAgricultorConLlaveYFechaDeCreacion() {
        Agricultor agricultor = agricultor("1130456789", "pedro@correo.com");

        entityManager.persist(agricultor);
        entityManager.flush();

        assertNotNull(agricultor.getId());
        assertNotNull(agricultor.getCreadoEn());
        assertEquals("Dagua", agricultor.getMunicipio().getNombre());
        assertEquals("AGRICULTOR", agricultor.getRol().getNombre());
    }

    @Test
    void rechazaCedulaDuplicada() {
        entityManager.persist(agricultor("1130456789", "pedro@correo.com"));
        entityManager.flush();
        Agricultor duplicado = agricultor("1130456789", "otro@correo.com");

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(duplicado);
            entityManager.flush();
        });
    }

    @Test
    void rechazaCorreoDuplicado() {
        entityManager.persist(agricultor("1130456789", "pedro@correo.com"));
        entityManager.flush();
        Agricultor duplicado = agricultor("1130456790", "pedro@correo.com");

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(duplicado);
            entityManager.flush();
        });
    }

    @Test
    void rechazaAgricultorSinMunicipio() {
        Agricultor sinMunicipio = new Agricultor(
                "Pedro Perez", "1130456789", "pedro@correo.com", "hash-de-prueba", null, rol);

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(sinMunicipio);
            entityManager.flush();
        });
    }
}
