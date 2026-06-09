package com.conectatech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuração de CORS para permitir chamadas do front-end Next.js
 * rodando em http://localhost:3000.
 *
 * Arquivo: src/main/java/com/conectatech/config/WebConfig.java
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Origens permitidas — adicione a URL de produção quando fizer deploy
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://127.0.0.1:3000"
        ));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Headers que o cliente pode enviar
        config.setAllowedHeaders(List.of("*"));

        // Headers que o browser pode expor ao JavaScript (ex: Location em 201)
        config.setExposedHeaders(List.of("Location", "Content-Type"));

        // Permite cookies/credenciais (útil para futuras implementações de auth)
        config.setAllowCredentials(true);

        // Cache do preflight OPTIONS por 1 hora
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
