package cl.duoc.milsabores.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.responses.ApiResponse;

import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI milsaboresOpenAPI() {

        return new OpenAPI()
            .info(
                new Info()
                    .title("Pasteleria Mil Sabores - API REST")
                    .description(
                        "API REST para la gestion de productos, " +
                        "categorias, usuarios y compras. " +
                        "Evaluacion Parcial 3 - Desarrollo Fullstack II."
                    )
                    .version("1.0.0")
            )
            .servers(
                List.of(
                    new Server()
                        .url("http://localhost:8080")
                        .description("Servidor local")
                )
            )
            .components(
                new Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        new SecurityScheme()
                            .name("bearerAuth")
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            );
    }

    @Bean
    public OpenApiCustomizer documentarRespuestas() {

        return openApi -> {

            openApi.getPaths().forEach((ruta, path) -> {

                path.readOperationsMap().forEach((metodo, operacion) -> {

                    String http = metodo.name();

                    boolean lecturaPublica =
                        http.equals("GET") &&
                        (ruta.startsWith("/api/v1/productos") ||
                         ruta.startsWith("/api/v1/categorias") ||
                         ruta.equals("/api/v1/prueba")) &&
                        !ruta.equals("/api/v1/productos/administracion") &&
                        !ruta.equals("/api/v1/categorias/administracion");

                    boolean authPublica =
                        http.equals("POST") &&
                        (ruta.equals("/api/v1/auth/login") ||
                         ruta.equals("/api/v1/auth/register") || ruta.equals("/api/v1/auth/refresh") || ruta.equals("/api/v1/auth/logout"));

                    boolean publico = lecturaPublica || authPublica;

                    var respuestas = operacion.getResponses();

                    if (!publico) {

                        operacion.addSecurityItem(
                            new SecurityRequirement()
                                .addList("bearerAuth")
                        );

                        respuestas.putIfAbsent("401",
                            new ApiResponse()
                                .description("Token ausente o invalido"));

                        respuestas.putIfAbsent("403",
                            new ApiResponse()
                                .description("Permisos insuficientes"));
                    }

                    if (ruta.equals("/api/v1/auth/login")) {
                        respuestas.putIfAbsent("401",
                            new ApiResponse()
                                .description("Credenciales incorrectas"));
                    }

                    if (!http.equals("GET") || ruta.contains("{")) {
                        respuestas.putIfAbsent("400",
                            new ApiResponse()
                                .description("Solicitud invalida"));
                    }

                    if (ruta.contains("{") ||
                        ruta.equals("/api/v1/boletas")) {
                        respuestas.putIfAbsent("404",
                            new ApiResponse()
                                .description("Recurso no encontrado"));
                    }

                    if (!http.equals("GET") &&
                        !ruta.equals("/api/v1/auth/login")) {
                        respuestas.putIfAbsent("409",
                            new ApiResponse()
                                .description("Conflicto de datos"));
                    }
                });
            });
        };
    }
}
