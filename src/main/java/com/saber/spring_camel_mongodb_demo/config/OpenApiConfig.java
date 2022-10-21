package com.saber.spring_camel_mongodb_demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(@Value("${service.swagger.title}") String title,
                         @Value("${service.swagger.version}")  String version){
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(title)
                        .version(version));
    }
}