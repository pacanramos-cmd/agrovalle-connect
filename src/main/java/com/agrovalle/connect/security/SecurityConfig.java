package com.agrovalle.connect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de Spring Security para AgroValle Connect.
 *
 * <p>Política stateless con JWT: no se crean sesiones HTTP. Los endpoints
 * de autenticación ({@code /api/v1/auth/**}) y la consulta pública de productos
 * ({@code GET /api/v1/productos}) son accesibles sin token. Publicar un producto
 * ({@code POST /api/v1/productos}) exige el rol AGRICULTOR (403 si es otro rol); el resto requiere un
 * JWT válido. Una solicitud sin token válido recibe 401.
 *
 * <p>Principio SOLID aplicado:
 * <ul>
 *   <li>OCP — la cadena de filtros puede extenderse sin modificar esta clase.</li>
 *   <li>DIP — depende de abstracciones ({@link UserDetailsService}, {@link PasswordEncoder}).</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ROL_AGRICULTOR = "AGRICULTOR";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Construye la configuración con las dependencias necesarias (DIP).
     *
     * @param jwtAuthenticationFilter filtro que valida tokens JWT
     * @param userDetailsService      servicio que carga usuarios desde la base de datos
     */
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Cadena de filtros de seguridad: stateless, sin CSRF, rutas públicas y protegidas.
     *
     * @param http constructor de la cadena de filtros
     * @return la cadena de filtros configurada
     * @throws Exception si Spring Security lanza una excepción de configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/productos").hasRole(ROL_AGRICULTOR)
                        .anyRequest().authenticated())
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Codificador de contraseñas BCrypt utilizado tanto en el registro (HU-01)
     * como en la verificación de credenciales (HU-13).
     *
     * @return instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Administrador de autenticación configurado con el proveedor DAO.
     *
     * @return {@link AuthenticationManager} listo para autenticar por correo y contraseña
     */
        @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
