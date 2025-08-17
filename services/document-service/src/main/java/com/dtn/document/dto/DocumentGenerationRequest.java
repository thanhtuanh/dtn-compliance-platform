package com.dtn.document.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

/**
 * Document Generation Request DTO
 * 
 * @author DTN Compliance Team
 * @version 1.0.0
 * @since 2025-08-17
 */
@Schema(description = "Request für PDF-Generierung (VVT/DSFA)")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentGenerationRequest {
    
    @Schema(description = "Dokumenttyp", example = "VVT", allowableValues = {"VVT", "DSFA"})
    @NotBlank(message = "Dokumenttyp ist erforderlich")
    private String documentType;
    
    @Schema(description = "Organisation/Firma", example = "Mustermann Software GmbH")
    @NotBlank(message = "Organisation ist erforderlich")
    private String organization;
    
    @Schema(description = "Anfordernde Person", example = "Max Mustermann")
    private String requestedBy;
    
    @Schema(description = "E-Mail für Benachrichtigungen", example = "max.mustermann@example.com")
    private String notificationEmail;
    
    @Schema(description = "Zusätzliche Metadaten")
    private Map<String, Object> metadata;
    
    @Schema(description = "Template-Variablen für Anpassungen")
    private Map<String, Object> templateVariables;
    
    @Schema(description = "Corporate Design aktivieren", example = "true")
    private boolean corporateDesign = true;
    
    @Schema(description = "PDF-Qualität", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    private String quality = "HIGH";

    // Constructors
    public DocumentGenerationRequest() {}
    
    public DocumentGenerationRequest(String documentType, String organization) {
        this.documentType = documentType;
        this.organization = organization;
    }

    // Builder Pattern
    public static DocumentGenerationRequestBuilder builder() {
        return new DocumentGenerationRequestBuilder();
    }

    public static class DocumentGenerationRequestBuilder {
        private DocumentGenerationRequest request = new DocumentGenerationRequest();

        public DocumentGenerationRequestBuilder documentType(String documentType) {
            request.documentType = documentType;
            return this;
        }

        public DocumentGenerationRequestBuilder organization(String organization) {
            request.organization = organization;
            return this;
        }

        public DocumentGenerationRequestBuilder requestedBy(String requestedBy) {
            request.requestedBy = requestedBy;
            return this;
        }

        public DocumentGenerationRequestBuilder notificationEmail(String email) {
            request.notificationEmail = email;
            return this;
        }

        public DocumentGenerationRequestBuilder metadata(Map<String, Object> metadata) {
            request.metadata = metadata;
            return this;
        }

        public DocumentGenerationRequestBuilder templateVariables(Map<String, Object> variables) {
            request.templateVariables = variables;
            return this;
        }

        public DocumentGenerationRequestBuilder corporateDesign(boolean corporateDesign) {
            request.corporateDesign = corporateDesign;
            return this;
        }

        public DocumentGenerationRequestBuilder quality(String quality) {
            request.quality = quality;
            return this;
        }

        public DocumentGenerationRequest build() {
            return request;
        }
    }

    // Getters and Setters
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    
    public String getNotificationEmail() { return notificationEmail; }
    public void setNotificationEmail(String notificationEmail) { this.notificationEmail = notificationEmail; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public Map<String, Object> getTemplateVariables() { return templateVariables; }
    public void setTemplateVariables(Map<String, Object> templateVariables) { this.templateVariables = templateVariables; }
    
    public boolean isCorporateDesign() { return corporateDesign; }
    public void setCorporateDesign(boolean corporateDesign) { this.corporateDesign = corporateDesign; }
    
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
}