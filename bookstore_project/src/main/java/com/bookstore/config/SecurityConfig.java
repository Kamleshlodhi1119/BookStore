package com.bookstore.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

import com.bookstore.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    // read from application.properties (fallback to "*" if missing)
    @Value("${bookstore.frontend.url:*}")
    private String frontendUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())

            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ---------- PUBLIC ----------
                .requestMatchers(
                    "/api/auth/**",
                    "/api/books/**",
                    "/api/authors/**",
                    "/api/roles",
                    "/api/health",
                    "/api/config",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/h2-console/**"
                ).permitAll()

                // ---------- USER + ADMIN ----------
                .requestMatchers(
                    "/api/cart/**",
                    "/api/wishlist/**",
                    "/api/orders/**",
                    "/api/ratings/**",
                    "/api/payments/**",
                    "/api/users/me/**"
                ).hasAnyRole("USER", "ADMIN")

                // ---------- ADMIN ----------
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )

            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Render / proxy safe CORS config.
     *
     * Important:
     * - For JWT sent in Authorization header we do NOT need credentials (cookies).
     * - Using addAllowedOriginPattern avoids exact-origin mismatch that can happen behind proxies.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // If frontendUrl is set to "*" in properties, or is empty, allow any origin.
        // Otherwise allow the exact pattern provided (works with Render proxies).
        if (frontendUrl != null && !frontendUrl.isBlank() && !"*".equals(frontendUrl.trim())) {
            // allow the configured frontend (pattern style)
            config.addAllowedOriginPattern(frontendUrl.trim());
        } else {
            // fallback (dev / missing config)
            config.addAllowedOriginPattern("*");
        }

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization")); // keep JWT header visible to browser
        // IMPORTANT: wildcard origins only accepted by browsers when allowCredentials is false
        // JWT is header-based, so disabling credentials is correct and safer here.
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
