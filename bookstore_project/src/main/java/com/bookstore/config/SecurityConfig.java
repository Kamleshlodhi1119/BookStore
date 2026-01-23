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
//@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
		super();
		this.jwtFilter = jwtFilter;
	}

	// -------------------------
    // PASSWORD ENCODER
    // -------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // -------------------------
    // AUTH MANAGER
    // -------------------------
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // -------------------------
    // SECURITY FILTER CHAIN
    // -------------------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ✅ CORS enabled (uses corsConfigurationSource bean)
            .cors(Customizer.withDefaults())

            // ❌ CSRF not needed (JWT)
            .csrf(csrf -> csrf.disable())

            // ❌ Session not needed (JWT)
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                // ✅ Allow preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ---------- PUBLIC APIs ----------
                .requestMatchers(
                    "/api/auth/**",
                    "/api/books/**",
                    "/api/authors/**",
                    "/api/roles",
                    "/api/health",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                .requestMatchers("/images/**").permitAll()

                // ---------- USER / ADMIN ----------
                .requestMatchers(
                    "/api/cart/**",
                    "/api/wishlist/**",
                    "/api/orders/**",
                    "/api/ratings/**",
                    "/api/payments/**",
                    "/api/users/me/**",
                    "/api/admin/users/getall/**"
                ).hasAnyRole("USER", "ADMIN")

                // ---------- ADMIN ONLY ----------
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // ---------- EVERYTHING ELSE ----------
                .anyRequest().authenticated()
            );

        // ✅ JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // -------------------------
    // GLOBAL CORS CONFIG
    // -------------------------
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration config = new CorsConfiguration();
//
//        // ✅ ONLY Render UI allowed
//        config.addAllowedOrigin("https://book-store-ui-xuao.onrender.com");
//
//        // (optional dev support)
//        // config.addAllowedOrigin("http://localhost:4200");
//
//        config.setAllowedMethods(
//            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
//        );
//
//        config.setAllowedHeaders(List.of("*"));
//        config.setExposedHeaders(List.of("Authorization"));
//
//        // 🔥 JWT header auth → cookies not needed
//        config.setAllowCredentials(false);
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }
    
    
    
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ ALLOWED ORIGINS
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://books-storeapp.netlify.app",
                "https://book-store-ui-xuao.onrender.com"
        ));

        // ✅ ALLOWED METHODS
        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        );

        // ✅ ALLOWED HEADERS
        config.setAllowedHeaders(List.of("*"));

        // ✅ EXPOSE JWT HEADER
        config.setExposedHeaders(List.of("Authorization"));

        // 🔥 JWT in header → NO cookies
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
