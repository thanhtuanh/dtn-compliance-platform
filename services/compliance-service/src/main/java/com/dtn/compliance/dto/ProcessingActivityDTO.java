package com.dtn.compliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * Processing Activity DTO
 * 
 * Repräsentiert eine einzelne Verarbeitungstätigkeit nach DSGVO Art. 30
 * 
 * Pflichtangaben nach DSGVO Art. 30:
 * - Name und Kontaktdaten des Verantwortlichen
 * - Zwecke der Verarbeitung
 * - Beschreibung der Kategorien betroffener Personen
 * - Beschreibung der Kategorien personenbezogener Daten
 * - Kategorien von Empfängern
 * - Übermittlungen in Drittländer
 * - Fristen für die Löschung
 * - Allgemeine Beschreibung der technischen und organisatorischen Maßnahmen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Verarbeitungstätigkeit nach DSGVO Art. 30")
public class ProcessingActivityDTO {

    @Schema(description = "Name der Verarbeitungstätigkeit", 
            example = "Mitarbeiterdatenverwaltung",
            required = true)
    private String name;

    @Schema(description = "Zweck der Verarbeitung", 
            example = "Personalverwaltung, Gehaltsabrechnung, Sozialversicherung",
            required = true)
    private String purpose;

    @Schema(description = "Kategorien betroffener Personen", 
            example = "[\"Mitarbeiter\", \"Bewerber\", \"Praktikanten\"]",
            required = true)
    private List<String> dataSubjectCategories;

    @Schema(description = "Kategorien personenbezogener Daten", 
            example = "[\"Stammdaten\", \"Gehaltsdaten\", \"Arbeitszeitdaten\"]",
            required = true)
    private List<String> dataCategories;

    @Schema(description = "Empfänger der Daten", 
            example = "[\"Lohnbuchhaltung\", \"Sozialversicherungsträger\"]")
    private List<String> recipients;

    @Schema(description = "Übermittlung in Drittländer", 
            example = "false")
    private boolean thirdCountryTransfer;

    @Schema(description = "Löschfristen", 
            example = "10 Jahre nach Beendigung des Arbeitsverhältnisses",
            required = true)
    private String retentionPeriod;

    @Schema(description = "Rechtsgrundlage nach DSGVO", 
            example = "Art. 6 Abs. 1 lit. b, c DSGVO (Vertrag, rechtliche Verpflichtung)",
            required = true)
    private String legalBasis;

    @Schema(description = "Technische Maßnahmen (TOM)", 
            example = "[\"Verschlüsselung\", \"Zugriffskontrolle\", \"Backup-System\"]")
    private List<String> technicalMeasures;

    @Schema(description = "Organisatorische Maßnahmen (TOM)", 
            example = "[\"Schulungen\", \"Berechtigungskonzept\", \"Incident Response\"]")
    private List<String> organizationalMeasures;

    @Schema(description = "Risikobewertung (niedrig/mittel/hoch)", 
            example = "niedrig")
    @Builder.Default
    private String riskLevel = "niedrig";

    @Schema(description = "DSFA erforderlich nach Art. 35 DSGVO", 
            example = "false")
    @Builder.Default
    private boolean dsfaRequired = false;

    @Schema(description = "Zusätzliche Bemerkungen und Hinweise")
    private String comments;

    // Business Logic Helper-Methoden

    /**
     * Prüft ob alle DSGVO Art. 30 Pflichtfelder ausgefüllt sind
     */
    public boolean isGdprCompliant() {
        return isNotBlank(name) &&
               isNotBlank(purpose) &&
               hasContent(dataSubjectCategories) &&
               hasContent(dataCategories) &&
               isNotBlank(retentionPeriod) &&
               isNotBlank(legalBasis);
    }

    /**
     * Prüft ob technische und organisatorische Maßnahmen definiert sind
     */
    public boolean hasTechnicalOrganizationalMeasures() {
        return hasContent(technicalMeasures) || hasContent(organizationalMeasures);
    }

    /**
     * Berechnet Compliance-Score für diese Verarbeitungstätigkeit (0-100)
     */
    public double calculateComplianceScore() {
        double score = 0.0;
        double maxScore = 100.0;
        
        // Pflichtfelder DSGVO Art. 30 (60 Punkte)
        if (isNotBlank(name)) score += 10;
        if (isNotBlank(purpose)) score += 10;
        if (isNotBlank(legalBasis)) score += 10;
        if (hasContent(dataCategories)) score += 10;
        if (isNotBlank(retentionPeriod)) score += 10;
        if (hasContent(dataSubjectCategories)) score += 10;
        
        // TOM - Technische und Organisatorische Maßnahmen (30 Punkte)
        if (hasContent(technicalMeasures)) score += 15;
        if (hasContent(organizationalMeasures)) score += 15;
        
        // Qualitätskriterien (10 Punkte)
        if (hasContent(recipients)) score += 3;
        if (isValidGermanLegalBasis()) score += 3;
        if (thirdCountryTransfer && hasThirdCountryGarantees()) score += 2;
        if (isNotBlank(riskLevel)) score += 2;
        
        return (score / maxScore) * 100.0;
    }

    /**
     * Prüft ob deutsche DSGVO-Rechtsgrundlagen korrekt verwendet werden
     */
    public boolean isValidGermanLegalBasis() {
        if (legalBasis == null) return false;
        
        return legalBasis.contains("Art. 6") || 
               legalBasis.contains("Art. 9") ||
               legalBasis.contains("BDSG") ||
               legalBasis.contains("berechtigtes Interesse") ||
               legalBasis.contains("Einwilligung") ||
               legalBasis.contains("Vertrag") ||
               legalBasis.contains("rechtliche Verpflichtung");
    }

    /**
     * Prüft ob bei Drittlandübermittlung Garantien erwähnt sind
     */
    public boolean hasThirdCountryGarantees() {
        if (!thirdCountryTransfer || comments == null) return false;
        
        String commentsLower = comments.toLowerCase();
        return commentsLower.contains("scc") ||
               commentsLower.contains("standardvertragsklauseln") ||
               commentsLower.contains("angemessenheitsbeschluss") ||
               commentsLower.contains("garantien");
    }

    /**
     * Generiert Verbesserungsvorschläge für diese Verarbeitungstätigkeit
     */
    public List<String> generateImprovementSuggestions() {
        var suggestions = new java.util.ArrayList<String>();
        
        if (!hasTechnicalOrganizationalMeasures()) {
            suggestions.add("Technische und organisatorische Maßnahmen (TOM) ergänzen");
        }
        
        if (thirdCountryTransfer && !hasThirdCountryGarantees()) {
            suggestions.add("Garantien für Drittlandübermittlung dokumentieren (SCCs/Angemessenheitsbeschluss)");
        }
        
        if ("hoch".equals(riskLevel) && !dsfaRequired) {
            suggestions.add("DSFA-Erforderlichkeit für Hochrisiko-Verarbeitung prüfen");
        }
        
        if (legalBasis != null && legalBasis.toLowerCase().contains("berechtigtes interesse")) {
            suggestions.add("Interessenabwägung für berechtigtes Interesse dokumentieren");
        }
        
        if (retentionPeriod != null && retentionPeriod.toLowerCase().contains("unbegrenzt")) {
            suggestions.add("Unbegrenzte Speicherung überprüfen - DSGVO Art. 5 Abs. 1 lit. e");
        }
        
        return suggestions;
    }

    /**
     * Prüft ob diese Verarbeitungstätigkeit EU AI Act relevant ist
     */
    public boolean isAIActRelevant() {
        if (name == null && purpose == null) return false;
        
        String combined = (name + " " + purpose).toLowerCase();
        return combined.contains("ki") ||
               combined.contains("ai") ||
               combined.contains("algorithmus") ||
               combined.contains("automatisiert") ||
               combined.contains("machine learning") ||
               combined.contains("künstliche intelligenz");
    }

    /**
     * Gibt deutsche Compliance-Zusammenfassung zurück
     */
    public String getGermanComplianceSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("Verarbeitungstätigkeit '%s': ", name));
        
        if (isGdprCompliant()) {
            summary.append("DSGVO Art. 30 konform. ");
        } else {
            summary.append("DSGVO-Lücken vorhanden. ");
        }
        
        summary.append(String.format("Risiko: %s. ", riskLevel));
        
        if (dsfaRequired) {
            summary.append("DSFA erforderlich. ");
        }
        
        if (thirdCountryTransfer) {
            summary.append("Drittlandübermittlung. ");
        }
        
        if (isAIActRelevant()) {
            summary.append("EU AI Act relevant.");
        }
        
        return summary.toString();
    }

    // Helper-Methoden für Validierung
    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    private boolean hasContent(List<String> list) {
        return list != null && !list.isEmpty() && 
               list.stream().anyMatch(item -> item != null && !item.trim().isEmpty());
    }

    // Equals und HashCode für Vergleiche
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessingActivityDTO that = (ProcessingActivityDTO) o;
        return Objects.equals(name, that.name) && 
               Objects.equals(purpose, that.purpose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, purpose);
    }

    /**
     * Erstellt Demo-Verarbeitungstätigkeit für Bewerbungspräsentationen
     */
    public static ProcessingActivityDTO createDemoActivity() {
        return ProcessingActivityDTO.builder()
                .name("Mitarbeiterdatenverwaltung")
                .purpose("Personalverwaltung, Gehaltsabrechnung, Sozialversicherung")
                .dataSubjectCategories(List.of("Mitarbeiter", "Bewerber", "Praktikanten"))
                .dataCategories(List.of("Stammdaten", "Gehaltsdaten", "Arbeitszeitdaten"))
                .recipients(List.of("Lohnbuchhaltung", "Sozialversicherungsträger", "Finanzamt"))
                .thirdCountryTransfer(false)
                .retentionPeriod("10 Jahre nach Beendigung des Arbeitsverhältnisses")
                .legalBasis("Art. 6 Abs. 1 lit. b, c DSGVO (Vertrag, rechtliche Verpflichtung)")
                .technicalMeasures(List.of("AES-256 Verschlüsselung", "Rollenbasierte Zugriffskontrolle", "Audit-Logging"))
                .organizationalMeasures(List.of("Datenschutzschulung", "Berechtigungskonzept", "Incident Response Plan"))
                .riskLevel("niedrig")
                .dsfaRequired(false)
                .comments("Standard HR-Verarbeitung nach deutschem Arbeitsrecht")
                .build();
    }
}