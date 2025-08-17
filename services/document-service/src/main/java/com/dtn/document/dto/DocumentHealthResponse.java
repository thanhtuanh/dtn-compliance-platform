package com.dtn.document.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Document Health Response DTO
 * 
 * @author DTN Compliance Team
 * @version 1.0.0
 * @since 2025-08-17
 */
@Schema(description = "Health Check Response für Document Service")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentHealthResponse {
    
    @Schema(description = "Service-Status", example = "UP", allowableValues = {"UP", "DOWN", "MAINTENANCE"})
    @NotNull
    private String status;
    
    @Schema(description = "Zeitstempel des Health Checks")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @Schema(description = "Service-Name", example = "DTN Document Service")
    private String serviceName;
    
    @Schema(description = "Service-Version", example = "1.0.0")
    private String version;
    
    @Schema(description = "Templates verfügbar", example = "true")
    private Boolean templatesAvailable;
    
    @Schema(description = "PDF-Engine Status", example = "iText 8.0.2 - Operational")
    private String pdfEngineStatus;
    
    @Schema(description = "Verarbeitungskapazität in %", example = "85")
    private Integer processingCapacity;
    
    @Schema(description = "Aktuelle Auslastung", example = "3/10 PDF-Generierungen aktiv")
    private String currentLoad;
    
    @Schema(description = "Fehler-Details (nur bei Status DOWN)")
    private String error;
    
    @Schema(description = "Zusätzliche Systeminformationen")
    private Map<String, Object> systemInfo;

    // Constructors
    public DocumentHealthResponse() {}

    // Builder Pattern
    public static DocumentHealthResponseBuilder builder() {
        return new DocumentHealthResponseBuilder();
    }

    public static class DocumentHealthResponseBuilder {
        private DocumentHealthResponse response = new DocumentHealthResponse();

        public DocumentHealthResponseBuilder status(String status) {
            response.status = status;
            return this;
        }

        public DocumentHealthResponseBuilder timestamp(LocalDateTime timestamp) {
            response.timestamp = timestamp;
            return this;
        }

        public DocumentHealthResponseBuilder serviceName(String serviceName) {
            response.serviceName = serviceName;
            return this;
        }

        public DocumentHealthResponseBuilder version(String version) {
            response.version = version;
            return this;
        }

        public DocumentHealthResponseBuilder templatesAvailable(Boolean available) {
            response.templatesAvailable = available;
            return this;
        }

        public DocumentHealthResponseBuilder pdfEngineStatus(String status) {
            response.pdfEngineStatus = status;
            return this;
        }

        public DocumentHealthResponseBuilder processingCapacity(Integer capacity) {
            response.processingCapacity = capacity;
            return this;
        }

        public DocumentHealthResponseBuilder currentLoad(String load) {
            response.currentLoad = load;
            return this;
        }

        public DocumentHealthResponseBuilder error(String error) {
            response.error = error;
            return this;
        }

        public DocumentHealthResponseBuilder systemInfo(Map<String, Object> info) {
            response.systemInfo = info;
            return this;
        }

        public DocumentHealthResponse build() {
            return response;
        }
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public Boolean getTemplatesAvailable() { return templatesAvailable; }
    public void setTemplatesAvailable(Boolean templatesAvailable) { this.templatesAvailable = templatesAvailable; }
    
    public String getPdfEngineStatus() { return pdfEngineStatus; }
    public void setPdfEngineStatus(String pdfEngineStatus) { this.pdfEngineStatus = pdfEngineStatus; }
    
    public Integer getProcessingCapacity() { return processingCapacity; }
    public void setProcessingCapacity(Integer processingCapacity) { this.processingCapacity = processingCapacity; }
    
    public String getCurrentLoad() { return currentLoad; }
    public void setCurrentLoad(String currentLoad) { this.currentLoad = currentLoad; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public Map<String, Object> getSystemInfo() { return systemInfo; }
    public void setSystemInfo(Map<String, Object> systemInfo) { this.systemInfo = systemInfo; }
}