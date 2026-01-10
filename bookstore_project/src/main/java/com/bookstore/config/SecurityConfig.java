//package com.bookstore.config;
//
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import com.bookstore.security.JwtAuthenticationFilter;
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtFilter;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//            // 🔥 THIS IS CRITICAL
//            .cors(Customizer.withDefaults())
//            .csrf(csrf -> csrf.disable())
//
//            .sessionManagement(sm ->
//                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//
//            .authorizeHttpRequests(auth -> auth
//
//                // 🔥 MUST allow preflight
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                // ---------- PUBLIC ----------
//                .requestMatchers(
//                    "/api/auth/**",
//                    "/api/books/**",
//                    "/api/authors/**",
//                    "/api/roles",
//                    "/api/health",
//                    "/api/config",
//                    "/v3/api-docs/**",
//                    "/swagger-ui/**",
//                    "/swagger-ui.html",
//                    "/h2-console/**"
//                ).permitAll()
//
//                // ---------- USER + ADMIN ----------
//                .requestMatchers(
//                    "/api/cart/**",
//                    "/api/wishlist/**",
//                    "/api/orders/**",
//                    "/api/ratings/**",
//                    "/api/payments/**",
//                    "/api/users/me/**"
//                ).hasAnyRole("USER", "ADMIN")
//
//                // ---------- ADMIN ----------
//                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//
//                .anyRequest().authenticated()
//            )
//
//            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
//
//        // JWT AFTER CORS
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    // 🌍 GLOBAL CORS CONFIG
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowedOrigins(
//            List.of("https://book-store-ui-xuao.onrender.com")
//        );
//        config.setAllowedMethods(
//            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
//        );
//        config.setAllowedHeaders(List.of("*"));
//        config.setExposedHeaders(List.of("Authorization"));
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//}



package com.bookstore.config;

import java.util.List;

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
            // 🔥 Spring Security must handle CORS
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())

            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                // 🔥 Preflight MUST pass
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

        // JWT must run AFTER CORS
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🌍 GLOBAL CORS CONFIG — FIXED
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ REQUIRED when allowCredentials(true) is used
        config.setAllowedOriginPatterns(
            List.of("https://book-store-ui-xuao.onrender.com")
        );

        // ✅ Allow all HTTP methods incl. OPTIONS
        config.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        );

        // ✅ Explicit headers for preflight
        config.setAllowedHeaders(
            List.of(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin"
            )
        );

        // ✅ JWT needs this
        config.setExposedHeaders(List.of("Authorization"));

        // ✅ Required for Bearer tokens
        config.setAllowCredentials(true);

        // ✅ Reduce preflight calls
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
