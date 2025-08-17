package com.dtn.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * DTN Compliance Platform - Demo Security Configuration
 * Deaktiviert Authentifizierung für Demo-Präsentationen
 * 
 * ⚠️ NUR FÜR DEMO-ZWECKE - NICHT IN PRODUCTION VERWENDEN!
 * 
 * @author Duc Thanh Nguyen
 */
@Configuration
@EnableWebSecurity
@Profile({"demo", "docker", "default"})
public class DemoSecurityConfig {

    /**
     * Security Filter Chain für Demo-Modus
     * Erlaubt öffentlichen Zugriff auf alle Endpoints
     */
    @Bean
    public SecurityFilterChain demoFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll() // Alle Requests öffentlich zugänglich
            )
            .formLogin(form -> form.disable()) // Login-Form deaktiviert
            .httpBasic(basic -> basic.disable()) // HTTP Basic Auth deaktiviert
            .logout(logout -> logout.disable()); // Logout deaktiviert
        
        return http.build();
    }
}