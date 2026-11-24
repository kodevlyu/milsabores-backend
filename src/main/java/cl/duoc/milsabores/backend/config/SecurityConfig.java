package cl.duoc.milsabores.backend.config;

import cl.duoc.milsabores.backend.security.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final JwtAccessDeniedHandler deniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtFilter,
            JwtAuthenticationEntryPoint entryPoint,
            JwtAccessDeniedHandler deniedHandler) {

        this.jwtFilter = jwtFilter;
        this.entryPoint = entryPoint;
        this.deniedHandler = deniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .exceptionHandling(exception ->
                exception
                    .authenticationEntryPoint(entryPoint)
                    .accessDeniedHandler(deniedHandler)
            )

            .authorizeHttpRequests(auth -> auth

                // Documentacion Swagger y prueba de conexion.
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api/v1/prueba"
                ).permitAll()

                // Inicio de sesion y registro publico.
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/auth/login",
                    "/api/v1/auth/register",
                    "/api/v1/auth/refresh",
                    "/api/v1/auth/logout"
                ).permitAll()

                // Listado administrativo de productos.
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/productos/administracion"
                ).hasAnyRole("ADMIN", "VENDEDOR")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/categorias/administracion"
                ).hasRole("ADMIN")

                // Catalogo publico de productos y categorias.
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/productos",
                    "/api/v1/productos/**",
                    "/api/v1/categorias",
                    "/api/v1/categorias/**"
                ).permitAll()

                // Administracion de productos y categorias.
                .requestMatchers(
                    "/api/v1/productos",
                    "/api/v1/productos/**",
                    "/api/v1/categorias",
                    "/api/v1/categorias/**"
                ).hasRole("ADMIN")

                // Administracion de usuarios.
                .requestMatchers(
                    "/api/v1/usuarios",
                    "/api/v1/usuarios/**"
                ).hasRole("ADMIN")

                // Historial de compras del cliente.
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/boletas/mias"
                ).hasRole("CLIENTE")

                // Realizar compras.
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/boletas"
                ).hasRole("CLIENTE")

                // Consulta administrativa de ventas.
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/boletas",
                    "/api/v1/boletas/**"
                ).hasAnyRole("ADMIN", "VENDEDOR")

                // Bloquear cualquier otra solicitud.
                .anyRequest().denyAll()
            )

            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}
