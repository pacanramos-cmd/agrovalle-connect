package com.agrovalle.connect.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agrovalle.connect.dto.AgricultorResponse;
import com.agrovalle.connect.dto.LoginResponse;
import com.agrovalle.connect.dto.RegistroAgricultorRequest;
import com.agrovalle.connect.exception.CredencialesInvalidasException;
import com.agrovalle.connect.exception.MunicipioNoEncontradoException;
import com.agrovalle.connect.exception.RecursoDuplicadoException;
import com.agrovalle.connect.service.AgricultorService;
import com.agrovalle.connect.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Pruebas de la capa REST de registro (HU-01) e inicio de sesión (HU-13): simulan peticiones HTTP con MockMvc
 * y verifican estados y cuerpos JSON.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final String REGISTRO_VALIDO = """
            {"nombre":"Pedro Perez","ubicacion_valle":"Dagua","cedula":"1130456789",
             "correo":"pedro@correo.com","contrasena":"clave12345"}""";

    @Mock
    private AgricultorService agricultorService;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(agricultorService, authService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private void registrar(String cuerpo, int estadoEsperado) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().is(estadoEsperado));
    }

    @Test
    void registroExitosoResponde201ConElAgricultor() throws Exception {
        when(agricultorService.registrar(any(RegistroAgricultorRequest.class))).thenReturn(
                new AgricultorResponse(1L, "Pedro Perez", "1130456789", "pedro@correo.com", "Dagua"));

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTRO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ubicacion_valle").value("Dagua"))
                .andExpect(jsonPath("$.contrasena").doesNotExist());
    }

    @Test
    void registroConCedulaInvalidaResponde400SinLlamarAlServicio() throws Exception {
        registrar(REGISTRO_VALIDO.replace("1130456789", "12ab"), 400);

        verify(agricultorService, never()).registrar(any(RegistroAgricultorRequest.class));
    }

    @Test
    void registroConCuerpoDetalladoDeErrorIncluyeElCampo() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTRO_VALIDO.replace("1130456789", "12ab")))
                .andExpect(jsonPath("$.estado").value(400))
                .andExpect(jsonPath("$.detalles[0]").exists());
    }

    @Test
    void registroConJsonMalFormadoResponde400() throws Exception {
        registrar("{no-json", 400);
    }

    @Test
    void registroConCedulaDuplicadaResponde409() throws Exception {
        when(agricultorService.registrar(any(RegistroAgricultorRequest.class)))
                .thenThrow(new RecursoDuplicadoException("La cédula ya está registrada"));

        registrar(REGISTRO_VALIDO, 409);
    }

    @Test
    void registroConMunicipioInexistenteResponde400() throws Exception {
        when(agricultorService.registrar(any(RegistroAgricultorRequest.class)))
                .thenThrow(new MunicipioNoEncontradoException("Municipio fuera del catálogo"));

        registrar(REGISTRO_VALIDO, 400);
    }

    @Test
    void loginExitosoResponde200ConElToken() throws Exception {
        when(authService.login(any())).thenReturn(LoginResponse.bearer("token-firmado", 1L, "AGRICULTOR"));

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"pedro@correo.com\",\"contrasena\":\"clave12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-firmado"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.usuario_id").value(1))
                .andExpect(jsonPath("$.rol").value("AGRICULTOR"));
    }

    @Test
    void loginConCredencialesInvalidasResponde401() throws Exception {
        when(authService.login(any())).thenThrow(new CredencialesInvalidasException());

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"pedro@correo.com\",\"contrasena\":\"clave-mala-123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.estado").value(401));
    }

    @Test
    void loginSinContrasenaResponde400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"pedro@correo.com\"}"))
                .andExpect(status().isBadRequest());
    }
}
