package com.agrovalle.connect.security;

import com.agrovalle.connect.model.Usuario;
import com.agrovalle.connect.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Carga los datos de acceso de un usuario (tabla {@code usuario}) para Spring Security, usando el correo
 * como nombre de usuario. El "username" que expone es el identificador numérico del usuario.
 */
@Service
public class AgricultorUserDetailsService implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    private final UsuarioRepository usuarioRepository;

    /**
     * Crea el servicio.
     *
     * @param usuarioRepository acceso a usuarios
     */
    public AgricultorUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + correo));
        String authority = ROLE_PREFIX + usuario.getRol().getNombre();
        return new User(
                String.valueOf(usuario.getId()),
                usuario.getContrasenaHash(),
                List.of(new SimpleGrantedAuthority(authority)));
    }
}
