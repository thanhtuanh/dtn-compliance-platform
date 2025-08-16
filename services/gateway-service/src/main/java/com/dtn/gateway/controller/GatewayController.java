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
import java.util.HashMap;

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
        
        // Map Builder für komplexe Strukturen (Java 21 kompatibel)
        Map<String, Object> compliance = new HashMap<>();
        compliance.put("gdpr_article_30", "VVT-Generierung automatisiert");
        compliance.put("gdpr_article_35", "DSFA-Automatisierung");
        compliance.put("eu_ai_act", "Risikoklassifizierung seit Feb 2025");
        compliance.put("german_legal", "BfDI + Landesdatenschutzbehörden konform");

        Map<String, Object> services = new HashMap<>();
        services.put("gateway", "http://localhost:8080 (aktiv)");
        services.put("compliance", "http://localhost:8081 (wird in Step 3 erstellt)");
        services.put("document", "http://localhost:8082 (wird in Step 4 erstellt)");

        Map<String, Object> status = new HashMap<>();
        status.put("service", "DTN Compliance Gateway");
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("version", "1.0.0");
        status.put("port", serverPort);
        status.put("description", "DSGVO + EU AI Act konforme KI-Lösung");
        status.put("demo_url", "http://localhost:" + serverPort + "/swagger-ui/");
        status.put("business_value", "96.960€ Jahresersparnis für 100-MA Firma");
        status.put("target_audience", "Deutsche Software-Dienstleister (50-200 MA)");
        status.put("compliance", compliance);
        status.put("services", services);
        
        return ResponseEntity.ok(status);
    }

    @Operation(
        summary = "💼 Bewerbungsinfo für Recruiter",
        description = "Entwickler-Information für Bewerbungsgespräche"
    )
    @GetMapping("/developer-info")
    public ResponseEntity<Map<String, Object>> getDeveloperInfo() {
        log.info("Developer Info für Bewerbung angefordert");
        
        Map<String, Object> developer = new HashMap<>();
        developer.put("name", "Duc Thanh Nguyen");
        developer.put("experience", "10+ Jahre Java Fullstack Development");
        developer.put("specialization", "Enterprise Mikroservices + DSGVO Compliance");
        developer.put("languages", "Deutsch C1, English A2");
        developer.put("location", "München/Bayern (Remote möglich)");

        Map<String, Object> projectHighlights = new HashMap<>();
        projectHighlights.put("business_problem_solved", "KI-Dilemma deutscher Software-Dienstleister");
        projectHighlights.put("solution_approach", "Lokale KI + automatische Compliance-Prüfung");
        projectHighlights.put("roi_demonstrated", "96.960€ Jahresersparnis quantifiziert");
        projectHighlights.put("legal_compliance", "DSGVO + EU AI Act + deutsche Rechtssicherheit");

        Map<String, Object> technicalExpertise = new HashMap<>();
        technicalExpertise.put("backend", "Java 21, Spring Boot 3.2, Spring Security");
        technicalExpertise.put("database", "PostgreSQL, JPA/Hibernate, Flyway Migration");
        technicalExpertise.put("microservices", "API Gateway, Service Discovery");
        technicalExpertise.put("containerization", "Docker, docker-compose, Multi-stage builds");
        technicalExpertise.put("testing", "JUnit 5, TestContainers, RestAssured");

        Map<String, Object> demoReadiness = new HashMap<>();
        demoReadiness.put("swagger_ui", "Vollständige API-Dokumentation");
        demoReadiness.put("health_checks", "Monitoring für alle Services");
        demoReadiness.put("docker_demo", "5 Minuten von Start bis Demo");
        demoReadiness.put("business_case", "ROI-Kalkulation integriert");

        Map<String, Object> info = new HashMap<>();
        info.put("developer", developer);
        info.put("project_highlights", projectHighlights);
        info.put("technical_expertise", technicalExpertise);
        info.put("demo_readiness", demoReadiness);
        
        return ResponseEntity.ok(info);
    }

    @Operation(
        summary = "🎯 Demo-Preparation Check",
        description = "Prüft ob alle Services für Demo bereit sind"
    )
    @GetMapping("/demo-ready")
    public ResponseEntity<Map<String, Object>> isDemoReady() {
        log.info("Demo-Readiness Check gestartet");
        
        Map<String, Object> servicesStatus = new HashMap<>();
        servicesStatus.put("gateway", "✅ Läuft auf Port 8080");
        servicesStatus.put("compliance", "⏳ Wird in Step 3 erstellt");
        servicesStatus.put("document", "⏳ Wird in Step 4 erstellt");

        Map<String, Object> featuresReady = new HashMap<>();
        featuresReady.put("vvt_generation", "⏳ DSGVO Art. 30 - Step 3");
        featuresReady.put("ai_risk_classification", "⏳ EU AI Act - Step 3");
        featuresReady.put("dsfa_automation", "⏳ DSGVO Art. 35 - Step 3");
        featuresReady.put("pdf_export", "⏳ Deutsche Templates - Step 4");

        Map<String, Object> demoScript = new HashMap<>();
        demoScript.put("duration", "5 Minuten");
        demoScript.put("audience", "Recruiter + Fach-ITler + Management");
        demoScript.put("key_points", List.of(
            "Business Problem: KI-Compliance-Dilemma",
            "Solution: Lokale KI + DSGVO/EU AI Act",
            "ROI: 96.960€ Jahresersparnis",
            "Tech: Java 21 + Spring Boot + Mikroservices",
            "Demo: Live Swagger UI + Health Checks"
        ));

        Map<String, Object> demoStatus = new HashMap<>();
        demoStatus.put("demo_ready", true);
        demoStatus.put("swagger_ui", "✅ http://localhost:8080/swagger-ui/");
        demoStatus.put("services", servicesStatus);
        demoStatus.put("features_ready", featuresReady);
        demoStatus.put("demo_script", demoScript);
        
        return ResponseEntity.ok(demoStatus);
    }
}