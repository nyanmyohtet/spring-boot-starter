package com.nyanmyohtet.springbootstarter.config;

import com.nyanmyohtet.springbootstarter.security.JwtAuthFilter;
import com.nyanmyohtet.springbootstarter.security.RateLimitFilter;
import com.nyanmyohtet.springbootstarter.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${cors.allowed-origin}")
    private String ALLOWED_ORIGINS;

    private final RateLimitFilter rateLimitFilter;
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    private final String[] WHITE_LIST_URLS = {
            "/swagger-ui/**",        // swagger
            "/swagger-ui**",         // swagger
            "/swagger-ui.html",      // swagger
            "/swagger-resources/**", // swagger
            "/v3/api-docs/**",       // swagger
            "/v3/api-docs.yaml",     // swagger
            "/api/v1/auth/**"};

    private final List<String> CORS_ALLOWED_METHODS = Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    private final List<String> CORS_ALLOWED_HEADERS = Arrays.asList("Authorization", "Content-Type");
    private final List<String> CORS_EXPOSED_HEADERS = List.of("Authorization");

    // configure web security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // disable CSRF, JWT is immune to it
        return http.csrf(AbstractHttpConfigurer::disable)
                // Set permissions on endpoints
                .authorizeHttpRequests(auth -> auth.requestMatchers(WHITE_LIST_URLS)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                // Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                // Add filters
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitFilter, JwtAuthFilter.class)
                .build();
    }

    // explicitly expose AuthenticationManager as a bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // configure authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // setting password-encoding schema
    // and, expose as bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Used by Spring Security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(ALLOWED_ORIGINS);
        config.setAllowedMethods(CORS_ALLOWED_METHODS);
        config.setAllowedHeaders(CORS_ALLOWED_HEADERS);
        config.setExposedHeaders(CORS_EXPOSED_HEADERS);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
