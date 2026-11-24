package cl.duoc.milsabores.backend.config;

import cl.duoc.milsabores.backend.security.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
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

                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api/v1/prueba"
                ).permitAll()

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/auth/login",
                    "/api/v1/auth/register"
                ).permitAll()

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/productos",
                    "/api/v1/productos/**",
                    "/api/v1/categorias",
                    "/api/v1/categorias/**"
                ).permitAll()

                .requestMatchers(
                    "/api/v1/productos",
                    "/api/v1/productos/**",
                    "/api/v1/categorias",
                    "/api/v1/categorias/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    "/api/v1/usuarios",
                    "/api/v1/usuarios/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/boletas/mias"
                ).hasRole("CLIENTE")

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/boletas"
                ).hasRole("CLIENTE")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/boletas",
                    "/api/v1/boletas/**"
                ).hasAnyRole("ADMIN", "VENDEDOR")

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