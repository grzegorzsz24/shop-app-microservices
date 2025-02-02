package org.example.productservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenAPIConfig {

    @Bean
    OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Product Service API")
                        .description("This is the REST API for product service")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0")));
    }
}
