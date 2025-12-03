package com.ecocoins.ecocoins_microservice.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ AGREGAR ESTE LOG
        log.info("🔍 Procesando ruta: {} - Method: {}", path, request.getMethod());

        // Rutas públicas que NO requieren autenticación
        if (path.startsWith("/api/auth/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs/") ||
                path.startsWith("/actuator/") ||
                path.equals("/error")) {

            log.debug("✅ Ruta pública: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener token del header Authorization
        String authHeader = request.getHeader("Authorization");

        // ✅ AGREGAR ESTE LOG
        log.info("🔑 Authorization header: {}", authHeader != null ? "PRESENTE" : "❌ AUSENTE");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // ✅ AGREGAR ESTE LOG
            log.info("🎫 Token extraído (primeros 50 chars): {}", token.substring(0, Math.min(50, token.length())));

            try {
                // Verificar token con Firebase Admin SDK
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

                String uid = decodedToken.getUid();
                String email = decodedToken.getEmail();

                log.info("✅ Usuario autenticado: {} (UID: {})", email, uid);

                // Crear autenticación de Spring Security
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.emptyList()
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                log.error("❌ Token inválido: {}", e.getMessage());
            }
        } else {
            // ✅ AGREGAR ESTE LOG
            log.warn("⚠️ No se encontró token de autenticación para ruta protegida: {}", path);
        }

        filterChain.doFilter(request, response);
    }
}