package com.conectatech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI conectaTechOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Conecta Tech API")
                        .description("Plataforma que conecta profissionais experientes de TI (Mentores) " +
                                     "a jovens de periferia que buscam entrar no mercado de tecnologia (Mentorados).")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipe Conecta Tech")
                                .email("contato@conectatech.com.br"))
                        .license(new License()
                                .name("MIT License")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente local")
                ));
    }
}
