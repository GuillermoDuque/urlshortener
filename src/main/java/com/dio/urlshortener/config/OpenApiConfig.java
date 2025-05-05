package com.dio.urlshortener.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .description("API para acortar URLs")
                        .version("1.0.0-SNAPSHOT")
                        .contact(new Contact()
                                .name("Guillermo Duque")
                                .url("https://github.com/GuillermoDuque")
                                .email("gui.duquesanchez@gmail.com"))

                );
    }


}
