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
 * Compliance Report Response DTO
 * 
 * Umfassender Compliance-Report für DSGVO + EU AI Act
 * 
 * Business Value Demonstration:
 * - Vollständiger Compliance-Überblick in einem Report
 * - Executive Summary für Management-Entscheidungen
 * - Messbare Compliance-Scores und KPIs
 * - Deutsche Rechtssicherheit dokumentiert
 * - Audit-Ready Dokumentation
 * 
 * Report-Typen:
 * - FULL: Vollständiger DSGVO + EU AI Act Report
 * - GDPR_ONLY: Nur DSGVO-Compliance (VVT + DSFA)
 * - AI_ACT_ONLY: Nur EU AI Act Compliance
 * - EXECUTIVE: Management Summary
 * - AUDIT: Behörden-konforme Dokumentation
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Umfassender Compliance-Report für DSGVO + EU AI Act")
public class ComplianceReportResponse {

    @Schema(description = "Erfolg der Report-Generierung", example = "true")
    private boolean success;

    @Schema(description = "Report-Typ", 
            example = "FULL",
            allowableValues = {"FULL", "GDPR_ONLY", "AI_ACT_ONLY", "EXECUTIVE", "AUDIT"})
    private String reportType;

    @Schema(description = "Ausgabeformat", 
            example = "PDF",
            allowableValues = {"PDF", "CSV", "XML", "JSON", "HTML"})
    private String format;

    @Schema(description = "Zeitpunkt der Report-Generierung")
    private LocalDateTime generatedAt;

    @Schema(description = "Report gültig bis")
    private LocalDateTime validUntil;

    @Schema(description = "Unternehmen/Organisation", example = "Mustermann Software GmbH")
    private String organization;

    @Schema(description = "Berichtszeitraum von")
    private LocalDateTime reportPeriodFrom;

    @Schema(description = "Berichtszeitraum bis")
    private LocalDateTime reportPeriodTo;

    // Gesamtbewertung
    @Schema(description = "Gesamt-Compliance-Score (0-100%)", example = "87.5")
    private double overallComplianceScore;

    @Schema(description = "DSGVO-Compliance-Status", 
            example = "COMPLIANT",
            allowableValues = {"COMPLIANT", "PARTIALLY_COMPLIANT", "NON_COMPLIANT", "NEEDS_ATTENTION"})
    private String gdprStatus;

    @Schema(description = "EU AI Act-Compliance-Status", 
            example = "NEEDS_ATTENTION",
            allowableValues = {"COMPLIANT", "PARTIALLY_COMPLIANT", "NON_COMPLIANT", "NEEDS_ATTENTION", "NOT_APPLICABLE"})
    private String aiActStatus;

    // DSGVO-spezifische Metriken
    @Schema(description = "Anzahl VVT-Einträge", example = "6")
    private int vvtEntries;

    @Schema(description = "Anzahl durchgeführter DSFA-Assessments", example = "3")
    private int dsfaAssessments;

    @Schema(description = "Anzahl Hochrisiko-Verarbeitungen", example = "1")
    private int highRiskProcessingActivities;

    @Schema(description = "Anzahl Drittland-Übermittlungen", example = "2")
    private int thirdCountryTransfers;

    // EU AI Act-spezifische Metriken
    @Schema(description = "Anzahl bewerteter KI-Systeme", example = "4")
    private int aiSystems;

    @Schema(description = "Anzahl Hochrisiko-KI-Systeme", example = "1")
    private int highRiskAISystems;

    @Schema(description = "Anzahl KI-Systeme mit Transparenz-Verpflichtungen", example = "2")
    private int limitedRiskAISystems;

    @Schema(description = "Anzahl verbotener KI-Praktiken erkannt", example = "0")
    private int prohibitedAIPractices;

    // Compliance-Analyse
    @Schema(description = "Kritische Compliance-Probleme")
    private List<String> criticalIssues;

    @Schema(description = "Empfohlene Sofortmaßnahmen")
    private List<String> recommendedActions;

    @Schema(description = "Mittelfristige Verbesserungen")
    private List<String> mediumTermImprovements;

    @Schema(description = "Langfristige Compliance-Strategie")
    private List<String> longTermStrategy;

    // Risk Assessment
    @Schema(description = "Identifizierte Compliance-Risiken")
    private List<String> identifiedRisks;

    @Schema(description = "Risiko-Kategorien mit Scores")
    private Map<String, Double> riskCategoryScores;

    @Schema(description = "Rechtliche Risikobewertung", example = "MEDIUM")
    private String legalRiskLevel;

    @Schema(description = "Geschätztes Bußgeld-Risiko in EUR", example = "0")
    private long estimatedFineRisk;

    // Business Impact
    @Schema(description = "Business Value Metriken")
    private Map<String, Object> businessMetrics;

    @Schema(description = "ROI der Compliance-Automatisierung", example = "340%")
    private String complianceAutomationROI;

    @Schema(description = "Jährliche Kostenersparnis in EUR", example = "96720")
    private long annualCostSavings;

    @Schema(description = "Zeitersparnis durch Automatisierung", example = "87%")
    private double timeEfficiencyGain;

    // Download und Export
    @Schema(description = "Download-URL für generierten Report")
    private String downloadUrl;

    @Schema(description = "Dateigröße in Bytes", example = "1024000")
    private Long fileSizeBytes;

    @Schema(description = "Verfügbare Export-Formate")
    private List<String> availableFormats;

    // Deutsche Rechtssicherheit
    @Schema(description = "BfDI-konforme Dokumentation", example = "true")
    private boolean bfdiCompliant;

    @Schema(description = "Landesdatenschutzbehörden-kompatibel", example = "true")
    private boolean stateAuthorityCompliant;

    @Schema(description = "Audit-Ready Status", example = "true")
    private boolean auditReady;

    @Schema(description = "Letzte Behörden-Anfrage beantwortet am")
    private LocalDateTime lastAuthorityContactHandled;

    // Report-Metadaten
    @Schema(description = "Report-Version", example = "1.2.3")
    private String reportVersion;

    @Schema(description = "Verwendete Compliance-Standards")
    private List<String> complianceStandards;

    @Schema(description = "Report-Sprache", example = "DE")
    private String language;

    @Schema(description = "Nächster Report-Termin empfohlen")
    private LocalDateTime nextReportRecommended;

    @Schema(description = "Fehlermeldung bei Misserfolg")
    private String errorMessage;

    // Business Value Helper-Methoden

    /**
     * Berechnet Compliance-Readiness-Level
     */
    public String getComplianceReadinessLevel() {
        if (overallComplianceScore >= 95) {
            return "EXCELLENT - Vollständig audit-ready";
        } else if (overallComplianceScore >= 85) {
            return "GOOD - Kleinere Optimierungen empfohlen";
        } else if (overallComplianceScore >= 70) {
            return "SATISFACTORY - Maßnahmen erforderlich";
        } else if (overallComplianceScore >= 50) {
            return "NEEDS_IMPROVEMENT - Sofortige Maßnahmen notwendig";
        } else {
            return "CRITICAL - Umfassende Compliance-Sanierung erforderlich";
        }
    }

    /**
     * Berechnet geschätzte Bußgeld-Exposition basierend auf Compliance-Lücken
     */
    public String getEstimatedFineExposure() {
        if (prohibitedAIPractices > 0) {
            return "KRITISCH - Bis zu 35 Mio€ oder 7% Jahresumsatz (EU AI Act)";
        }
        
        if (overallComplianceScore < 50 || criticalIssues.size() > 3) {
            return "HOCH - Bis zu 20 Mio€ oder 4% Jahresumsatz (DSGVO)";
        } else if (overallComplianceScore < 70) {
            return "MITTEL - Bis zu 10 Mio€ oder 2% Jahresumsatz";
        } else if (overallComplianceScore < 85) {
            return "NIEDRIG - Administrative Maßnahmen möglich";
        } else {
            return "MINIMAL - Compliance-konforme Position";
        }
    }

    /**
     * Gibt Priority-Score für Management zurück
     */
    public int getManagementPriorityScore() {
        int priority = 0;
        
        // Kritische Faktoren (jeweils +3)
        if (prohibitedAIPractices > 0) priority += 3;
        if (overallComplianceScore < 50) priority += 3;
        if (criticalIssues.size() > 3) priority += 3;
        
        // Wichtige Faktoren (jeweils +2)
        if (highRiskAISystems > 0 && !aiActStatus.equals("COMPLIANT")) priority += 2;
        if (thirdCountryTransfers > 0) priority += 2;
        if (dsfaAssessments == 0 && highRiskProcessingActivities > 0) priority += 2;
        
        // Moderate Faktoren (jeweils +1)
        if (overallComplianceScore < 85) priority += 1;
        if (!auditReady) priority += 1;
        
        return Math.min(priority, 10); // Max 10 Punkte
    }

    /**
     * Generiert Executive Summary für C-Level
     */
    public String getExecutiveSummary() {
        StringBuilder summary = new StringBuilder();
        
        summary.append(String.format("Compliance-Status: %s (Score: %.1f%%). ", 
                                    getComplianceReadinessLevel(), overallComplianceScore));
        
        if (prohibitedAIPractices > 0) {
            summary.append(String.format("KRITISCH: %d verbotene KI-Praktiken erkannt. ", prohibitedAIPractices));
        }
        
        if (highRiskAISystems > 0) {
            summary.append(String.format("%d Hochrisiko-KI-Systeme benötigen CE-Kennzeichnung. ", highRiskAISystems));
        }
        
        if (criticalIssues.size() > 0) {
            summary.append(String.format("%d kritische Compliance-Lücken identifiziert. ", criticalIssues.size()));
        }
        
        summary.append(String.format("Automatisierungs-ROI: %s. ", complianceAutomationROI));
        summary.append(String.format("Jährliche Ersparnis: %,.0f€. ", (double) annualCostSavings));
        
        if (auditReady) {
            summary.append("Behörden-audit-ready.");
        } else {
            summary.append("Audit-Vorbereitung erforderlich.");
        }
        
        return summary.toString();
    }

    /**
     * Gibt Top-3 Action Items für Management zurück
     */
    public List<String> getTop3ActionItems() {
        List<String> actions = new java.util.ArrayList<>();
        
        if (prohibitedAIPractices > 0) {
            actions.add("🚨 SOFORT: Verbotene KI-Praktiken einstellen");
        }
        
        if (highRiskAISystems > 0 && !aiActStatus.equals("COMPLIANT")) {
            actions.add("🔴 PRIORITÄT: CE-Kennzeichnung für Hochrisiko-KI-Systeme");
        }
        
        if (dsfaAssessments == 0 && highRiskProcessingActivities > 0) {
            actions.add("📋 ERFORDERLICH: DSFA für Hochrisiko-Verarbeitungen");
        }
        
        if (actions.size() < 3 && thirdCountryTransfers > 0) {
            actions.add("🌍 PRÜFEN: Drittland-Übermittlungen und SCCs validieren");
        }
        
        if (actions.size() < 3 && overallComplianceScore < 85) {
            actions.add("📈 VERBESSERN: Compliance-Score auf >85% steigern");
        }
        
        if (actions.size() < 3 && !auditReady) {
            actions.add("✅ VORBEREITEN: Audit-Readiness sicherstellen");
        }
        
        return actions.subList(0, Math.min(actions.size(), 3));
    }

    /**
     * Berechnet Next Review Date basierend auf Risiko-Level
     */
    public LocalDateTime calculateNextReviewDate() {
        LocalDateTime base = LocalDateTime.now();
        
        if (prohibitedAIPractices > 0 || overallComplianceScore < 50) {
            return base.plusWeeks(2); // Sofortige Nachkontrolle
        } else if (overallComplianceScore < 70 || criticalIssues.size() > 2) {
            return base.plusMonths(1); // Monatliche Überprüfung
        } else if (overallComplianceScore < 85) {
            return base.plusMonths(3); // Quartalsweise
        } else {
            return base.plusMonths(6); // Halbjährlich
        }
    }

    /**
     * Gibt Compliance-Trend zurück
     */
    public String getComplianceTrend() {
        // Vereinfachte Trend-Analyse basierend auf aktuellen Werten
        if (overallComplianceScore >= 85 && criticalIssues.size() <= 1) {
            return "📈 POSITIV - Compliance-Niveau steigt";
        } else if (criticalIssues.size() > 3 || prohibitedAIPractices > 0) {
            return "📉 NEGATIV - Compliance-Risiken nehmen zu";
        } else {
            return "➡️ STABIL - Compliance-Niveau konstant";
        }
    }

    /**
     * Erstellt Demo-Response für Bewerbungspräsentationen
     */
    public static ComplianceReportResponse createDemoResponse() {
        return ComplianceReportResponse.builder()
                .success(true)
                .reportType("FULL")
                .format("PDF")
                .generatedAt(LocalDateTime.now())
                .validUntil(LocalDateTime.now().plusMonths(6))
                .organization("Mustermann Software GmbH")
                .reportPeriodFrom(LocalDateTime.now().minusMonths(6))
                .reportPeriodTo(LocalDateTime.now())
                .overallComplianceScore(87.5)
                .gdprStatus("COMPLIANT")
                .aiActStatus("NEEDS_ATTENTION")
                .vvtEntries(6)
                .dsfaAssessments(3)
                .highRiskProcessingActivities(1)
                .thirdCountryTransfers(2)
                .aiSystems(4)
                .highRiskAISystems(1)
                .limitedRiskAISystems(2)
                .prohibitedAIPractices(0)
                .criticalIssues(List.of(
                    "CE-Kennzeichnung für Hochrisiko-KI-System erforderlich",
                    "Transfer Impact Assessment für US-Cloud-Services ausstehend"
                ))
                .recommendedActions(List.of(
                    "Konformitätsbewertung für biometrisches KI-System starten",
                    "Standardvertragsklauseln für Drittland-Übermittlungen aktualisieren",
                    "EU AI Act Compliance-Check für alle KI-Systeme durchführen",
                    "DSFA-Dokumentation vervollständigen"
                ))
                .mediumTermImprovements(List.of(
                    "Privacy by Design in alle Entwicklungsprozesse integrieren",
                    "Automatisierte Compliance-Monitoring implementieren",
                    "Mitarbeiter-Schulungsprogramm zu EU AI Act etablieren"
                ))
                .longTermStrategy(List.of(
                    "Compliance-Management-System ausbauen",
                    "Proaktive Regulatory Intelligence aufbauen",
                    "Compliance-Automatisierung auf weitere Bereiche ausweiten"
                ))
                .identifiedRisks(List.of(
                    "Potentielle EU AI Act Bußgelder bei Hochrisiko-KI-Systemen",
                    "DSGVO-Risiken bei Drittland-Übermittlungen",
                    "Compliance-Lücken bei neuen KI-Projekten"
                ))
                .riskCategoryScores(Map.of(
                    "DSGVO-Compliance", 0.9,
                    "EU AI Act Compliance", 0.6,
                    "Datensicherheit", 0.85,
                    "Drittland-Transfers", 0.7,
                    "KI-Governance", 0.65
                ))
                .legalRiskLevel("MEDIUM")
                .estimatedFineRisk(0L)
                .businessMetrics(Map.of(
                    "annual_savings_eur", 96720,
                    "automation_roi_percent", 340,
                    "time_efficiency_gain", 87.5,
                    "compliance_automation_level", "95%",
                    "audit_readiness_score", 85,
                    "regulatory_response_time_hours", 4
                ))
                .complianceAutomationROI("340%")
                .annualCostSavings(96720L)
                .timeEfficiencyGain(87.5)
                .downloadUrl("/api/v1/compliance/report/download/compliance-report-2024.pdf")
                .fileSizeBytes(2048000L)
                .availableFormats(List.of("PDF", "CSV", "XML", "JSON", "HTML"))
                .bfdiCompliant(true)
                .stateAuthorityCompliant(true)
                .auditReady(true)
                .lastAuthorityContactHandled(LocalDateTime.now().minusMonths(3))
                .reportVersion("1.2.3")
                .complianceStandards(List.of(
                    "DSGVO (EU 2016/679)",
                    "EU AI Act (EU 2024/1689)",
                    "BDSG (Deutschland)",
                    "BfDI-Leitlinien",
                    "ISO 27001"
                ))
                .language("DE")
                .nextReportRecommended(LocalDateTime.now().plusMonths(3))
                .build();
    }

    /**
     * Erstellt Executive-Summary-Response
     */
    public static ComplianceReportResponse createExecutiveResponse() {
        return ComplianceReportResponse.builder()
                .success(true)
                .reportType("EXECUTIVE")
                .format("PDF")
                .generatedAt(LocalDateTime.now())
                .organization("Mustermann Software GmbH")
                .overallComplianceScore(87.5)
                .gdprStatus("COMPLIANT")
                .aiActStatus("NEEDS_ATTENTION")
                .criticalIssues(List.of(
                    "CE-Kennzeichnung für 1 Hochrisiko-KI-System erforderlich"
                ))
                .recommendedActions(List.of(
                    "🔴 Konformitätsbewertung für KI-System starten",
                    "📋 EU AI Act Compliance-Check durchführen",
                    "💰 340% ROI durch Compliance-Automatisierung realisiert"
                ))
                .businessMetrics(Map.of(
                    "executive_summary", "87.5% Compliance-Score - Gut positioniert",
                    "key_risk", "EU AI Act Hochrisiko-System",
                    "business_impact", "340% ROI, 96.720€ jährliche Ersparnis",
                    "action_required", "CE-Kennzeichnung binnen 60 Tagen"
                ))
                .complianceAutomationROI("340%")
                .annualCostSavings(96720L)
                .auditReady(true)
                .build();
    }

    /**
     * Erstellt Audit-Ready-Response für Behörden
     */
    public static ComplianceReportResponse createAuditResponse() {
        return ComplianceReportResponse.builder()
                .success(true)
                .reportType("AUDIT")
                .format("PDF")
                .generatedAt(LocalDateTime.now())
                .organization("Mustermann Software GmbH")
                .overallComplianceScore(92.0)
                .gdprStatus("COMPLIANT")
                .aiActStatus("COMPLIANT")
                .vvtEntries(6)
                .dsfaAssessments(3)
                .aiSystems(3)
                .highRiskAISystems(0)
                .prohibitedAIPractices(0)
                .criticalIssues(List.of())
                .recommendedActions(List.of(
                    "Kontinuierliche Überwachung der Compliance-Standards",
                    "Regelmäßige Updates der VVT-Dokumentation",
                    "Jährliche Compliance-Audits durchführen"
                ))
                .complianceStandards(List.of(
                    "DSGVO Art. 30 - Vollständiges VVT dokumentiert",
                    "DSGVO Art. 35 - DSFAs für alle Hochrisiko-Verarbeitungen",
                    "EU AI Act - Alle KI-Systeme klassifiziert und konform",
                    "BDSG - Deutsche Besonderheiten berücksichtigt",
                    "BfDI-Leitlinien - Vollständig umgesetzt"
                ))
                .bfdiCompliant(true)
                .stateAuthorityCompliant(true)
                .auditReady(true)
                .language("DE")
                .build();
    }

    /**
     * Prüft ob Report demo-ready ist
     */
    public boolean isDemoReady() {
        return success && 
               overallComplianceScore > 0 && 
               reportType != null && 
               organization != null &&
               businessMetrics != null;
    }
}