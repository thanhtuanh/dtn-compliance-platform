package com.dtn.document.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Document Generation Response DTO
 * 
 * @author DTN Compliance Team
 * @version 1.0.0
 * @since 2025-08-17
 */
@Schema(description = "Response für PDF-Generierung")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentGenerationResponse {
    
    @Schema(description = "Request-ID für Tracking", example = "REQ_20250817_123456")
    private String requestId;
    
    @Schema(description = "Verarbeitungsstatus", example = "COMPLETED", allowableValues = {"PROCESSING", "COMPLETED", "ERROR"})
    private String status;
    
    @Schema(description = "Status-Nachricht", example = "PDF erfolgreich generiert")
    private String message;
    
    @Schema(description = "Zeitstempel der Response")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @Schema(description = "Geschätzte Fertigstellung in Minuten", example = "2")
    private Integer estimatedCompletionMinutes;
    
    @Schema(description = "Dokumenttyp", example = "VVT")
    private String documentType;
    
    @Schema(description = "Organisation", example = "Mustermann Software GmbH")
    private String organization;
    
    @Schema(description = "PDF-Dateigröße in Bytes", example = "1048576")
    private Long fileSizeBytes;
    
    @Schema(description = "Download-URL (bei async Generierung)")
    private String downloadUrl;
    
    @Schema(description = "Verarbeitungszeit in Millisekunden", example = "1500")
    private Long processingTimeMs;

    // Constructors
    public DocumentGenerationResponse() {}

    // Builder Pattern
    public static DocumentGenerationResponseBuilder builder() {
        return new DocumentGenerationResponseBuilder();
    }

    public static class DocumentGenerationResponseBuilder {
        private DocumentGenerationResponse response = new DocumentGenerationResponse();

        public DocumentGenerationResponseBuilder requestId(String requestId) {
            response.requestId = requestId;
            return this;
        }

        public DocumentGenerationResponseBuilder status(String status) {
            response.status = status;
            return this;
        }

        public DocumentGenerationResponseBuilder message(String message) {
            response.message = message;
            return this;
        }

        public DocumentGenerationResponseBuilder timestamp(LocalDateTime timestamp) {
            response.timestamp = timestamp;
            return this;
        }

        public DocumentGenerationResponseBuilder estimatedCompletionMinutes(Integer minutes) {
            response.estimatedCompletionMinutes = minutes;
            return this;
        }

        public DocumentGenerationResponseBuilder documentType(String documentType) {
            response.documentType = documentType;
            return this;
        }

        public DocumentGenerationResponseBuilder organization(String organization) {
            response.organization = organization;
            return this;
        }

        public DocumentGenerationResponseBuilder fileSizeBytes(Long fileSize) {
            response.fileSizeBytes = fileSize;
            return this;
        }

        public DocumentGenerationResponseBuilder downloadUrl(String url) {
            response.downloadUrl = url;
            return this;
        }

        public DocumentGenerationResponseBuilder processingTimeMs(Long timeMs) {
            response.processingTimeMs = timeMs;
            return this;
        }

        public DocumentGenerationResponse build() {
            return response;
        }
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public Integer getEstimatedCompletionMinutes() { return estimatedCompletionMinutes; }
    public void setEstimatedCompletionMinutes(Integer estimatedCompletionMinutes) { this.estimatedCompletionMinutes = estimatedCompletionMinutes; }
    
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    
    public Long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
}