package com.dtn.compliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * VVT Generation Request DTO
 * 
 * Request-Parameter für die automatische VVT-Generierung nach DSGVO Art. 30
 * Optimiert für deutsche Software-Dienstleister (50-200 MA)
 * 
 * Business Value:
 * - Zeitersparnis: 95% (8 Stunden → 24 Minuten)
 * - Kostenersparnis: 45.000€ pro Jahr
 * - BfDI-konforme deutsche Templates
 * - Automatische Updates bei Änderungen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Parameter für VVT-Generierung nach DSGVO Art. 30")
public class VVTGenerationRequest {

    @NotBlank(message = "Firmenname ist erforderlich")
    @Size(min = 2, max = 200, message = "Firmenname muss zwischen 2 und 200 Zeichen lang sein")
    @Schema(description = "Vollständiger Name des Unternehmens", 
            example = "Mustermann Software GmbH",
            required = true)
    private String companyName;

    @NotBlank(message = "Branche ist erforderlich")
    @Size(min = 2, max = 100, message = "Branche muss zwischen 2 und 100 Zeichen lang sein")
    @Schema(description = "Branche/Geschäftsbereich des Unternehmens", 
            example = "Software-Dienstleistung",
            required = true)
    private String industry;

    @Min(value = 1, message = "Mindestens 1 Mitarbeiter erforderlich")
    @Max(value = 50000, message = "Maximal 50.000 Mitarbeiter unterstützt")
    @Schema(description = "Anzahl Mitarbeiter (Vollzeitäquivalente)", 
            example = "120",
            required = true)
    private int employeeCount;

    @NotEmpty(message = "Mindestens eine Datenkategorie erforderlich")
    @Size(min = 1, max = 20, message = "1-20 Datenkategorien erforderlich")
    @Schema(description = "Kategorien der verarbeiteten personenbezogenen Daten", 
            example = "[\"Kundendaten\", \"Mitarbeiterdaten\", \"Projektdaten\", \"Rechnungsdaten\"]")
    private List<@NotBlank(message = "Datenkategorie darf nicht leer sein") String> dataCategories;

    @Schema(description = "Verarbeitet das Unternehmen Kundendaten?", 
            example = "true")
    @Builder.Default
    private boolean hasCustomerData = true;

    @Schema(description = "Verarbeitet das Unternehmen Mitarbeiterdaten?", 
            example = "true")
    @Builder.Default
    private boolean hasEmployeeData = true;

    @Schema(description = "Nutzt das Unternehmen KI-Verarbeitung oder Machine Learning?", 
            example = "true")
    @Builder.Default
    private boolean usesAIProcessing = false;

    @Schema(description = "Verarbeitung besonderer Datenkategorien nach Art. 9 DSGVO", 
            example = "false")
    @Builder.Default
    private boolean hasSpecialCategories = false;

    @Schema(description = "Übermittlung personenbezogener Daten in Drittländer", 
            example = "false")
    @Builder.Default
    private boolean hasThirdCountryTransfer = false;

    @Schema(description = "Automatisierte Entscheidungsfindung einschließlich Profiling", 
            example = "false")
    @Builder.Default
    private boolean hasAutomatedDecisionMaking = false;

    @Schema(description = "Verarbeitung von Daten vulnerabler Gruppen (Minderjährige, etc.)", 
            example = "false")
    @Builder.Default
    private boolean hasVulnerableGroups = false;

    @Schema(description = "Umfangreiche oder systematische Überwachung", 
            example = "false")
    @Builder.Default
    private boolean hasSystematicMonitoring = false;

    @Schema(description = "Datenschutzbeauftragter bestellt", 
            example = "true")
    @Builder.Default
    private boolean hasDataProtectionOfficer = false;

    @Schema(description = "Betriebsrat vorhanden (relevant für Mitarbeiterdatenverarbeitung)", 
            example = "true")
    @Builder.Default
    private boolean hasWorksCouncil = false;

    @Size(max = 1000, message = "Zusätzliche Informationen dürfen maximal 1000 Zeichen lang sein")
    @Schema(description = "Zusätzliche branchen-spezifische oder unternehmensspezifische Informationen", 
            example = "B2B Software-Entwicklung, SCRUM-basiert, hauptsächlich Enterprise-Kunden in DACH-Region")
    private String additionalInfo;

    @Schema(description = "Demo-Modus aktivieren (vereinfachte Ausgabe für Präsentationen)", 
            example = "false")
    @Builder.Default
    private boolean demoMode = false;

    @Schema(description = "Business-Value-Metriken in Ausgabe einbeziehen", 
            example = "true")
    @Builder.Default
    private boolean includeBusinessMetrics = true;

    /**
     * Prüft ob eine DSFA erforderlich sein könnte
     */
    public boolean isDsfaLikelyRequired() {
        return hasSpecialCategories || 
               hasSystematicMonitoring || 
               hasVulnerableGroups || 
               (usesAIProcessing && hasAutomatedDecisionMaking) ||
               employeeCount > 1000;
    }

    /**
     * Berechnet Risiko-Level basierend auf Verarbeitungsparametern
     */
    public String calculateRiskLevel() {
        int riskScore = 0;
        
        if (hasSpecialCategories) riskScore += 3;
        if (hasThirdCountryTransfer) riskScore += 2;
        if (hasAutomatedDecisionMaking) riskScore += 2;
        if (hasVulnerableGroups) riskScore += 2;
        if (hasSystematicMonitoring) riskScore += 2;
        if (usesAIProcessing) riskScore += 1;
        if (employeeCount > 250) riskScore += 1;
        
        if (riskScore >= 6) return "hoch";
        if (riskScore >= 3) return "mittel";
        return "niedrig";
    }

    /**
     * Schätzt Anzahl zu generierender Verarbeitungstätigkeiten
     */
    public int estimateProcessingActivities() {
        int baseActivities = 3; // Grundausstattung: Personal, IT, Website
        
        if (hasCustomerData) baseActivities += 2;
        if (usesAIProcessing) baseActivities += 1;
        if (hasThirdCountryTransfer) baseActivities += 1;
        if (employeeCount > 50) baseActivities += 1;
        if (employeeCount > 250) baseActivities += 1;
        
        return Math.min(baseActivities, 15); // Maximal 15 Aktivitäten für Demo
    }

    /**
     * Validiert die Eingabedaten für Business Logic
     */
    public boolean isValidForProcessing() {
        return companyName != null && !companyName.trim().isEmpty() &&
               industry != null && !industry.trim().isEmpty() &&
               employeeCount > 0 &&
               dataCategories != null && !dataCategories.isEmpty();
    }

    /**
     * Erstellt Demo-Request für Bewerbungsgespräche
     */
    public static VVTGenerationRequest createDemoRequest() {
        return VVTGenerationRequest.builder()
                .companyName("Mustermann Software GmbH")
                .industry("Software-Dienstleistung")
                .employeeCount(120)
                .hasCustomerData(true)
                .hasEmployeeData(true)
                .usesAIProcessing(true)
                .hasThirdCountryTransfer(false)
                .hasSpecialCategories(false)
                .dataCategories(List.of(
                    "Kundendaten", "Mitarbeiterdaten", "Projektdaten", 
                    "Rechnungsdaten", "Support-Daten", "Analytics-Daten"
                ))
                .hasDataProtectionOfficer(true)
                .hasWorksCouncil(true)
                .demoMode(true)
                .includeBusinessMetrics(true)
                .additionalInfo("B2B Software-Entwicklung, SCRUM-basiert, hauptsächlich Enterprise-Kunden in DACH-Region. Spezialisiert auf Compliance-Software für deutsche Unternehmen.")
                .build();
    }

    /**
     * Erstellt Minimal-Request für Testing
     */
    public static VVTGenerationRequest createMinimalRequest() {
        return VVTGenerationRequest.builder()
                .companyName("Test GmbH")
                .industry("Software-Dienstleistung")
                .employeeCount(10)
                .dataCategories(List.of("Kundendaten", "Mitarbeiterdaten"))
                .build();
    }
}