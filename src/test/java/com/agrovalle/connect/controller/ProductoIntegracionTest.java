package com.agrovalle.connect.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Categoria;
import com.agrovalle.connect.model.Municipio;
import com.agrovalle.connect.model.Rol;
import com.agrovalle.connect.model.TipoProducto;
import com.agrovalle.connect.model.Usuario;
import com.agrovalle.connect.repository.AgricultorRepository;
import com.agrovalle.connect.repository.CategoriaRepository;
import com.agrovalle.connect.repository.MunicipioRepository;
import com.agrovalle.connect.repository.RolRepository;
import com.agrovalle.connect.repository.TipoProductoRepository;
import com.agrovalle.connect.repository.UsuarioRepository;
import com.agrovalle.connect.security.JwtService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Prueba de integración de punta a punta de HU-02 y HU-04: cadena real de Spring Security, filtro JWT,
 * controlador, servicio, repositorio y base de pruebas H2.
 */
@SpringBootTest
@Transactional
class ProductoIntegracionTest {

    @Autowired
    private WebApplicationContext contexto;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private MunicipioRepository municipioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AgricultorRepository agricultorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    private MockMvc mockMvc;
    private Long agricultorId;

    @BeforeEach
    void prepararDatos() {
        mockMvc = MockMvcBuilders.webAppContextSetup(contexto).apply(springSecurity()).build();
        Rol rolAgricultor = rolRepository.save(new Rol("AGRICULTOR"));
        Municipio dagua = municipioRepository.save(new Municipio("Dagua"));
        Usuario usuario = usuarioRepository.save(
                new Usuario("Pedro Perez", "pedro@correo.com", "hash", rolAgricultor));
        agricultorId = agricultorRepository.save(new Agricultor(usuario, "1130456789", dagua)).getId();
        Categoria frutas = categoriaRepository.save(new Categoria("Frutas"));
        tipoProductoRepository.save(new TipoProducto("Mango", frutas));
    }

    private String cuerpo(LocalDate fechaCosecha) {
        return "{\"tipo\":\"Mango\",\"cantidad\":120,\"unidad_medida\":\"KG\",\"precio_unitario\":3500,"
                + "\"fecha_cosecha\":\"" + fechaCosecha + "\"}";
    }

    private ResultActions publicar(String token, String cuerpo) throws Exception {
        var solicitud = post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON).content(cuerpo);
        if (token != null) {
            solicitud.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        return mockMvc.perform(solicitud);
    }

    @Test
    void agricultorAutenticadoPublicaYLuegoLoEncuentraConElFiltro() throws Exception {
        String token = jwtService.generarToken(agricultorId, "AGRICULTOR");

        publicar(token, cuerpo(LocalDate.now().plusDays(10)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("Mango"))
                .andExpect(jsonPath("$.municipio").value("Dagua"))
                .andExpect(jsonPath("$.comision_estimada").value(16800.00));

        mockMvc.perform(get("/api/v1/productos").param("municipio", "dagua").param("categoria", "FRUTAS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Mango"));
        mockMvc.perform(get("/api/v1/productos").param("municipio", "Buga"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void publicarSinTokenResponde401() throws Exception {
        publicar(null, cuerpo(LocalDate.now().plusDays(10))).andExpect(status().isUnauthorized());
    }

    @Test
    void publicarConTokenInvalidoResponde401() throws Exception {
        publicar("token-falso", cuerpo(LocalDate.now().plusDays(10))).andExpect(status().isUnauthorized());
    }

    @Test
    void publicarConRolCompradorResponde403() throws Exception {
        String token = jwtService.generarToken(agricultorId, "COMPRADOR");

        publicar(token, cuerpo(LocalDate.now().plusDays(10))).andExpect(status().isForbidden());
    }

    @Test
    void publicarConFechaPasadaResponde400() throws Exception {
        String token = jwtService.generarToken(agricultorId, "AGRICULTOR");

        publicar(token, cuerpo(LocalDate.now().minusDays(2))).andExpect(status().isBadRequest());
    }

    @Test
    void publicarConUnUsuarioInexistenteResponde403() throws Exception {
        String token = jwtService.generarToken(999_999L, "AGRICULTOR");

        publicar(token, cuerpo(LocalDate.now().plusDays(10))).andExpect(status().isForbidden());
    }
}
