package com.agrovalle.connect.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro que intercepta cada petición HTTP, extrae el token JWT del encabezado
 * {@code Authorization} y, si es válido, establece la autenticación en el
 * {@link SecurityContextHolder}.
 *
 * <p>Principio SOLID: SRP — este filtro tiene una única responsabilidad:
 * validar el JWT y propagar la identidad autenticada.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_HEADER = "Authorization";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtService jwtService;

    /**
     * Construye el filtro con el servicio JWT (inyección por constructor — DIP).
     *
     * @param jwtService servicio que valida y extrae información del token
     */
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        if (!jwtService.esValido(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Long usuarioId = jwtService.extraerUsuarioId(token);
        String rol = jwtService.extraerRol(token);
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(ROLE_PREFIX + rol));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(usuarioId, null, authorities);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
