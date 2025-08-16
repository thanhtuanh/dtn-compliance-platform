package com.dtn.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lombok.extern.slf4j.Slf4j;

/**
 * CORS Configuration für DTN Compliance Platform
 * 
 * Ermöglicht sichere Frontend-Integration für:
 * - Angular/React Dashboards
 * - Mobile Apps
 * - Third-Party Integrationen
 * 
 * DSGVO-konform: Kontrollierte Cross-Origin Zugriffe
 * Frontend URL aus .env: ${FRONTEND_URL}
 */
@Configuration
@Slf4j
public class CorsConfig implements WebMvcConfigurer {

    @Value("${FRONTEND_URL:https://dtn-frontend.onrender.com}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Konfiguriere CORS Mappings für Frontend-Integration");
        
        registry.addMapping("/**")
            // Erlaubte Origins aus .env + Development
            .allowedOriginPatterns(
                "http://localhost:3000",           // React Development
                "http://localhost:4200",           // Angular Development
                frontendUrl,                       // Production Frontend aus .env
                "http://127.0.0.1:*",             // Local Development
                "https://*.onrender.com"           // Render.com Deployments
            )
            
            // HTTP-Methoden
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
            
            // Request Headers
            .allowedHeaders(
                "Authorization", 
                "Content-Type", 
                "X-Requested-With", 
                "Accept", 
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
            )
            
            // Response Headers (für Frontend-Zugriff)
            .exposedHeaders("X-Total-Count", "X-Page-Count", "Authorization")
            
            // Credentials für JWT-Token (Production-ready)
            .allowCredentials(true)
            
            // Cache-Zeit für Preflight Requests
            .maxAge(3600);
        
        log.info("CORS Configuration erfolgreich geladen:");
        log.info("  📱 React Development: http://localhost:3000");
        log.info("  🅰️ Angular Development: http://localhost:4200");
        log.info("  🌐 Production Frontend: {}", frontendUrl);
        log.info("  🎪 Demo: *.onrender.com Pattern");
        log.info("  🔐 Credentials: Enabled für JWT-Token");
        
        log.info("Frontend-Integration ready für Bewerbungsdemos!");
    }
}