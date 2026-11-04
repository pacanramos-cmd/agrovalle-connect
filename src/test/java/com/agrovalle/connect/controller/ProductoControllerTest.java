package com.agrovalle.connect.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agrovalle.connect.dto.ProductoRequest;
import com.agrovalle.connect.dto.ProductoResponse;
import com.agrovalle.connect.exception.AccesoDenegadoException;
import com.agrovalle.connect.exception.FechaCosechaInvalidaException;
import com.agrovalle.connect.exception.TipoProductoNoEncontradoException;
import com.agrovalle.connect.service.ProductoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Pruebas de la capa REST de productos (HU-02 y HU-04) con MockMvc: validaciones, códigos HTTP y filtros. La
 * autenticación real se prueba en {@code ProductoIntegracionTest}.
 */
@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    private static final String PRODUCTO_VALIDO = """
            {"tipo":"Mango","cantidad":120,"unidad_medida":"KG","precio_unitario":3500,
             "fecha_cosecha":"2026-11-15"}""";

    @Mock
    private ProductoService productoService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductoController(productoService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private ProductoResponse respuesta() {
        return new ProductoResponse(1L, "Mango", "Frutas", "Dagua", new BigDecimal("120"), "KG",
                new BigDecimal("3500"), LocalDate.of(2026, 11, 15), new BigDecimal("16800.00"));
    }

    private ResultActions publicar(String cuerpo) throws Exception {
        return mockMvc.perform(post("/api/v1/productos")
                .principal(new UsernamePasswordAuthenticationToken(7L, null, List.of()))
                .contentType(MediaType.APPLICATION_JSON).content(cuerpo));
    }

    @Test
    void publicacionExitosaResponde201() throws Exception {
        when(productoService.publicar(eq(7L), any(ProductoRequest.class))).thenReturn(respuesta());

        publicar(PRODUCTO_VALIDO)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.unidad_medida").value("KG"))
                .andExpect(jsonPath("$.comision_estimada").value(16800.00));
    }

    @Test
    void publicacionConFechaAnteriorAHoyResponde400() throws Exception {
        when(productoService.publicar(eq(7L), any(ProductoRequest.class)))
                .thenThrow(new FechaCosechaInvalidaException("La fecha de cosecha no puede ser anterior a hoy"));

        publicar(PRODUCTO_VALIDO).andExpect(status().isBadRequest());
    }

    @Test
    void publicacionConTipoInexistenteResponde400() throws Exception {
        when(productoService.publicar(eq(7L), any(ProductoRequest.class)))
                .thenThrow(new TipoProductoNoEncontradoException("El tipo no existe en el catálogo"));

        publicar(PRODUCTO_VALIDO).andExpect(status().isBadRequest());
    }

    @Test
    void publicacionSinCamposObligatoriosResponde400() throws Exception {
        publicar("{\"tipo\":\"Mango\"}").andExpect(status().isBadRequest());

        verify(productoService, never()).publicar(any(), any(ProductoRequest.class));
    }

    @Test
    void publicacionConUnidadDesconocidaResponde400() throws Exception {
        publicar(PRODUCTO_VALIDO.replace("KG", "PARSEC")).andExpect(status().isBadRequest());
    }

    @Test
    void publicacionDeUnUsuarioSinPerfilDeAgricultorResponde403() throws Exception {
        when(productoService.publicar(eq(7L), any(ProductoRequest.class)))
                .thenThrow(new AccesoDenegadoException("Solo los agricultores pueden publicar productos"));

        publicar(PRODUCTO_VALIDO).andExpect(status().isForbidden());
    }

    @Test
    void consultaConFiltrosDevuelveUnArreglo() throws Exception {
        when(productoService.listar("Dagua", "Frutas")).thenReturn(List.of(respuesta()));

        mockMvc.perform(get("/api/v1/productos").param("municipio", "Dagua").param("categoria", "Frutas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Mango"))
                .andExpect(jsonPath("$[0].municipio").value("Dagua"));
    }

    @Test
    void consultaSinCoincidenciasDevuelveArregloVacio() throws Exception {
        when(productoService.listar("Dagua", "Frutas")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos").param("municipio", "Dagua").param("categoria", "Frutas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
