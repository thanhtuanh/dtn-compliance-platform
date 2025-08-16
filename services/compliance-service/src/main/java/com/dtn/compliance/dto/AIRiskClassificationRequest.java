package com.dtn.compliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

/**
 * AI Risk Classification Request DTO
 * 
 * Request-Parameter für automatische KI-Risikoklassifizierung nach EU AI Act
 * Implementiert seit Februar 2025 geltende EU-Verordnung
 * 
 * Business Value:
 * - Compliance-Sicherheit: EU AI Act seit Feb 2025 Pflicht
 * - Kostenersparnis: 21.000€ pro Jahr durch Automatisierung
 * - CE-Kennzeichnung Vorbereitung automatisiert
 * - Prohibited Practices Check integriert
 * 
 * EU AI Act Risikoklassen:
 * - UNACCEPTABLE_RISK: Verbotene KI-Praktiken
 * - HIGH_RISK: CE-Kennzeichnung + Konformitätsbewertung
 * - LIMITED_RISK: Transparenz-Verpflichtungen
 * - MINIMAL_RISK: Keine besonderen Auflagen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Parameter für KI-Risikoklassifizierung nach EU AI Act")
public class AIRiskClassificationRequest {

    @NotBlank(message = "Name des KI-Systems ist erforderlich")
    @Size(min = 2, max = 200, message = "Name muss zwischen 2 und 200 Zeichen lang sein")
    @Schema(description = "Name des zu bewertenden KI-Systems", 
            example = "E-Commerce Recommendation Engine",
            required = true)
    private String systemName;

    @NotBlank(message = "Systemtyp ist erforderlich")
    @Size(min = 2, max = 100, message = "Systemtyp muss zwischen 2 und 100 Zeichen lang sein")
    @Schema(description = "Typ des KI-Systems", 
            example = "Recommendation System",
            required = true)
    private String systemType;

    @NotBlank(message = "Anwendungsbereich ist erforderlich")
    @Size(min = 2, max = 100, message = "Anwendungsbereich muss zwischen 2 und 100 Zeichen lang sein")
    @Schema(description = "Hauptanwendungsbereich des KI-Systems", 
            example = "E-Commerce",
            required = true)
    private String applicationDomain;

    @Size(min = 10, max = 2000, message = "Beschreibung muss zwischen 10 und 2000 Zeichen lang sein")
    @Schema(description = "Detaillierte Beschreibung des KI-Systems", 
            example = "Machine Learning basiertes Empfehlungssystem für Produktvorschläge basierend auf Nutzerverhalten und Präferenzen")
    private String systemDescription;

    @NotEmpty(message = "Mindestens ein Datentyp erforderlich")
    @Size(min = 1, max = 15, message = "1-15 Datentypen erforderlich")
    @Schema(description = "Arten der vom KI-System verarbeiteten Daten", 
            example = "[\"Kaufverhalten\", \"Präferenzen\", \"Demografische Daten\", \"Browsing-Verhalten\"]")
    private List<@NotBlank(message = "Datentyp darf nicht leer sein") String> dataTypes;

    @Schema(description = "Direkte Interaktion mit natürlichen Personen", 
            example = "true")
    @Builder.Default
    private boolean userInteraction = false;

    @Schema(description = "Automatisierte Entscheidungsfindung ohne menschliche Überprüfung", 
            example = "false")
    @Builder.Default
    private boolean automatedDecisionMaking = false;

    @Schema(description = "Verarbeitung biometrischer Daten zur Identifikation", 
            example = "false")
    @Builder.Default
    private boolean biometricData = false;

    @Schema(description = "Emotionserkennung oder biometrische Kategorisierung", 
            example = "false")
    @Builder.Default
    private boolean emotionRecognition = false;

    @Schema(description = "Einsatz in kritischen Infrastrukturen", 
            example = "false")
    @Builder.Default
    private boolean criticalInfrastructure = false;

    @Schema(description = "Bildungs- oder Berufsausbildungskontext", 
            example = "false")
    @Builder.Default
    private boolean educationContext = false;

    @Schema(description = "Beschäftigungskontext (Personalwesen, Recruiting)", 
            example = "false")
    @Builder.Default
    private boolean employmentContext = false;

    @Schema(description = "Zugang zu oder Nutzung von wesentlichen Dienstleistungen", 
            example = "false")
    @Builder.Default
    private boolean essentialServices = false;

    @Schema(description = "Strafverfolgung (außer zur Erkennung von Deepfakes)", 
            example = "false")
    @Builder.Default
    private boolean lawEnforcement = false;

    @Schema(description = "Migrations-, Asyl- oder Grenzkontrollen", 
            example = "false")
    @Builder.Default
    private boolean migrationAsylBorder = false;

    @Schema(description = "Rechtspflege und demokratische Prozesse", 
            example = "false")
    @Builder.Default
    private boolean justiceAndDemocracy = false;

    @Schema(description = "Bewertung der Kreditwürdigkeit natürlicher Personen", 
            example = "false")
    @Builder.Default
    private boolean creditScoring = false;

    @Schema(description = "Risikobewertung oder Preisfestsetzung für Versicherungen", 
            example = "false")
    @Builder.Default
    private boolean insuranceRiskAssessment = false;

    @Schema(description = "Einstufung von Notrufen durch Rettungsdienste", 
            example = "false")
    @Builder.Default
    private boolean emergencyServices = false;

    @Schema(description = "Sicherheitskomponenten von Produkten", 
            example = "false")
    @Builder.Default
    private boolean safetyComponents = false;

    @Schema(description = "Verarbeitung von Daten Minderjähriger", 
            example = "false")
    @Builder.Default
    private boolean minorsData = false;

    @Schema(description = "Verwendung in öffentlichen Räumen zugänglichen Systemen", 
            example = "false")
    @Builder.Default
    private boolean publicSpaces = false;

    @Schema(description = "Umfangreiche Verarbeitung (mehr als 10.000 Personen)", 
            example = "true")
    @Builder.Default
    private boolean largeScale = false;

    @Min(value = 1, message = "Mindestens 1 betroffene Person")
    @Max(value = 1000000000, message = "Maximal 1 Milliarde betroffene Personen")
    @Schema(description = "Geschätzte Anzahl der vom KI-System betroffenen Personen", 
            example = "50000")
    @Builder.Default
    private int estimatedAffectedPersons = 1000;

    @Schema(description = "Geografische Reichweite (LOCAL/NATIONAL/EU/GLOBAL)", 
            example = "EU")
    @Builder.Default
    private String geographicScope = "NATIONAL";

    @Size(max = 1000, message = "Zusätzliche Informationen dürfen maximal 1000 Zeichen lang sein")
    @Schema(description = "Zusätzliche Informationen für die Klassifizierung", 
            example = "B2B E-Commerce Platform, hauptsächlich DACH-Region, personalisierte Produktempfehlungen")
    private String additionalInfo;

    @Schema(description = "Demo-Modus aktivieren (vereinfachte Ausgabe für Präsentationen)", 
            example = "false")
    @Builder.Default
    private boolean demoMode = false;

    @Schema(description = "Deutsche Aufsichtsbehörden-Standards berücksichtigen", 
            example = "true")
    @Builder.Default
    private boolean germanStandards = true;

    /**
     * Berechnet EU AI Act Risikoklasse basierend auf Parametern
     */
    public String calculateRiskClass() {
        // UNACCEPTABLE_RISK - Verbotene KI-Praktiken
        if (isProhibitedPractice()) {
            return "UNACCEPTABLE_RISK";
        }
        
        // HIGH_RISK - Hochrisiko-KI-Systeme nach Anhang III
        if (isHighRiskSystem()) {
            return "HIGH_RISK";
        }
        
        // LIMITED_RISK - Transparenz-Verpflichtungen
        if (isLimitedRiskSystem()) {
            return "LIMITED_RISK";
        }
        
        // MINIMAL_RISK - Keine besonderen Auflagen
        return "MINIMAL_RISK";
    }

    /**
     * Prüft auf verbotene KI-Praktiken nach EU AI Act Art. 5
     */
    public boolean isProhibitedPractice() {
        // Subliminal techniques beyond consciousness
        if (systemDescription != null && 
            (systemDescription.toLowerCase().contains("subliminal") ||
             systemDescription.toLowerCase().contains("manipulation") ||
             systemDescription.toLowerCase().contains("unterbewusst"))) {
            return true;
        }
        
        // Exploitation of vulnerabilities (children, disabilities, economic situation)
        if (minorsData && (emotionRecognition || biometricData)) {
            return true;
        }
        
        // Social scoring by public authorities
        if (systemDescription != null && 
            systemDescription.toLowerCase().contains("social scoring") &&
            (justiceAndDemocracy || essentialServices)) {
            return true;
        }
        
        // Real-time biometric identification in public spaces by law enforcement
        if (biometricData && lawEnforcement && publicSpaces) {
            return true;
        }
        
        return false;
    }

    /**
     * Prüft auf Hochrisiko-KI-Systeme nach Anhang III
     */
    public boolean isHighRiskSystem() {
        // Biometric identification and categorisation
        if (biometricData || emotionRecognition) {
            return true;
        }
        
        // Critical infrastructure
        if (criticalInfrastructure) {
            return true;
        }
        
        // Education and vocational training
        if (educationContext && automatedDecisionMaking) {
            return true;
        }
        
        // Employment, workers management and access to self-employment
        if (employmentContext && automatedDecisionMaking) {
            return true;
        }
        
        // Essential services
        if (essentialServices && (creditScoring || emergencyServices)) {
            return true;
        }
        
        // Law enforcement (excluding prohibited uses)
        if (lawEnforcement && !publicSpaces && biometricData) {
            return true;
        }
        
        // Migration, asylum and border control management
        if (migrationAsylBorder && (biometricData || automatedDecisionMaking)) {
            return true;
        }
        
        // Justice and democratic processes
        if (justiceAndDemocracy && automatedDecisionMaking) {
            return true;
        }
        
        // Safety components
        if (safetyComponents) {
            return true;
        }
        
        return false;
    }

    /**
     * Prüft auf Limited Risk (Transparenz-Verpflichtungen)
     */
    public boolean isLimitedRiskSystem() {
        // AI systems intended to interact with natural persons
        if (userInteraction && !isHighRiskSystem()) {
            return true;
        }
        
        // Emotion recognition or biometric categorisation (not high-risk)
        if ((emotionRecognition || biometricData) && !isHighRiskSystem()) {
            return true;
        }
        
        // AI systems that generate or manipulate content
        if (systemType != null && 
            (systemType.toLowerCase().contains("generation") ||
             systemType.toLowerCase().contains("content") ||
             systemType.toLowerCase().contains("deepfake"))) {
            return true;
        }
        
        return false;
    }

    /**
     * Berechnet Risiko-Score (0.0 - 1.0)
     */
    public double calculateRiskScore() {
        double score = 0.0;
        
        // Prohibited practices (1.0)
        if (isProhibitedPractice()) {
            return 1.0;
        }
        
        // High-risk factors
        if (biometricData) score += 0.3;
        if (emotionRecognition) score += 0.3;
        if (criticalInfrastructure) score += 0.3;
        if (automatedDecisionMaking) score += 0.2;
        if (employmentContext) score += 0.2;
        if (essentialServices) score += 0.2;
        if (lawEnforcement) score += 0.2;
        if (justiceAndDemocracy) score += 0.2;
        
        // Medium-risk factors
        if (userInteraction) score += 0.1;
        if (educationContext) score += 0.1;
        if (minorsData) score += 0.1;
        if (largeScale) score += 0.1;
        if (publicSpaces) score += 0.1;
        
        // Scale factor
        if (estimatedAffectedPersons > 100000) score += 0.1;
        if (estimatedAffectedPersons > 1000000) score += 0.1;
        
        return Math.min(score, 1.0);
    }

    /**
     * Generiert EU AI Act spezifische Empfehlungen
     */
    public List<String> generateAIActRecommendations() {
        List<String> recommendations = new ArrayList<>();
        String riskClass = calculateRiskClass();
        
        switch (riskClass) {
            case "UNACCEPTABLE_RISK":
                recommendations.add("❌ VERBOTEN - KI-System darf nicht eingesetzt werden");
                recommendations.add("Überarbeitung des Systems erforderlich");
                recommendations.add("Alternative, konforme Ansätze entwickeln");
                break;
                
            case "HIGH_RISK":
                recommendations.add("🔴 Hochrisiko-KI-System - Vollständige Compliance erforderlich");
                recommendations.add("CE-Kennzeichnung vor Markteinführung");
                recommendations.add("Konformitätsbewertung durch benannte Stelle");
                recommendations.add("Umfassende technische Dokumentation erstellen");
                recommendations.add("Risikomanagementsystem implementieren");
                recommendations.add("Kontinuierliche Überwachung nach Markteinführung");
                recommendations.add("Meldung schwerwiegender Zwischenfälle");
                break;
                
            case "LIMITED_RISK":
                recommendations.add("🟡 Transparenz-Verpflichtungen erforderlich");
                recommendations.add("Nutzer über KI-System informieren");
                recommendations.add("Automatisierte Entscheidungen erkennbar machen");
                recommendations.add("Benutzerfreundliche Information bereitstellen");
                break;
                
            case "MINIMAL_RISK":
                recommendations.add("🟢 Minimales Risiko - Keine besonderen EU AI Act Auflagen");
                recommendations.add("Freiwillige Codes of Conduct berücksichtigen");
                recommendations.add("Best Practices für verantwortliche KI befolgen");
                break;
        }
        
        // Deutsche spezifische Empfehlungen
        if (germanStandards) {
            recommendations.add("Deutsche KI-Strategie 2030 berücksichtigen");
            recommendations.add("BfDI-Leitfaden für KI-Systeme befolgen");
            recommendations.add("Koordinierung mit deutschen Aufsichtsbehörden");
        }
        
        return recommendations;
    }

    /**
     * Schätzt Compliance-Aufwand in Tagen
     */
    public int estimateComplianceEffortDays() {
        String riskClass = calculateRiskClass();
        
        switch (riskClass) {
            case "UNACCEPTABLE_RISK":
                return 90; // Redesign erforderlich
            case "HIGH_RISK":
                return 45; // Vollständige Dokumentation + CE-Kennzeichnung
            case "LIMITED_RISK":
                return 10; // Transparenz-Maßnahmen
            case "MINIMAL_RISK":
                return 2; // Dokumentation
            default:
                return 5;
        }
    }

    /**
     * Validiert die Eingabedaten für Business Logic
     */
    public boolean isValidForClassification() {
        return systemName != null && !systemName.trim().isEmpty() &&
               systemType != null && !systemType.trim().isEmpty() &&
               applicationDomain != null && !applicationDomain.trim().isEmpty() &&
               dataTypes != null && !dataTypes.isEmpty() &&
               estimatedAffectedPersons > 0;
    }

    /**
     * Erstellt Demo-Request für Bewerbungsgespräche
     */
    public static AIRiskClassificationRequest createDemoRequest() {
        return AIRiskClassificationRequest.builder()
                .systemName("E-Commerce Recommendation Engine")
                .systemType("Recommendation System")
                .applicationDomain("E-Commerce")
                .systemDescription("Machine Learning basiertes Empfehlungssystem für Produktvorschläge basierend auf Nutzerverhalten, Kaufhistorie und demografischen Präferenzen")
                .dataTypes(List.of("Kaufverhalten", "Präferenzen", "Demografische Daten", "Browsing-Verhalten", "Produktbewertungen"))
                .userInteraction(true)
                .automatedDecisionMaking(true)
                .biometricData(false)
                .emotionRecognition(false)
                .criticalInfrastructure(false)
                .educationContext(false)
                .employmentContext(false)
                .essentialServices(false)
                .lawEnforcement(false)
                .migrationAsylBorder(false)
                .justiceAndDemocracy(false)
                .creditScoring(false)
                .insuranceRiskAssessment(false)
                .emergencyServices(false)
                .safetyComponents(false)
                .minorsData(false)
                .publicSpaces(false)
                .largeScale(true)
                .estimatedAffectedPersons(50000)
                .geographicScope("EU")
                .additionalInfo("B2B E-Commerce Platform, hauptsächlich DACH-Region, personalisierte Produktempfehlungen für Online-Shops")
                .demoMode(true)
                .germanStandards(true)
                .build();
    }

    /**
     * Erstellt Hochrisiko-Request für Demo
     */
    public static AIRiskClassificationRequest createHighRiskRequest() {
        return AIRiskClassificationRequest.builder()
                .systemName("Biometrische Mitarbeiter-Überwachung")
                .systemType("Biometric Identification System")
                .applicationDomain("Human Resources")
                .systemDescription("Gesichtserkennung für Zugangskontrolle und kontinuierliche Mitarbeiterüberwachung mit Emotion Detection")
                .dataTypes(List.of("Biometrische Daten", "Gesichtsdaten", "Emotionsdaten", "Standortdaten"))
                .userInteraction(true)
                .automatedDecisionMaking(true)
                .biometricData(true)
                .emotionRecognition(true)
                .employmentContext(true)
                .publicSpaces(true)
                .largeScale(false)
                .estimatedAffectedPersons(500)
                .geographicScope("NATIONAL")
                .additionalInfo("Hochrisiko-System für EU AI Act Demo - Biometrische Überwachung am Arbeitsplatz")
                .germanStandards(true)
                .build();
    }

    /**
     * Erstellt Verbotene-Praktik-Request für Demo
     */
    public static AIRiskClassificationRequest createProhibitedRequest() {
        return AIRiskClassificationRequest.builder()
                .systemName("Social Credit Scoring System")
                .systemType("Social Scoring System")
                .applicationDomain("Public Administration")
                .systemDescription("Bewertung von Bürgern basierend auf Sozialverhalten, finanzieller Situation und öffentlichen Daten für Zugang zu öffentlichen Dienstleistungen")
                .dataTypes(List.of("Sozialdaten", "Finanzdaten", "Verhaltensdaten", "Öffentliche Daten"))
                .userInteraction(false)
                .automatedDecisionMaking(true)
                .biometricData(false)
                .emotionRecognition(false)
                .essentialServices(true)
                .justiceAndDemocracy(true)
                .largeScale(true)
                .estimatedAffectedPersons(1000000)
                .geographicScope("NATIONAL")
                .additionalInfo("VERBOTEN - Social Scoring System für Demo der prohibited practices")
                .germanStandards(true)
                .build();
    }
}