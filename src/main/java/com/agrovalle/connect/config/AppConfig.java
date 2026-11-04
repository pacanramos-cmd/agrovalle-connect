package com.agrovalle.connect.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beans de infraestructura compartidos.
 */
@Configuration
public class AppConfig {

    /**
     * Reloj de la aplicación en la zona horaria de Colombia. Las pruebas lo reemplazan por uno fijo.
     *
     * @return el reloj del sistema en America/Bogota
     */
    @Bean
    public Clock reloj() {
        return Clock.system(ZoneId.of("America/Bogota"));
    }
}
