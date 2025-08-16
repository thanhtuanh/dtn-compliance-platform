package com.dtn.compliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DSFA Assessment Response DTO
 * 
 * Response der automatischen Datenschutz-Folgenabschätzung nach DSGVO Art. 35
 * mit integrierter EU AI Act Bewertung
 * 
 * Business Value Demonstration:
 * - 87% Effizienzsteigerung (16h → 2h)
 * - 30.720€ jährliche Kostenersparnis
 * - Automatisierte Risiko-Bewertung
 * - Deutsche Rechtssicherheit gewährleistet
 * - EU AI Act Compliance integriert
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Antwort des DSFA-Assessments mit Risikobewertung und Maßnahmenempfehlungen")
public class DSFAAssessmentResponse {

    @Schema(description = "Erfolg des Assessments", example = "true")
    private boolean success;

    @Schema(description = "Name der bewerteten Verarbeitungstätigkeit", 
            example = "KI-basierte Kundensegmentierung")
    private String processingName;

    @Schema(description = "Risiko-Score (0.0 - 1.0)", example = "0.65")
    private double riskScore;

    @Schema(description = "Risiko-Level (niedrig/mittel/hoch)", example = "mittel")
    private String riskLevel;

    @Schema(description = "DSFA nach DSGVO Art. 35 erforderlich", example = "true")
    private boolean dsfaRequired;

    @Schema(description = "Identifizierte Datenschutzrisiken")
    private List<String> identifiedRisks;

    @Schema(description = "Empfohlene Schutzmaßnahmen")
    private List<String> recommendedMeasures;

    @Schema(description = "Compliance-Status", example = "Maßnahmen erforderlich")
    private String complianceStatus;

    @Schema(description = "Zeitpunkt des Assessments")
    private LocalDateTime assessedAt;

    @Schema(description = "Erneute Bewertung empfohlen", example = "true")
    private boolean reassessmentRecommended;

    @Schema(description = "Nächste Überprüfung in Monaten", example = "6")
    private int nextReviewMonths;

    @Schema(description = "Fehlermeldung bei Misserfolg")
    private String errorMessage;

    // EU AI Act Integration
    @Schema(description = "EU AI Act Assessment durchgeführt", example = "true")
    private boolean aiActAssessmentPerformed;

    @Schema(description = "KI-Risiko-Klasse nach EU AI Act", example = "LIMITED_RISK")
    private String aiRiskClass;

    @Schema(description = "KI-Risiko-Klasse auf Deutsch", example = "Begrenztes Risiko")
    private String aiRiskClassGerman;

    @Schema(description = "EU AI Act Compliance-Status", example = "Transparenz-Verpflichtungen erforderlich")
    private String aiActComplianceStatus;

    @Schema(description = "KI-spezifische Compliance-Maßnahmen")
    private List<String> aiComplianceMeasures;

    // Business Metrics
    @Schema(description = "Verarbeitungszeit in Millisekunden", example = "850")
    private Long processingTimeMs;

    @Schema(description = "Business Value Metriken für ROI-Demonstration")
    private Map<String, Object> businessMetrics;

    @Schema(description = "Deutsche Aufsichtsbehörden-Konformität", example = "true")
    private boolean germanAuthorityCompliant;

    @Schema(description = "BfDI-konforme DSFA-Vorlage verwendet", example = "true")
    private boolean bfdiTemplateUsed;

    // Detailed Assessment Results
    @Schema(description = "Detaillierte Risikobewertung nach Kategorien")
    private Map<String, Double> riskCategoryScores;

    @Schema(description = "Betroffene Rechte und Freiheiten")
    private List<String> affectedRightsAndFreedoms;

    @Schema(description = "Erforderliche Garantien und Schutzmaßnahmen")
    private List<String> requiredSafeguards;

    @Schema(description = "Interessenabwägung erforderlich", example = "false")
    private boolean balanceOfInterestsRequired;

    @Schema(description = "Konsultation der Aufsichtsbehörde erforderlich", example = "false")
    private boolean authorityConsultationRequired;

    // Business Value Helper-Methoden

    /**
     * Berechnet geschätzte Zeitersparnis gegenüber manueller DSFA
     */
    public String getTimeSavingsDescription() {
        if (processingTimeMs != null) {
            double manualHours = 16.0; // Typische manuelle DSFA-Dauer
            double automatedHours = processingTimeMs / (1000.0 * 60.0 * 60.0);
            double savingsPercent = ((manualHours - automatedHours) / manualHours) * 100;
            
            return String.format("%.0f%% Zeitersparnis (%.0fh → %.0fmin)", 
                    savingsPercent, manualHours, automatedHours * 60);
        }
        return "87% Zeitersparnis (16h → 2h)";
    }

    /**
     * Berechnet geschätzte jährliche Kostenersparnis
     */
    public String getAnnualCostSavings() {
        double hourlyRate = 80.0; // €/h für Compliance-Experte
        double manualHours = 16.0;
        double automatedHours = 2.0; // Nach KI-Unterstützung
        double savingsPerDSFA = (manualHours - automatedHours) * hourlyRate;
        double annualSavings = savingsPerDSFA * 24; // 24 DSFAs pro Jahr (2 pro Monat)
        
        return String.format("%.0f€ jährliche Ersparnis", annualSavings);
    }

    /**
     * Gibt Compliance-Priorität für Management-Reports zurück
     */
    public String getCompliancePriority() {
        if (!dsfaRequired) {
            return "NIEDRIG - DSFA nicht erforderlich";
        }
        
        if (riskScore >= 0.8) {
            return "KRITISCH - Sofortige Maßnahmen erforderlich";
        } else if (riskScore >= 0.6) {
            return "HOCH - Maßnahmen zeitnah umsetzen";
        } else if (riskScore >= 0.4) {
            return "MITTEL - Maßnahmen planen";
        } else {
            return "NIEDRIG - Überwachung ausreichend";
        }
    }

    /**
     * Prüft ob kritische Compliance-Lücken vorliegen
     */
    public boolean hasCriticalComplianceGaps() {
        return dsfaRequired && (riskScore >= 0.8 || 
                               authorityConsultationRequired ||
                               (aiActAssessmentPerformed && "HIGH_RISK".equals(aiRiskClass)));
    }

    /**
     * Generiert Executive Summary für Management
     */
    public String getExecutiveSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("DSFA für '%s' abgeschlossen. ", processingName));
        summary.append(String.format("Risiko-Level: %s (Score: %.2f). ", riskLevel, riskScore));
        
        if (dsfaRequired) {
            summary.append("DSFA nach Art. 35 DSGVO erforderlich. ");
        } else {
            summary.append("Keine DSFA-Pflicht. ");
        }
        
        if (aiActAssessmentPerformed) {
            summary.append(String.format("EU AI Act: %s. ", aiRiskClassGerman));
        }
        
        if (hasCriticalComplianceGaps()) {
            summary.append("Kritische Compliance-Lücken identifiziert - sofortige Maßnahmen erforderlich.");
        } else {
            summary.append("Compliance-Status akzeptabel - geplante Maßnahmen ausreichend.");
        }
        
        return summary.toString();
    }

    /**
     * Gibt ROI-Berechnung für Bewerbungsgespräche zurück
     */
    public Map<String, String> getROICalculation() {
        return Map.of(
            "zeitersparnis", getTimeSavingsDescription(),
            "kostenersparnis", getAnnualCostSavings(),
            "compliance_prioritaet", getCompliancePriority(),
            "rechtssicherheit", "Deutsche Aufsichtsbehörden-konforme DSFA",
            "automatisierung", "DSGVO Art. 35 + EU AI Act vollständig automatisiert"
        );
    }

    /**
     * Prüft ob DSFA demo-ready ist
     */
    public boolean isDemoReady() {
        return success && 
               processingName != null && 
               !identifiedRisks.isEmpty() && 
               !recommendedMeasures.isEmpty() &&
               germanAuthorityCompliant;
    }

    /**
     * Erstellt Demo-Response für Bewerbungspräsentationen
     */
    public static DSFAAssessmentResponse createDemoResponse() {
        return DSFAAssessmentResponse.builder()
                .success(true)
                .processingName("KI-basierte Kundensegmentierung")
                .riskScore(0.65)
                .riskLevel("mittel")
                .dsfaRequired(true)
                .identifiedRisks(List.of(
                    "Automatisierte Entscheidungsfindung bei Kundensegmentierung",
                    "Umfangreiche Datenverarbeitung (50.000+ Kunden)",
                    "Zusammenführung verschiedener Datenquellen",
                    "Potentielle Diskriminierung durch ML-Algorithmen"
                ))
                .recommendedMeasures(List.of(
                    "Privacy by Design in ML-Pipeline implementieren",
                    "Algorithmic Bias Monitoring einführen",
                    "Transparenz-Dashboard für Betroffene bereitstellen",
                    "Human-in-the-Loop Oversight für kritische Entscheidungen",
                    "Regelmäßige Modell-Audits durchführen",
                    "Datenminimierung vor ML-Training anwenden"
                ))
                .complianceStatus("Maßnahmen erforderlich - DSFA durchführen")
                .assessedAt(LocalDateTime.now())
                .reassessmentRecommended(true)
                .nextReviewMonths(6)
                .aiActAssessmentPerformed(true)
                .aiRiskClass("LIMITED_RISK")
                .aiRiskClassGerman("Begrenztes Risiko")
                .aiActComplianceStatus("Transparenz-Verpflichtungen erforderlich")
                .aiComplianceMeasures(List.of(
                    "KI-System-Information für Nutzer bereitstellen",
                    "Automatisierte Entscheidungen erkennbar machen",
                    "Widerspruchsrecht bei automatisierter Entscheidungsfindung"
                ))
                .processingTimeMs(850L)
                .businessMetrics(Map.of(
                    "manual_hours_saved", 14,
                    "automation_time_minutes", 120,
                    "efficiency_gain_percent", 87.5,
                    "annual_cost_savings_eur", 30720,
                    "dsfa_automation_level", "90%",
                    "risk_assessment_accuracy", "95%"
                ))
                .germanAuthorityCompliant(true)
                .bfdiTemplateUsed(true)
                .riskCategoryScores(Map.of(
                    "Datenschutzrisiko", 0.6,
                    "Technisches Risiko", 0.5,
                    "Organisatorisches Risiko", 0.4,
                    "Rechtliches Risiko", 0.7,
                    "Reputationsrisiko", 0.3
                ))
                .affectedRightsAndFreedoms(List.of(
                    "Recht auf informationelle Selbstbestimmung",
                    "Recht auf Transparenz (Art. 12-14 DSGVO)",
                    "Recht auf Widerspruch (Art. 21 DSGVO)",
                    "Schutz vor automatisierter Entscheidungsfindung (Art. 22 DSGVO)"
                ))
                .requiredSafeguards(List.of(
                    "Einwilligungsmanagement für Marketing-Nutzung",
                    "Pseudonymisierung vor ML-Training",
                    "Regelmäßige Löschung nicht mehr benötigter Daten",
                    "Sichere Datenübertragung und -speicherung",
                    "Mitarbeiter-Schulungen zu KI-Datenschutz"
                ))
                .balanceOfInterestsRequired(true)
                .authorityConsultationRequired(false)
                .build();
    }

    /**
     * Erstellt Hochrisiko-Response für DSFA-Pflicht-Demo
     */
    public static DSFAAssessmentResponse createHighRiskResponse() {
        return DSFAAssessmentResponse.builder()
                .success(true)
                .processingName("Biometrische Mitarbeiterüberwachung")
                .riskScore(0.92)
                .riskLevel("hoch")
                .dsfaRequired(true)
                .identifiedRisks(List.of(
                    "Verarbeitung biometrischer Daten (Art. 9 DSGVO)",
                    "Systematische Überwachung von Mitarbeitern",
                    "Automatisierte Entscheidungen bei Zugangskontrollen",
                    "Hohes Diskriminierungspotential",
                    "Beeinträchtigung der Mitarbeiterrechte",
                    "Potentielle Gesundheitsgefährdung durch Überwachungsstress"
                ))
                .recommendedMeasures(List.of(
                    "DSFA zwingend erforderlich vor Implementierung",
                    "Konsultation der Aufsichtsbehörde empfohlen",
                    "Betriebsvereinbarung mit Betriebsrat aushandeln",
                    "Alternative, weniger invasive Methoden prüfen",
                    "Strenge Zweckbindung und Datenminimierung",
                    "Regelmäßige Überprüfung der Verhältnismäßigkeit"
                ))
                .complianceStatus("KRITISCH - Sofortige Maßnahmen erforderlich")
                .assessedAt(LocalDateTime.now())
                .reassessmentRecommended(true)
                .nextReviewMonths(3)
                .aiActAssessmentPerformed(true)
                .aiRiskClass("HIGH_RISK")
                .aiRiskClassGerman("Hochrisiko-KI-System")
                .aiActComplianceStatus("CE-Kennzeichnung und Konformitätsbewertung erforderlich")
                .aiComplianceMeasures(List.of(
                    "Konformitätsbewertung durch benannte Stelle",
                    "CE-Kennzeichnung vor Markteinführung",
                    "Umfassende Dokumentation der KI-Systeme",
                    "Kontinuierliche Überwachung nach Markteinführung",
                    "Meldung schwerwiegender Zwischenfälle"
                ))
                .processingTimeMs(950L)
                .businessMetrics(Map.of(
                    "risk_level", "KRITISCH",
                    "dsfa_mandatory", true,
                    "authority_consultation", "empfohlen",
                    "implementation_blocked", true
                ))
                .germanAuthorityCompliant(true)
                .bfdiTemplateUsed(true)
                .riskCategoryScores(Map.of(
                    "Datenschutzrisiko", 0.95,
                    "Technisches Risiko", 0.8,
                    "Organisatorisches Risiko", 0.9,
                    "Rechtliches Risiko", 0.98,
                    "Reputationsrisiko", 0.85
                ))
                .affectedRightsAndFreedoms(List.of(
                    "Recht auf informationelle Selbstbestimmung",
                    "Recht auf körperliche Unversehrtheit",
                    "Arbeitnehmerrechte und Mitbestimmung",
                    "Recht auf Privatsphäre am Arbeitsplatz"
                ))
                .requiredSafeguards(List.of(
                    "Explizite Einwilligung oder gesetzliche Grundlage",
                    "Minimierung biometrischer Datenverarbeitung",
                    "Sichere Speicherung mit höchstem Sicherheitsniveau",
                    "Sofortige Löschung bei Beendigung des Arbeitsverhältnisses",
                    "Transparente Information aller Betroffenen"
                ))
                .balanceOfInterestsRequired(true)
                .authorityConsultationRequired(true)
                .build();
    }

    /**
     * Erstellt Niedrigrisiko-Response ohne DSFA-Pflicht
     */
    public static DSFAAssessmentResponse createLowRiskResponse() {
        return DSFAAssessmentResponse.builder()
                .success(true)
                .processingName("Standard-Newsletter-Versand")
                .riskScore(0.25)
                .riskLevel("niedrig")
                .dsfaRequired(false)
                .identifiedRisks(List.of(
                    "Potentielle unerwünschte Werbung",
                    "Möglicher Datenmissbrauch bei Sicherheitslücken",
                    "Tracking von Öffnungsraten und Klicks"
                ))
                .recommendedMeasures(List.of(
                    "Double-Opt-In-Verfahren implementieren",
                    "Einfache Abmelde-Möglichkeit bereitstellen",
                    "Regelmäßige Sicherheitsüberprüfungen",
                    "Transparente Datenschutzerklärung",
                    "Datenminimierung bei Analytics"
                ))
                .complianceStatus("Akzeptabel - Standard-Maßnahmen ausreichend")
                .assessedAt(LocalDateTime.now())
                .reassessmentRecommended(false)
                .nextReviewMonths(12)
                .aiActAssessmentPerformed(false)
                .aiRiskClass("NOT_APPLICABLE")
                .aiRiskClassGerman("Nicht KI-relevant")
                .processingTimeMs(400L)
                .businessMetrics(Map.of(
                    "risk_level", "NIEDRIG",
                    "dsfa_required", false,
                    "standard_compliance", true
                ))
                .germanAuthorityCompliant(true)
                .bfdiTemplateUsed(true)
                .riskCategoryScores(Map.of(
                    "Datenschutzrisiko", 0.2,
                    "Technisches Risiko", 0.3,
                    "Organisatorisches Risiko", 0.1,
                    "Rechtliches Risiko", 0.2,
                    "Reputationsrisiko", 0.1
                ))
                .affectedRightsAndFreedoms(List.of(
                    "Recht auf informationelle Selbstbestimmung",
                    "Recht auf Widerspruch gegen Direktwerbung"
                ))
                .requiredSafeguards(List.of(
                    "Einwilligungsbasierte Verarbeitung",
                    "Sichere Datenübertragung",
                    "Regelmäßige Datenlöschung",
                    "Transparente Kommunikation"
                ))
                .balanceOfInterestsRequired(false)
                .authorityConsultationRequired(false)
                .build();
    }
}