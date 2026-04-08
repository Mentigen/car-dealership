package ru.CarDealership.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import ru.CarDealership.api.exception.ErrorResponse;

import java.time.Instant;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final KeycloakJwtConverter keycloakJwtConverter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/actuator/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter))
                        .authenticationEntryPoint((request, response, ex) -> {
                            try { writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", ex.getMessage()); }
                            catch (Exception e) { response.sendError(HttpServletResponse.SC_UNAUTHORIZED); }
                        })
                        .accessDeniedHandler((request, response, ex) -> {
                            try { writeError(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden", ex.getMessage()); }
                            catch (Exception e) { response.sendError(HttpServletResponse.SC_FORBIDDEN); }
                        })
                );
        return http.build();
    }

    private void writeError(HttpServletResponse response, int status, String error, String message) throws Exception {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(),
                new ErrorResponse(status, error, message, Instant.now()));
    }
}
