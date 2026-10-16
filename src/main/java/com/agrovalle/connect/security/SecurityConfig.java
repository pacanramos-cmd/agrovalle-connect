package com.agrovalle.connect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de seguridad. Por ahora solo define el codificador de contraseñas (BCrypt) que usa el
 * registro de HU-01; la política stateless y el acceso a {@code /api/v1/auth/**} se agregan en HU-13.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
