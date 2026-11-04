package com.agrovalle.connect.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verifica que el mapeo JPA respeta las reglas del modelo de datos (HU-01, HU-02 y HU-13): llaves generadas,
 * perfil 1 a 1 con el usuario, unicidad de cédula y correo, y obligatoriedad de las relaciones.
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

    private Usuario usuario(String correo) {
        return new Usuario("Pedro Perez", correo, "hash-de-prueba", rol);
    }

    private Agricultor guardarAgricultor(String cedula, String correo) {
        Usuario usuario = usuario(correo);
        entityManager.persist(usuario);
        Agricultor agricultor = new Agricultor(usuario, cedula, municipio);
        entityManager.persist(agricultor);
        entityManager.flush();
        return agricultor;
    }

    @Test
    void guardaUsuarioConLlaveYFechaDeCreacion() {
        Usuario usuario = usuario("pedro@correo.com");

        entityManager.persist(usuario);
        entityManager.flush();

        assertNotNull(usuario.getId());
        assertNotNull(usuario.getCreadoEn());
        assertEquals("AGRICULTOR", usuario.getRol().getNombre());
    }

    @Test
    void elAgricultorComparteLaLlaveDelUsuario() {
        Agricultor agricultor = guardarAgricultor("1130456789", "pedro@correo.com");

        assertEquals(agricultor.getUsuario().getId(), agricultor.getId());
        assertEquals("Pedro Perez", agricultor.getNombre());
        assertEquals("pedro@correo.com", agricultor.getCorreo());
        assertEquals("Dagua", agricultor.getMunicipio().getNombre());
    }

    @Test
    void rechazaCedulaDuplicada() {
        guardarAgricultor("1130456789", "pedro@correo.com");
        Usuario otro = usuario("otro@correo.com");
        entityManager.persist(otro);
        Agricultor duplicado = new Agricultor(otro, "1130456789", municipio);

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(duplicado);
            entityManager.flush();
        });
    }

    @Test
    void rechazaCorreoDuplicado() {
        entityManager.persist(usuario("pedro@correo.com"));
        entityManager.flush();
        Usuario duplicado = usuario("pedro@correo.com");

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(duplicado);
            entityManager.flush();
        });
    }

    @Test
    void rechazaAgricultorSinMunicipio() {
        Usuario usuario = usuario("pedro@correo.com");
        entityManager.persist(usuario);
        Agricultor sinMunicipio = new Agricultor(usuario, "1130456789", null);

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(sinMunicipio);
            entityManager.flush();
        });
    }

    @Test
    void guardaProductoActivoConCategoriaDelTipo() {
        Agricultor agricultor = guardarAgricultor("1130456789", "pedro@correo.com");
        Categoria frutas = new Categoria("Frutas");
        entityManager.persist(frutas);
        TipoProducto mango = new TipoProducto("Mango", frutas);
        entityManager.persist(mango);
        Producto producto = new Producto(agricultor, mango, new BigDecimal("120"), "KG",
                new BigDecimal("3500"), LocalDate.of(2026, 11, 15));

        entityManager.persist(producto);
        entityManager.flush();

        assertNotNull(producto.getId());
        assertNotNull(producto.getCreadoEn());
        assertTrue(producto.isActivo());
        assertEquals("Frutas", producto.getTipoProducto().getCategoria().getNombre());
        assertEquals("Dagua", producto.getAgricultor().getMunicipio().getNombre());
    }
}
