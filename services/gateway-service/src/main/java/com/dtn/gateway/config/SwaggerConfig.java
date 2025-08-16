package com.dtn.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 3 Konfiguration für DTN Compliance Platform
 * 
 * Optimiert für:
 * - Bewerbungsgespräche und Live-Demos
 * - Recruiter-freundliche API-Dokumentation
 * - Technical Interview Präsentationen
 * - Customer Demo Sessions
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(apiInfo())
            .servers(serverList())
            .addSecurityItem(securityRequirement())
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("bearerAuth", securityScheme()));
    }

    private Info apiInfo() {
        return new Info()
            .title("🚀 DTN Compliance Intelligence Platform")
            .description("""
                **DSGVO + EU AI Act konforme KI-Lösung für deutsche Unternehmen**
                
                ### 🎯 Business Value:
                - **96.960€ Jahresersparnis** für 100-MA Software-Firma
                - **DSGVO Art. 30**: Automatische VVT-Generierung
                - **DSGVO Art. 35**: DSFA-Automatisierung
                - **EU AI Act**: KI-Risikoklassifizierung seit Feb 2025
                
                ### 🏢 Zielgruppe:
                Software-Dienstleister (50-200 MA) in München/Bayern
                
                ### 🇩🇪 Deutsche Rechtssicherheit:
                - Lokale KI-Verarbeitung (Privacy by Design)
                - BfDI + Landesdatenschutzbehörden konform
                - Deutsche Server (Frankfurt/München)
                
                ### 💻 Tech Stack:
                Java 21, Spring Boot 3.2, PostgreSQL, Docker, Ollama
                
                ### 🎪 Live-Demo:
                Perfekt für Bewerbungsgespräche und Kundenpräsentationen
                """)
            .version("1.0.0")
            .contact(new Contact()
                .name("Duc Thanh Nguyen")
                .email("n.thanh@gmx.de")
                .url("https://linkedin.com/in/duc-thanh-nguyen-55aa5941"))
            .license(new License()
                .name("MIT License")
                .url("https://github.com/thanhtuanh/dtn-compliance-platform/blob/main/LICENSE"));
    }

    private List<Server> serverList() {
        return List.of(
            new Server()
                .url("http://localhost:8080")
                .description("🖥️ Local Development Server"),
            new Server()
                .url("https://dtn-compliance.onrender.com")
                .description("🌐 Live Demo Server (Bewerbungen)"),
            new Server()
                .url("https://staging-dtn-compliance.onrender.com")
                .description("🧪 Staging Environment")
        );
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("bearerAuth");
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
            .name("bearerAuth")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("""
                JWT Token für sichere API-Zugriffe.                
                **Für Demo-Zwecke:** Öffentliche Endpunkte verfügbar
                **Für Produktion:** JWT Token über Authentication Service
                """);
    }
}