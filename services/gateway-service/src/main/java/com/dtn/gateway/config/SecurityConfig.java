package com.dtn.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * DTN Compliance Platform - Security Configuration
 * Konfiguriert Demo-Benutzer und öffentliche Endpoints für Demos
 * 
 * @author Duc Thanh Nguyen
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Security Filter Chain Konfiguration
     * Erlaubt öffentlichen Zugriff auf Demo-Endpoints und Swagger UI
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Für Demo-Zwecke deaktiviert
            .authorizeHttpRequests(authz -> authz
                // Öffentliche Endpoints für Demos
                .requestMatchers("/", "/api/v1/demo/**", "/actuator/health").permitAll()
                
                // Swagger UI und API Docs öffentlich zugänglich
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .requestMatchers("/webjars/**", "/swagger-resources/**").permitAll()
                
                // Actuator Endpoints öffentlich (nur für Demo)
                .requestMatchers("/actuator/**").permitAll()
                
                // Statische Ressourcen
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                
                // Alle anderen Requests benötigen Authentifizierung
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/swagger-ui/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/swagger-ui/")
                .permitAll()
            )
            .httpBasic(httpBasic -> {});
        
        return http.build();
    }

    /**
     * Demo-Benutzer für Präsentationen
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails demoUser = User.builder()
                .username("demo")
                .password(passwordEncoder().encode("demo2024"))
                .roles("USER", "ADMIN")
                .build();
        
        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin2024"))
                .roles("ADMIN")
                .build();
        
        UserDetails dtnUser = User.builder()
                .username("dtn")
                .password(passwordEncoder().encode("compliance2024"))
                .roles("USER", "ADMIN", "COMPLIANCE_OFFICER")
                .build();

        return new InMemoryUserDetailsManager(demoUser, adminUser, dtnUser);
    }

    /**
     * Password Encoder für sichere Passwort-Speicherung
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}