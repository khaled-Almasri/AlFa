package de.unibremen.cs.swp.bokerfi.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfiguration der Sicherheitseinstellungen (Spring Security).
 * <p>
 * Definiert Filterketten, CORS-Regeln und Zugriffsberechtigungen.
 * </p>
 */
@Configuration
@EnableWebSecurity
@org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // NOSONAR: CSRF not needed for stateless REST API
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.setAllowedOrigins(java.util.List.of("http://localhost", "http://localhost:80", "http://localhost:4200"));
                corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                corsConfig.setAllowedHeaders(java.util.List.of("*"));
                return corsConfig;
            }))
// Add filter
            .addFilterBefore(new MockTokenFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                // Allow login and health
                .requestMatchers("/login", "/api/login", "/api/auth/login", "/auth/login", "/token", "/api/v1/auth/login").permitAll()
                .requestMatchers("/health", "/api/health", "/api/v1/health", "/actuator/**", "/error", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                // Require auth for everything else
                .anyRequest().authenticated()
            )
            .exceptionHandling(e -> e.authenticationEntryPoint(
                new org.springframework.security.web.authentication.HttpStatusEntryPoint(org.springframework.http.HttpStatus.UNAUTHORIZED)
            ));
        return http.build();
    }
}
