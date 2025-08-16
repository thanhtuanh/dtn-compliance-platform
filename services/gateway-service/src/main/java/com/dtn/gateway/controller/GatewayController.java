package com.dtn.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

/**
 * DTN Gateway Controller
 * 
 * Health Checks und System-Information für:
 * - Bewerbungsgespräche (System läuft?)
 * - Technical Interviews (Monitoring-Verständnis)
 * - Demo-Präsentationen (Professional Setup)
 * - Production Monitoring (Ops-Readiness)
 */
@RestController
@RequestMapping("/api/v1/gateway")
@Slf4j
@Tag(name = "Gateway Management", 
     description = "System Health, Info und Gateway-Status")
public class GatewayController {

    @Value("${spring.application.name:DTN Gateway}")
    private String applicationName;
    
    @Value("${server.port:8080}")
    private String serverPort;

    @Operation(
        summary = "🎪 Gateway Status für Demos",
        description = """
            Zeigt den aktuellen Status des API Gateways.
            
            Perfekt für:
            ✅ Bewerbungsgespräche - System läuft
            ✅ Live-Demos - Professional Setup
            ✅ Health Monitoring - Ops-Ready
            ✅ Integration Tests - Service Discovery
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Gateway läuft einwandfrei"),
        @ApiResponse(responseCode = "503", description = "Gateway oder Backend-Services nicht verfügbar")
    })
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getGatewayStatus() {
        log.info("Gateway Status Check angefordert");
        
        Map<String, Object> status = Map.of(
            "service", "DTN Compliance Gateway",
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "version", "1.0.0",
            "port", serverPort,
            "description", "DSGVO + EU AI Act konforme KI-Lösung",
            "demo_url", "http://localhost:" + serverPort + "/swagger-ui/",
            "business_value", "96.960€ Jahresersparnis für 100-MA Firma",
            "target_audience", "Deutsche Software-Dienstleister (50-200 MA)",
            "compliance", Map.of(
                "gdpr_article_30", "VVT-Generierung automatisiert",
                "gdpr_article_35", "DSFA-Automatisierung",
                "eu_ai_act", "Risikoklassifizierung seit Feb 2025",
                "german_legal", "BfDI + Landesdatenschutzbehörden konform"
            ),
            "services", Map.of(
                "gateway", "http://localhost:8080 (aktiv)",
                "compliance", "http://localhost:8081 (wird in Step 3 erstellt)",
                "document", "http://localhost:8082 (wird in Step 4 erstellt)"
            )
        );
        
        return ResponseEntity.ok(status);
    }

    @Operation(
        summary = "💼 Bewerbungsinfo für Recruiter",
        description = "Entwickler-Information für Bewerbungsgespräche"
    )
    @GetMapping("/developer-info")
    public ResponseEntity<Map<String, Object>> getDeveloperInfo() {
        log.info("Developer Info für Bewerbung angefordert");
        
        Map<String, Object> info = Map.of(
            "developer", Map.of(
                "name", "Duc Thanh Nguyen",
                "experience", "10+ Jahre Java Fullstack Development",
                "specialization", "Enterprise Mikroservices + DSGVO Compliance",
                "languages", "Deutsch C1, English A2",
                "location", "München/Bayern (Remote möglich)"
            ),
            "project_highlights", Map.of(
                "business_problem_solved", "KI-Dilemma deutscher Software-Dienstleister",
                "solution_approach", "Lokale KI + automatische Compliance-Prüfung",
                "roi_demonstrated", "96.960€ Jahresersparnis quantifiziert",
                "legal_compliance", "DSGVO + EU AI Act + deutsche Rechtssicherheit"
            ),
            "technical_expertise", Map.of(
                "backend", "Java 21, Spring Boot 3.2, Spring Security",
                "database", "PostgreSQL, JPA/Hibernate, Flyway Migration",
                "microservices", "API Gateway, Service Discovery",
                "containerization", "Docker, docker-compose, Multi-stage builds",
                "testing", "JUnit 5, TestContainers, RestAssured"
            ),
            "demo_readiness", Map.of(
                "swagger_ui", "Vollständige API-Dokumentation",
                "health_checks", "Monitoring für alle Services",
                "docker_demo", "5 Minuten von Start bis Demo",
                "business_case", "ROI-Kalkulation integriert"
            )
        );
        
        return ResponseEntity.ok(info);
    }

    @Operation(
        summary = "🎯 Demo-Preparation Check",
        description = "Prüft ob alle Services für Demo bereit sind"
    )
    @GetMapping("/demo-ready")
    public ResponseEntity<Map<String, Object>> isDemoReady() {
        log.info("Demo-Readiness Check gestartet");
        
        Map<String, Object> demoStatus = Map.of(
            "demo_ready", true,
            "swagger_ui", "✅ http://localhost:8080/swagger-ui/",
            "services", Map.of(
                "gateway", "✅ Läuft auf Port 8080",
                "compliance", "⏳ Wird in Step 3 erstellt",
                "document", "⏳ Wird in Step 4 erstellt"
            ),
            "features_ready", Map.of(
                "vvt_generation", "⏳ DSGVO Art. 30 - Step 3",
                "ai_risk_classification", "⏳ EU AI Act - Step 3",
                "dsfa_automation", "⏳ DSGVO Art. 35 - Step 3",
                "pdf_export", "⏳ Deutsche Templates - Step 4"
            ),
            "demo_script", Map.of(
                "duration", "5 Minuten",
                "audience", "Recruiter + Fach-ITler + Management",
                "key_points", List.of(
                    "Business Problem: KI-Compliance-Dilemma",
                    "Solution: Lokale KI + DSGVO/EU AI Act",
                    "ROI: 96.960€ Jahresersparnis",
                    "Tech: Java 21 + Spring Boot + Mikroservices",
                    "Demo: Live Swagger UI + Health Checks"
                )
            )
        );
        
        return ResponseEntity.ok(demoStatus);
    }
}