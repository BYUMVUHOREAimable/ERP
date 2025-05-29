package com.erp.employee.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT-based authentication. Include a valid token in the Authorization header.",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

    @Value("${app.server.url}")
    private String serverUrl;

    @Bean
    @Profile("dev")
    public OpenAPI openAPIForDev() {
        return new OpenAPI()
                .info(new Info()
                        .title("🏦 ERP Application API Docs")
                        .description("This is the Swagger documentation FOR erp Application backend APIs.")
                        .version("1.0.0"))
                .servers(List.of(new Server().url(serverUrl).description("Development Server")));
    }

    @Bean
    @Profile("prod")
    public OpenAPI openAPIForProd() {
        return new OpenAPI()
                .info(new Info()
                        .title("🏦 ERPApplication API Docs")
                        .description("Production API documentation for the ERPApplication.")
                        .version("1.0.0"))
                .servers(List.of(new Server().url(serverUrl).description("Production Server")));
    }

    @Bean
    @Profile("test")
    public OpenAPI openAPIForTest() {
        return new OpenAPI()
                .info(new Info()
                        .title("🏦 ERPApplication API Docs")
                        .description("Testing API documentation for the ERP Application.")
                        .version("1.0.0"))
                .servers(List.of(new Server().url(serverUrl).description("Testing Server")));
    }
}
