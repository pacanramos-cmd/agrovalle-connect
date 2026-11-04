package com.agrovalle.connect.service;

import com.agrovalle.connect.dto.LoginRequest;
import com.agrovalle.connect.dto.LoginResponse;
import com.agrovalle.connect.exception.CredencialesInvalidasException;
import com.agrovalle.connect.model.Usuario;
import com.agrovalle.connect.repository.UsuarioRepository;
import com.agrovalle.connect.security.JwtService;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reglas de negocio del inicio de sesión (HU-13): valida las credenciales y emite un token JWT.
 *
 * <p>Si el correo no existe o la contraseña no coincide se lanza la misma excepción, para no revelar
 * cuál de los dos datos falló.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Crea el servicio con sus dependencias.
     *
     * @param usuarioRepository acceso a usuarios
     * @param passwordEncoder   comparador de contraseñas cifradas
     * @param jwtService        emisor de tokens
     */
    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Autentica al usuario y devuelve el token.
     *
     * @param solicitud correo y contraseña
     * @return el token con el identificador y el rol del usuario
     * @throws CredencialesInvalidasException si las credenciales no son correctas
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest solicitud) {
        String correo = solicitud.correo().trim().toLowerCase(Locale.ROOT);
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(CredencialesInvalidasException::new);

        if (!passwordEncoder.matches(solicitud.contrasena(), usuario.getContrasenaHash())) {
            throw new CredencialesInvalidasException();
        }

        String rol = usuario.getRol().getNombre();
        String token = jwtService.generarToken(usuario.getId(), rol);
        return LoginResponse.bearer(token, usuario.getId(), rol);
    }
}
