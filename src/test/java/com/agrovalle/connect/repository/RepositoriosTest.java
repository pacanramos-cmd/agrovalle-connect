package com.agrovalle.connect.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Municipio;
import com.agrovalle.connect.model.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verifica que las consultas derivadas de los repositorios funcionan contra la base de pruebas.
 */
@SpringBootTest
@Transactional
class RepositoriosTest {

    @Autowired
    private AgricultorRepository agricultorRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Test
    void consultasDerivadasDeAgricultor() {
        Municipio municipio = municipioRepository.save(new Municipio("Dagua"));
        Rol rol = rolRepository.save(new Rol("AGRICULTOR"));
        agricultorRepository.save(
                new Agricultor("Pedro Perez", "1130456789", "pedro@correo.com", "hash", municipio, rol));

        assertTrue(agricultorRepository.existsByCedula("1130456789"));
        assertFalse(agricultorRepository.existsByCedula("999999999"));
        assertTrue(agricultorRepository.existsByCorreo("pedro@correo.com"));
        assertFalse(agricultorRepository.existsByCorreo("otro@correo.com"));
        assertTrue(agricultorRepository.findByCorreo("pedro@correo.com").isPresent());
        assertTrue(agricultorRepository.findByCorreo("otro@correo.com").isEmpty());
    }

    @Test
    void buscaMunicipioSinImportarMayusculas() {
        municipioRepository.save(new Municipio("Dagua"));

        assertEquals("Dagua", municipioRepository.findByNombreIgnoreCase("dAGUA").orElseThrow().getNombre());
        assertTrue(municipioRepository.findByNombreIgnoreCase("Atlantida").isEmpty());
    }

    @Test
    void buscaRolPorNombre() {
        rolRepository.save(new Rol("AGRICULTOR"));

        assertEquals("AGRICULTOR", rolRepository.findByNombre("AGRICULTOR").orElseThrow().getNombre());
        assertTrue(rolRepository.findByNombre("INEXISTENTE").isEmpty());
    }
}
