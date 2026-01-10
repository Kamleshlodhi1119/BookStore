//package com.bookstore.config;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
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
//import com.bookstore.security.CustomUserDetailsService;
//import com.bookstore.security.JwtAuthenticationFilter;
//
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//	private final JwtAuthenticationFilter jwtFilter;
//	private final CustomUserDetailsService userDetailsService;
//
////	public SecurityConfig(JwtAuthenticationFilter jwtFilter, CustomUserDetailsService userDetailsService) {
////		super();
////		this.jwtFilter = jwtFilter;
////		this.userDetailsService = userDetailsService;
////	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//		return authConfig.getAuthenticationManager();
//	}
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//	    http
//	        .csrf(csrf -> csrf.disable())
//	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//
//	        .sessionManagement(sm ->
//	            sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//	        )
//
//	        .authorizeHttpRequests(auth -> auth
//
//	            // ---------- PUBLIC (NO TOKEN REQUIRED) ----------
//	            .requestMatchers(
//	                "/api/auth/login",
//	                "/api/auth/register",
//	                "/api/auth/refresh-token",
//	                "/api/health",
//	                "/api/config",
//	                "/h2-console/**",
//
//	                // public browsing
//	                "/api/books/**",
//	                "/api/books/filter/**",
//	                "/api/authors/**",
//	                "/api/roles",
//	                
//	                "/swagger-ui.html/**",
//	                "/swagger-ui/index.html/**",
//	                "/swagger-ui/**",
//	                "/v3/api-docs/**",
//	                "/swagger-ui.html",
//	                "/api/admin/books/*/image/**",
//	                "/api/books/*/ratings/**",
//	                "/api/auth/me/**"
//	            ).permitAll()
//	            .requestMatchers("/api/books/**").permitAll()
//	            // ---------- USER + ADMIN ----------
//	            .requestMatchers(
//	                "/api/auth/me",
//	                "/api/auth/logout",
//	                "/api/auth/sessions/**",
//
//	                "/api/users/me/**",
//	                "/api/cart/**",
//	                "/api/cart/getcart/**",
//	                "/api/wishlist/**",
//	                "/api/ratings/**",
//
//	                "/api/orders/my",
//	                "/api/orders/{orderId}",
//	                "/swagger-ui/**",
//	                "/v3/api-docs/**",
//	                "/swagger-ui.html",
//	                "/api/books/*/rating",
//	                "/api/payments/**",
//	                "/api/books/*/rating"
//	            ).hasAnyRole("USER", "ADMIN")
//
//	            // ---------- ADMIN ONLY ----------
//	            .requestMatchers(
//	                "/api/admin/**",
//	                "/api/admin/books/**",
//	                "/api/admin/users/**",
//	                "/api/admin/books/*/image",
//	                "/api/books/byadmin/**"
//	            ).hasRole("ADMIN").requestMatchers(HttpMethod.POST, "/api/books/*/rating").hasAnyRole("USER", "ADMIN")
//
//	            // ---------- EVERYTHING ELSE ----------
//	            .anyRequest().authenticated()
//	        );
//
//	    // allow H2 console frames
//	    http.headers(headers ->
//	        headers.frameOptions(frame -> frame.disable())
//	    );
//
//	    // JWT filter
//	    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//	    return http.build();
//	}
//
//
////	@Bean
////	public CorsConfigurationSource corsConfigurationSource() {
////		CorsConfiguration configuration = new CorsConfiguration();
////		configuration.setAllowedOrigins(Arrays.asList("https://books-storeapp.netlify.app"));
////		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
////		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
////		configuration.setAllowCredentials(true);
////
////		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////		source.registerCorsConfiguration("/**", configuration);
////		return source;
////	}
//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//	    CorsConfiguration configuration = new CorsConfiguration();
//	    // VITAL: Ensure NO trailing slash and NO extra spaces
//	    configuration.setAllowedOrigins(List.of("https://books-storeapp.netlify.app")); 
//	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//	    configuration.setAllowedHeaders(List.of("*")); 
//	    configuration.setAllowCredentials(true);
//
//	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	    source.registerCorsConfiguration("/**", configuration);
//	    return source;
//	}
//}




package com.bookstore.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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

import com.bookstore.security.CustomUserDetailsService;
import com.bookstore.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Enable CORS and Disable CSRF
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())

            // 2. Stateless Session
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                // VITAL: Allow all Preflight (OPTIONS) requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ---------- PUBLIC (NO TOKEN REQUIRED) ----------
                .requestMatchers(
                    "/api/auth/login",
                    "/api/auth/register",
                    "/api/auth/refresh-token",
                    "/api/health",
                    "/api/config",
                    "/h2-console/**",
                    "/api/books/**",
                    "/api/authors/**",
                    "/api/roles",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // ---------- USER + ADMIN ----------
                .requestMatchers(
                    "/api/auth/me",
                    "/api/auth/logout",
                    "/api/users/me/**",
                    "/api/cart/**",
                    "/api/wishlist/**",
                    "/api/ratings/**",
                    "/api/orders/**",
                    "/api/payments/**"
                ).hasAnyRole("USER", "ADMIN")

                // ---------- ADMIN ONLY ----------
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // ---------- EVERYTHING ELSE ----------
                .anyRequest().authenticated()
            );

        // Allow H2 console frames
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        // Add JWT Filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Exact Netlify Origin (No trailing slash)
        configuration.setAllowedOrigins(List.of("https://books-storeapp.netlify.app")); 
        
        // Allow all standard methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Allow all headers to prevent Preflight failure
        configuration.setAllowedHeaders(List.of("*")); 
        
        // Essential for JWT/Auth headers
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}