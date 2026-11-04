package com.agrovalle.connect.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Categoria;
import com.agrovalle.connect.model.Municipio;
import com.agrovalle.connect.model.Rol;
import com.agrovalle.connect.model.TipoProducto;
import com.agrovalle.connect.model.Usuario;
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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgricultorRepository agricultorRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Test
    void consultasDerivadasDeUsuarioYAgricultor() {
        Municipio municipio = municipioRepository.save(new Municipio("Dagua"));
        Rol rol = rolRepository.save(new Rol("AGRICULTOR"));
        Usuario usuario = usuarioRepository.save(new Usuario("Pedro Perez", "pedro@correo.com", "hash", rol));
        agricultorRepository.save(new Agricultor(usuario, "1130456789", municipio));

        assertTrue(agricultorRepository.existsByCedula("1130456789"));
        assertFalse(agricultorRepository.existsByCedula("999999999"));
        assertTrue(usuarioRepository.existsByCorreo("pedro@correo.com"));
        assertFalse(usuarioRepository.existsByCorreo("otro@correo.com"));
        assertTrue(usuarioRepository.findByCorreo("pedro@correo.com").isPresent());
        assertTrue(usuarioRepository.findByCorreo("otro@correo.com").isEmpty());
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

    @Test
    void buscaTipoDeProductoSinImportarMayusculas() {
        Categoria frutas = categoriaRepository.save(new Categoria("Frutas"));
        tipoProductoRepository.save(new TipoProducto("Mango", frutas));

        assertEquals("Mango", tipoProductoRepository.findByNombreIgnoreCase("mANGO").orElseThrow().getNombre());
        assertTrue(tipoProductoRepository.findByNombreIgnoreCase("Cometa").isEmpty());
    }
}
