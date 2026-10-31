package com.ecommerce.dmart.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.dmart.exception.EntityNotFoundException;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No Bearer token: continue the filter chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // Check for empty token
        if (token.isBlank()) {
            sendUnauthorizedResponse(response);
            return;
        }

        try {

            // Extract username from JWT
            String username = jwtUtil.extractUsername(token);

            if (username == null || username.isBlank()) {
                sendUnauthorizedResponse(response);
                return;
            }

            // Authenticate only if no authentication exists already
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // Validate JWT
                if (jwtUtil.validateToken(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);

                } else {
                    sendUnauthorizedResponse(response);
                    return;
                }
            }

        } catch (JwtException | IllegalArgumentException e) {

            // Invalid, expired or malformed JWT
            sendUnauthorizedResponse(response);
            return;

        } catch (EntityNotFoundException e) {

            // JWT refers to a user who no longer exists
            sendUnauthorizedResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    // Return consistent JSON response for invalid JWT
    private void sendUnauthorizedResponse(
            HttpServletResponse response) throws IOException {

        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "{\"status\":401,\"error\":\"Unauthorized\","
                + "\"message\":\"Invalid or expired JWT token\"}"
        );
    }
}
