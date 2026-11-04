package com.agrovalle.connect.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrovalle.connect.dto.ProductoRequest;
import com.agrovalle.connect.dto.ProductoResponse;
import com.agrovalle.connect.event.ProductoPublicadoEvent;
import com.agrovalle.connect.exception.AccesoDenegadoException;
import com.agrovalle.connect.exception.FechaCosechaInvalidaException;
import com.agrovalle.connect.exception.TipoProductoNoEncontradoException;
import com.agrovalle.connect.mapper.ProductoMapper;
import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Categoria;
import com.agrovalle.connect.model.Municipio;
import com.agrovalle.connect.model.Producto;
import com.agrovalle.connect.model.Rol;
import com.agrovalle.connect.model.TipoProducto;
import com.agrovalle.connect.model.UnidadMedida;
import com.agrovalle.connect.model.Usuario;
import com.agrovalle.connect.repository.AgricultorRepository;
import com.agrovalle.connect.repository.ProductoRepository;
import com.agrovalle.connect.repository.TipoProductoRepository;
import com.agrovalle.connect.service.comision.ComisionZonaCercana;
import com.agrovalle.connect.service.comision.ComisionZonaIntermedia;
import com.agrovalle.connect.service.comision.ComisionZonaLejana;
import com.agrovalle.connect.service.comision.SelectorComision;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * Pruebas unitarias de la publicación (HU-02) y del filtro (HU-04), con repositorios y reloj simulados.
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    private static final Long USUARIO_ID = 7L;
    private static final ZoneId ZONA = ZoneId.of("America/Bogota");
    private static final LocalDate HOY = LocalDate.of(2026, 11, 3);

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private AgricultorRepository agricultorRepository;

    @Mock
    private TipoProductoRepository tipoProductoRepository;

    @Mock
    private ApplicationEventPublisher publicador;

    private ProductoService servicio;
    private Agricultor agricultor;
    private TipoProducto mango;

    @BeforeEach
    void crearServicio() {
        Clock reloj = Clock.fixed(HOY.atStartOfDay(ZONA).toInstant(), ZONA);
        SelectorComision selector = new SelectorComision(
                List.of(new ComisionZonaCercana(), new ComisionZonaIntermedia(), new ComisionZonaLejana()));
        servicio = new ProductoService(productoRepository, agricultorRepository, tipoProductoRepository,
                new ProductoMapper(), selector, publicador, reloj);
        Usuario usuario = new Usuario("Pedro Perez", "pedro@correo.com", "hash", new Rol("AGRICULTOR"));
        agricultor = new Agricultor(usuario, "1130456789", new Municipio("Dagua"));
        mango = new TipoProducto("Mango", new Categoria("Frutas"));
    }

    private ProductoRequest solicitud(LocalDate fechaCosecha) {
        return new ProductoRequest("Mango", new BigDecimal("100"), UnidadMedida.KG, new BigDecimal("3500"),
                fechaCosecha);
    }

    @Test
    void publicaProductoCalculaComisionYNotificaElEvento() {
        // Arrange
        when(agricultorRepository.findById(USUARIO_ID)).thenReturn(Optional.of(agricultor));
        when(tipoProductoRepository.findByNombreIgnoreCase("Mango")).thenReturn(Optional.of(mango));
        when(productoRepository.save(any(Producto.class))).thenAnswer(llamada -> llamada.getArgument(0));

        // Act
        ProductoResponse respuesta = servicio.publicar(USUARIO_ID, solicitud(HOY));

        // Assert
        assertEquals("Mango", respuesta.tipo());
        assertEquals("Frutas", respuesta.categoria());
        assertEquals("Dagua", respuesta.municipio());
        assertEquals("KG", respuesta.unidadMedida());
        // Dagua es zona intermedia (4%): 100 x 3500 = 350000 -> 14000.00
        assertEquals(new BigDecimal("14000.00"), respuesta.comisionEstimada());
        ArgumentCaptor<ProductoPublicadoEvent> evento = ArgumentCaptor.forClass(ProductoPublicadoEvent.class);
        verify(publicador).publishEvent(evento.capture());
        assertEquals("Mango", evento.getValue().tipoProducto());
    }

    @Test
    void rechazaFechaDeCosechaAnteriorAHoy() {
        when(agricultorRepository.findById(USUARIO_ID)).thenReturn(Optional.of(agricultor));

        ProductoRequest solicitud = solicitud(HOY.minusDays(1));
        assertThrows(FechaCosechaInvalidaException.class, () -> servicio.publicar(USUARIO_ID, solicitud));

        verify(productoRepository, never()).save(any(Producto.class));
        verify(publicador, never()).publishEvent(any(Object.class));
    }

    @Test
    void rechazaUsuarioQueNoEsAgricultor() {
        when(agricultorRepository.findById(USUARIO_ID)).thenReturn(Optional.empty());

        ProductoRequest solicitud = solicitud(HOY);
        assertThrows(AccesoDenegadoException.class, () -> servicio.publicar(USUARIO_ID, solicitud));

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void rechazaTipoDeProductoFueraDelCatalogo() {
        when(agricultorRepository.findById(USUARIO_ID)).thenReturn(Optional.of(agricultor));
        when(tipoProductoRepository.findByNombreIgnoreCase("Mango")).thenReturn(Optional.empty());

        ProductoRequest solicitud = solicitud(HOY);
        assertThrows(TipoProductoNoEncontradoException.class, () -> servicio.publicar(USUARIO_ID, solicitud));

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void listaConFiltrosDevuelveLosProductosConvertidos() {
        Producto producto = new Producto(agricultor, mango, new BigDecimal("10"), "KG", new BigDecimal("1000"),
                HOY);
        when(productoRepository.findAll(anySpecification(), any(Sort.class))).thenReturn(List.of(producto));

        List<ProductoResponse> resultado = servicio.listar(" Dagua ", "Frutas");

        assertEquals(1, resultado.size());
        assertEquals("Dagua", resultado.get(0).municipio());
        assertEquals(new BigDecimal("400.00"), resultado.get(0).comisionEstimada());
    }

    @Test
    void listaSinFiltrosOIgnoraTextoEnBlanco() {
        when(productoRepository.findAll(anySpecification(), any(Sort.class))).thenReturn(List.of());

        List<ProductoResponse> sinFiltros = servicio.listar(null, null);
        List<ProductoResponse> enBlanco = servicio.listar("  ", "");

        assertTrue(sinFiltros.isEmpty());
        assertTrue(enBlanco.isEmpty());
    }

    @SuppressWarnings("unchecked")
    private static Specification<Producto> anySpecification() {
        return any(Specification.class);
    }
}
