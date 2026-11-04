package com.agrovalle.connect.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrovalle.connect.dto.LoginRequest;
import com.agrovalle.connect.dto.LoginResponse;
import com.agrovalle.connect.exception.CredencialesInvalidasException;
import com.agrovalle.connect.model.Rol;
import com.agrovalle.connect.model.Usuario;
import com.agrovalle.connect.repository.UsuarioRepository;
import com.agrovalle.connect.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Pruebas unitarias del inicio de sesión (HU-13): credenciales válidas e inválidas.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthService servicio;

    @BeforeEach
    void crearServicio() {
        servicio = new AuthService(usuarioRepository, passwordEncoder, jwtService);
    }

    private Usuario usuario() {
        return new Usuario("Pedro Perez", "pedro@correo.com", "hash-bcrypt", new Rol("AGRICULTOR"));
    }

    @Test
    void devuelveTokenConCredencialesValidas() {
        // Arrange
        when(usuarioRepository.findByCorreo("pedro@correo.com")).thenReturn(Optional.of(usuario()));
        when(passwordEncoder.matches("clave12345", "hash-bcrypt")).thenReturn(true);
        when(jwtService.generarToken(any(), anyString())).thenReturn("token-firmado");

        // Act
        LoginResponse respuesta = servicio.login(new LoginRequest(" Pedro@Correo.com ", "clave12345"));

        // Assert
        assertEquals("token-firmado", respuesta.token());
        assertEquals("Bearer", respuesta.tipo());
        assertEquals("AGRICULTOR", respuesta.rol());
    }

    @Test
    void rechazaContrasenaIncorrecta() {
        when(usuarioRepository.findByCorreo("pedro@correo.com")).thenReturn(Optional.of(usuario()));
        when(passwordEncoder.matches("incorrecta1", "hash-bcrypt")).thenReturn(false);

        LoginRequest solicitud = new LoginRequest("pedro@correo.com", "incorrecta1");
        assertThrows(CredencialesInvalidasException.class, () -> servicio.login(solicitud));

        verify(jwtService, never()).generarToken(any(), anyString());
    }

    @Test
    void rechazaCorreoNoRegistradoConElMismoMensaje() {
        when(usuarioRepository.findByCorreo("nadie@correo.com")).thenReturn(Optional.empty());

        LoginRequest solicitud = new LoginRequest("nadie@correo.com", "clave12345");
        CredencialesInvalidasException excepcion =
                assertThrows(CredencialesInvalidasException.class, () -> servicio.login(solicitud));

        assertEquals("Correo o contrasena incorrectos", excepcion.getMessage());
    }
}
