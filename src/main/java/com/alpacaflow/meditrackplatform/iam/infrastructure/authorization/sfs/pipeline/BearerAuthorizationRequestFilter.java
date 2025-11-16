package com.alpacaflow.meditrackplatform.iam.infrastructure.authorization.sfs.pipeline;

import com.alpacaflow.meditrackplatform.iam.infrastructure.authorization.sfs.model.UsernamePasswordAuthenticationTokenBuilder;
import com.alpacaflow.meditrackplatform.iam.infrastructure.tokens.jwt.BearerTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BearerAuthorizationRequestFilter.class);
    private final BearerTokenService tokenService;
    private final UserDetailsService userDetailsService;

    public BearerAuthorizationRequestFilter(BearerTokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // Skip authentication filter for public endpoints
            String path = request.getRequestURI();
            if (path.startsWith("/api/v1/authentication/") || 
                path.startsWith("/temp-api/") || 
                path.startsWith("/swagger-") || 
                path.startsWith("/v3/api-docs")) {
                LOGGER.info("Skipping authentication for public endpoint: {}", path);
                filterChain.doFilter(request, response);
                return;
            }

            String token = tokenService.getBearerTokenFrom(request);
            LOGGER.info("Token: {}", token);
            if (token != null && tokenService.validateToken(token)) {
                String email = tokenService.getUsernameFromToken(token); // Email is used as username
                var userDetails = userDetailsService.loadUserByUsername(email);
                SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationTokenBuilder.build(userDetails, request));
            } else {
                LOGGER.info("Token is not valid");
            }

        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}

