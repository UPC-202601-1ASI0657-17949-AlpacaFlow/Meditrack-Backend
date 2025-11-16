package com.alpacaflow.meditrackplatform.shared.infrastructure.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * CORS Configuration.
 * <p>
 * This class configures Cross-Origin Resource Sharing (CORS) to allow requests from the frontend.
 * Currently allows requests from localhost:4200 (Angular development server).
 * </p>
 */
@Configuration
public class WebCorsConfiguration {

    /**
     * Creates a CORS filter that allows requests from the frontend.
     * <p>
     * This configuration allows:
     * - Origins: http://localhost:4200 (Angular dev server)
     * - Methods: GET, POST, PUT, DELETE, PATCH, OPTIONS
     * - Headers: All headers
     * - Credentials: Allowed
     * </p>
     *
     * @return The {@link CorsFilter} instance with CORS configuration
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow requests from Angular development server
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        
        // Allow all HTTP methods
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Allow all headers
        config.setAllowedHeaders(List.of("*"));
        
        // Allow credentials (cookies, authorization headers, etc.)
        config.setAllowCredentials(true);
        
        // Cache preflight response for 1 hour
        config.setMaxAge(3600L);
        
        // Apply CORS configuration to all paths
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

