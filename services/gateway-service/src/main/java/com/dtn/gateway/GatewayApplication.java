package com.dtn.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DTN Compliance Intelligence Platform - Gateway Service
 * 
 * DSGVO + EU AI Act konforme KI-Lösung für deutsche Unternehmen
 * Entwickelt für Software-Dienstleister (50-200 MA) in München/Bayern
 * 
 * Business Value: 96.960€ Jahresersparnis durch Compliance-Automatisierung
 * Live-Demo: https://dtn-compliance.onrender.com/swagger-ui/
 * 
 * Gateway Features:
 * - API Gateway mit Routing zu allen Mikroservices
 * - JWT Authentication für sichere API-Zugriffe  
 * - Swagger UI Aggregation für Demo-Präsentationen
 * - Health Checks für Monitoring und Bewerbungsgespräche
 * - CORS Configuration für spätere Frontend-Integration
 * 
 * @author Duc Thanh Nguyen
 * @version 1.0.0
 * @since 2024-08
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("""
            🚀 DTN Compliance Gateway gestartet!
            
            ✅ Swagger UI: http://localhost:8080/swagger-ui/
            ✅ Health Check: http://localhost:8080/actuator/health
            ✅ Developer Info: http://localhost:8080/api/v1/gateway/developer-info
            
            💼 Bereit für Bewerbungsgespräche und Live-Demos!
            """);
    }
}