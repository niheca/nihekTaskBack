package com.example.nihektaskback.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Rutas afectadas
                .allowedOrigins("http://localhost:4200")  // Origen permitido (Angular)
                .allowedMethods("GET", "POST", "PUT", "DELETE" , "PATCH", "OPTIONS")  // Métodos permitidos
                .allowedHeaders("*")  // Headers permitidos
                .allowCredentials(true);  // Permitir cookies (si es necesario)
    }
}
