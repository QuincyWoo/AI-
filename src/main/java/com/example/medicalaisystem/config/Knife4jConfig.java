package com.example.medicalaisystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("医疗AI系统接口文档")
                        .description("Spring Boot + Knife4j 接口文档，支持在线调试")
                        .version("v1.0.0"));
    }
}