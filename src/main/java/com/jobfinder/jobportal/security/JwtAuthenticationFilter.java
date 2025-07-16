package com.jobfinder.jobportal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        String token = getTokenFromRequest(wrappedRequest);

        if (token != null && tokenProvider.validateToken(token)) {
            String email = tokenProvider.getEmailFromToken(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, null);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(wrappedRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 🔎 Log JSON Body if it's a POST and Content-Type is application/json
        if ("POST".equalsIgnoreCase(wrappedRequest.getMethod()) &&
                "application/json".equalsIgnoreCase(wrappedRequest.getContentType())) {

            byte[] buf = wrappedRequest.getContentAsByteArray();
            String body = new String(buf, StandardCharsets.UTF_8);
            System.out.println("📦 JSON Payload: " + body);
        }

        filterChain.doFilter(wrappedRequest, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}


