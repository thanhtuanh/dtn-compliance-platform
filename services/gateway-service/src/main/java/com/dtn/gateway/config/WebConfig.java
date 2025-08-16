// services/gateway-service/src/main/java/com/dtn/gateway/config/WebConfig.java
package com.dtn.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lombok.extern.slf4j.Slf4j;

/**
 * Web MVC Configuration für DTN Gateway Service
 * 
 * Löst Swagger UI Routing-Probleme für Demo-Präsentationen:
 * - Automatische Weiterleitung von verschiedenen Swagger Pfaden
 * - Vereinheitlicht Swagger UI Zugriff für Bewerbungsgespräche
 * - Kompatibel mit bestehender CorsConfig
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        log.info("Konfiguriere Swagger UI View Controller für Demo-Readiness");
        
        // Swagger UI Routing - Löst das 404 Problem
        registry.addRedirectViewController("/", "/swagger-ui/index.html");
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html");
        registry.addRedirectViewController("/swagger-ui.html", "/swagger-ui/index.html");
        
        // API Docs Routing
        registry.addRedirectViewController("/api-docs", "/v3/api-docs");
        registry.addRedirectViewController("/docs", "/v3/api-docs");
        
        log.info("Swagger UI Routing konfiguriert:");
        log.info("  📚 Root: / → /swagger-ui/index.html");
        log.info("  📚 /swagger-ui → /swagger-ui/index.html");
        log.info("  📚 /swagger-ui.html → /swagger-ui/index.html");
        log.info("  📄 /api-docs → /v3/api-docs");
        log.info("✅ Demo-Ready: Alle Swagger Pfade funktionieren!");
    }
}