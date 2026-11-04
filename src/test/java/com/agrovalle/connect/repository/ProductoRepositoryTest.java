package com.agrovalle.connect.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Categoria;
import com.agrovalle.connect.model.Municipio;
import com.agrovalle.connect.model.Producto;
import com.agrovalle.connect.model.Rol;
import com.agrovalle.connect.model.TipoProducto;
import com.agrovalle.connect.model.Usuario;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * Pruebas de integración del filtro de HU-04 contra la base de pruebas: municipio, categoría y actividad.
 */
@SpringBootTest
@Transactional
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;
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

    private Agricultor agricultorDagua;
    private Agricultor agricultorBuga;
    private TipoProducto mango;
    private TipoProducto papa;

    @BeforeEach
    void prepararDatos() {
        Rol rol = rolRepository.save(new Rol("AGRICULTOR"));
        Municipio dagua = municipioRepository.save(new Municipio("Dagua"));
        Municipio buga = municipioRepository.save(new Municipio("Buga"));
        agricultorDagua = nuevoAgricultor("pedro@correo.com", "1130456789", dagua, rol);
        agricultorBuga = nuevoAgricultor("maria@correo.com", "1130456790", buga, rol);
        Categoria frutas = categoriaRepository.save(new Categoria("Frutas"));
        Categoria tuberculos = categoriaRepository.save(new Categoria("Tubérculos"));
        mango = tipoProductoRepository.save(new TipoProducto("Mango", frutas));
        papa = tipoProductoRepository.save(new TipoProducto("Papa criolla", tuberculos));
    }

    private Agricultor nuevoAgricultor(String correo, String cedula, Municipio municipio, Rol rol) {
        Usuario usuario = usuarioRepository.save(new Usuario("Agricultor", correo, "hash", rol));
        return agricultorRepository.save(new Agricultor(usuario, cedula, municipio));
    }

    private Producto publicar(Agricultor agricultor, TipoProducto tipo) {
        return productoRepository.save(new Producto(agricultor, tipo, new BigDecimal("100"), "KG",
                new BigDecimal("3000"), LocalDate.of(2026, 12, 1)));
    }

    private List<Producto> consultar(Specification<Producto> criterio) {
        return productoRepository.findAll(criterio);
    }

    @Test
    void filtraPorMunicipioYCategoria() {
        publicar(agricultorDagua, mango);
        publicar(agricultorDagua, papa);
        publicar(agricultorBuga, mango);

        List<Producto> resultado = consultar(ProductoSpecifications.activo()
                .and(ProductoSpecifications.deMunicipio("dagua"))
                .and(ProductoSpecifications.deCategoria("FRUTAS")));

        assertEquals(1, resultado.size());
        assertEquals("Mango", resultado.get(0).getTipoProducto().getNombre());
        assertEquals("Dagua", resultado.get(0).getAgricultor().getMunicipio().getNombre());
    }

    @Test
    void sinFiltrosDevuelveTodasLasOfertasActivas() {
        publicar(agricultorDagua, mango);
        publicar(agricultorBuga, papa);

        assertEquals(2, consultar(ProductoSpecifications.activo()).size());
    }

    @Test
    void sinCoincidenciasDevuelveListaVacia() {
        publicar(agricultorDagua, mango);

        List<Producto> resultado = consultar(ProductoSpecifications.activo()
                .and(ProductoSpecifications.deMunicipio("Buga"))
                .and(ProductoSpecifications.deCategoria("Frutas")));

        assertTrue(resultado.isEmpty());
    }
}
