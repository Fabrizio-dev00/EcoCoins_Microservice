package com.ecocoins.ecocoins_microservice.config;

import com.ecocoins.ecocoins_microservice.security.FirebaseAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;

    public SecurityConfig(FirebaseAuthenticationFilter firebaseAuthenticationFilter) {
        this.firebaseAuthenticationFilter = firebaseAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // ========== RUTAS PÚBLICAS (SIN AUTENTICACIÓN) ==========
                        .requestMatchers(
                                // Autenticación
                                "/api/auth/**",

                                // Endpoints públicos existentes
                                "/api/recompensas",
                                "/api/estadisticas",
                                "/api/estadisticas/**",
                                "/api/reciclajes/validar-ia",

                                // Documentación Swagger
                                "/swagger-ui/**",
                                "/v3/api-docs/**",

                                // Actuator y errores
                                "/actuator/**",
                                "/error"
                        ).permitAll()

                        // ========== RUTAS PROTEGIDAS - FASE 2: GAMIFICACIÓN ==========
                        .requestMatchers(
                                "/api/ranking/**",
                                "/api/logros/**",
                                "/api/estadisticas/detalladas/**"
                        ).authenticated()

                        // ========== RUTAS PROTEGIDAS - FASE 3: COMUNIDAD ==========
                        .requestMatchers(
                                "/api/notificaciones/**",
                                "/api/referidos/**",
                                "/api/mapa/**"
                        ).authenticated()

                        // ========== RUTAS PROTEGIDAS - FASE 4: EDUCACIÓN Y SOPORTE ==========
                        .requestMatchers(
                                "/api/educacion/**",
                                "/api/soporte/**"
                        ).authenticated()

                        // ========== RUTAS EXISTENTES (YA PROTEGIDAS) ==========
                        .requestMatchers(
                                "/api/usuarios/**",
                                "/api/reciclajes/**",
                                "/api/canjes/**",
                                "/api/configuracion/**"
                        ).authenticated()

                        // Todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        firebaseAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ CORREGIDO: Especificar orígenes explícitos
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:8081",
                "http://10.0.2.2:8080"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
