package com.jobfinder.jobportal.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    // 🔐 Security rules για Spring Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(csrfAccessDeniedHandler()) // 👈 εδω μπαίνει σωστά!
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/csrf-token").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    // 🔐 Κρυπτογράφηση κωδικών με BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔧 RequestContext για σωστή session διαχείριση
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    // 🌐 CORS για frontend επικοινωνία με backend
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    // 🧪 Προαιρετική καταγραφή για CSRF απορρίψεις
    public AccessDeniedHandler csrfAccessDeniedHandler() {
        return (request, response, ex) -> {
            System.out.println("⛔ CSRF block: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF error");
        };
    }
}



//.ignoringRequestMatchers("/api/auth/register") // 👈 Δοκιμαστικά να αγνοείται το CSRF για αυτό
//.ignoringRequestMatchers("/api/auth/register", "/api/auth/login")



