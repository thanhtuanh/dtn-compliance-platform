package com.dtn.document.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service Statistics Response DTO + Helper Classes
 * 
 * @author DTN Compliance Team
 * @version 1.0.0
 * @since 2025-08-17
 */

// ===== MAIN RESPONSE =====

@Schema(description = "Service-Statistiken für Monitoring und Performance")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceStatisticsResponse {
    
    @Schema(description = "Zeitstempel der Statistik-Erstellung")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @Schema(description = "Service-Uptime", example = "2 Tage, 14 Stunden, 32 Minuten")
    private String uptime;
    
    @Schema(description = "Gesamt generierte PDFs", example = "1247")
    private Long totalGeneratedPdfs;
    
    @Schema(description = "VVT PDFs generiert", example = "823")
    private Long vvtPdfsGenerated;
    
    @Schema(description = "DSFA PDFs generiert", example = "424")
    private Long dsfaPdfsGenerated;
    
    @Schema(description = "Durchschnittliche Verarbeitungszeit in ms", example = "1850")
    private Long averageProcessingTimeMs;
    
    @Schema(description = "Kürzeste Verarbeitungszeit in ms", example = "892")
    private Long fastestProcessingTimeMs;
    
    @Schema(description = "Längste Verarbeitungszeit in ms", example = "4250")
    private Long slowestProcessingTimeMs;
    
    @Schema(description = "Aktuelle Memory-Nutzung")
    private MemoryUsageInfo memoryUsage;
    
    @Schema(description = "Performance-Metriken der letzten 24h")
    private PerformanceMetrics last24Hours;
    
    @Schema(description = "Top 5 Organisationen nach PDF-Anzahl")
    private List<OrganizationUsageInfo> topOrganizations;
    
    @Schema(description = "Fehlerstatistiken")
    private ErrorStatistics errorStatistics;

    // Constructors
    public ServiceStatisticsResponse() {}

    // Getters and Setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getUptime() { return uptime; }
    public void setUptime(String uptime) { this.uptime = uptime; }
    
    public Long getTotalGeneratedPdfs() { return totalGeneratedPdfs; }
    public void setTotalGeneratedPdfs(Long totalGeneratedPdfs) { this.totalGeneratedPdfs = totalGeneratedPdfs; }
    
    public Long getVvtPdfsGenerated() { return vvtPdfsGenerated; }
    public void setVvtPdfsGenerated(Long vvtPdfsGenerated) { this.vvtPdfsGenerated = vvtPdfsGenerated; }
    
    public Long getDsfaPdfsGenerated() { return dsfaPdfsGenerated; }
    public void setDsfaPdfsGenerated(Long dsfaPdfsGenerated) { this.dsfaPdfsGenerated = dsfaPdfsGenerated; }
    
    public Long getAverageProcessingTimeMs() { return averageProcessingTimeMs; }
    public void setAverageProcessingTimeMs(Long averageProcessingTimeMs) { this.averageProcessingTimeMs = averageProcessingTimeMs; }
    
    public Long getFastestProcessingTimeMs() { return fastestProcessingTimeMs; }
    public void setFastestProcessingTimeMs(Long fastestProcessingTimeMs) { this.fastestProcessingTimeMs = fastestProcessingTimeMs; }
    
    public Long getSlowestProcessingTimeMs() { return slowestProcessingTimeMs; }
    public void setSlowestProcessingTimeMs(Long slowestProcessingTimeMs) { this.slowestProcessingTimeMs = slowestProcessingTimeMs; }
    
    public MemoryUsageInfo getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(MemoryUsageInfo memoryUsage) { this.memoryUsage = memoryUsage; }
    
    public PerformanceMetrics getLast24Hours() { return last24Hours; }
    public void setLast24Hours(PerformanceMetrics last24Hours) { this.last24Hours = last24Hours; }
    
    public List<OrganizationUsageInfo> getTopOrganizations() { return topOrganizations; }
    public void setTopOrganizations(List<OrganizationUsageInfo> topOrganizations) { this.topOrganizations = topOrganizations; }
    
    public ErrorStatistics getErrorStatistics() { return errorStatistics; }
    public void setErrorStatistics(ErrorStatistics errorStatistics) { this.errorStatistics = errorStatistics; }
}

// ===== HELPER CLASSES =====

/**
 * Memory Usage Information
 */
@Schema(description = "Memory-Nutzungsinformationen")
@JsonInclude(JsonInclude.Include.NON_NULL)
class MemoryUsageInfo {
    
    @Schema(description = "Verwendeter Speicher in MB", example = "256")
    private Long usedMemoryMB;
    
    @Schema(description = "Freier Speicher in MB", example = "768")
    private Long freeMemoryMB;
    
    @Schema(description = "Gesamt-Speicher in MB", example = "1024")
    private Long totalMemoryMB;
    
    @Schema(description = "Max-Speicher in MB", example = "2048")
    private Long maxMemoryMB;
    
    @Schema(description = "Speichernutzung in %", example = "25")
    private Integer usagePercent;

    // Constructors, Getters and Setters
    public MemoryUsageInfo() {}
    
    public Long getUsedMemoryMB() { return usedMemoryMB; }
    public void setUsedMemoryMB(Long usedMemoryMB) { this.usedMemoryMB = usedMemoryMB; }
    
    public Long getFreeMemoryMB() { return freeMemoryMB; }
    public void setFreeMemoryMB(Long freeMemoryMB) { this.freeMemoryMB = freeMemoryMB; }
    
    public Long getTotalMemoryMB() { return totalMemoryMB; }
    public void setTotalMemoryMB(Long totalMemoryMB) { this.totalMemoryMB = totalMemoryMB; }
    
    public Long getMaxMemoryMB() { return maxMemoryMB; }
    public void setMaxMemoryMB(Long maxMemoryMB) { this.maxMemoryMB = maxMemoryMB; }
    
    public Integer getUsagePercent() { return usagePercent; }
    public void setUsagePercent(Integer usagePercent) { this.usagePercent = usagePercent; }
}

/**
 * Performance Metrics
 */
@Schema(description = "Performance-Metriken für spezifischen Zeitraum")
@JsonInclude(JsonInclude.Include.NON_NULL)
class PerformanceMetrics {
    
    @Schema(description = "Anzahl Requests", example = "156")
    private Long requestCount;
    
    @Schema(description = "Erfolgreiche Requests", example = "152")
    private Long successfulRequests;
    
    @Schema(description = "Fehlgeschlagene Requests", example = "4")
    private Long failedRequests;
    
    @Schema(description = "Durchschnittliche Response-Zeit in ms", example = "1650")
    private Long averageResponseTimeMs;
    
    @Schema(description = "Requests pro Stunde", example = "6.5")
    private Double requestsPerHour;
    
    @Schema(description = "Erfolgsrate in %", example = "97.4")
    private Double successRate;

    // Constructors, Getters and Setters
    public PerformanceMetrics() {}
    
    public Long getRequestCount() { return requestCount; }
    public void setRequestCount(Long requestCount) { this.requestCount = requestCount; }
    
    public Long getSuccessfulRequests() { return successfulRequests; }
    public void setSuccessfulRequests(Long successfulRequests) { this.successfulRequests = successfulRequests; }
    
    public Long getFailedRequests() { return failedRequests; }
    public void setFailedRequests(Long failedRequests) { this.failedRequests = failedRequests; }
    
    public Long getAverageResponseTimeMs() { return averageResponseTimeMs; }
    public void setAverageResponseTimeMs(Long averageResponseTimeMs) { this.averageResponseTimeMs = averageResponseTimeMs; }
    
    public Double getRequestsPerHour() { return requestsPerHour; }
    public void setRequestsPerHour(Double requestsPerHour) { this.requestsPerHour = requestsPerHour; }
    
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
}

/**
 * Organization Usage Information
 */
@Schema(description = "Nutzungsstatistiken pro Organisation")
@JsonInclude(JsonInclude.Include.NON_NULL)
class OrganizationUsageInfo {
    
    @Schema(description = "Organisations-Name", example = "Mustermann Software GmbH")
    private String organizationName;
    
    @Schema(description = "Anzahl generierter PDFs", example = "47")
    private Long pdfCount;
    
    @Schema(description = "VVT PDFs", example = "32")
    private Long vvtCount;
    
    @Schema(description = "DSFA PDFs", example = "15")
    private Long dsfaCount;
    
    @Schema(description = "Letzter PDF-Request")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastRequest;

    // Constructors, Getters and Setters
    public OrganizationUsageInfo() {}
    
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    
    public Long getPdfCount() { return pdfCount; }
    public void setPdfCount(Long pdfCount) { this.pdfCount = pdfCount; }
    
    public Long getVvtCount() { return vvtCount; }
    public void setVvtCount(Long vvtCount) { this.vvtCount = vvtCount; }
    
    public Long getDsfaCount() { return dsfaCount; }
    public void setDsfaCount(Long dsfaCount) { this.dsfaCount = dsfaCount; }
    
    public LocalDateTime getLastRequest() { return lastRequest; }
    public void setLastRequest(LocalDateTime lastRequest) { this.lastRequest = lastRequest; }
}

/**
 * Error Statistics
 */
@Schema(description = "Fehlerstatistiken für Monitoring")
@JsonInclude(JsonInclude.Include.NON_NULL)
class ErrorStatistics {
    
    @Schema(description = "Gesamt-Fehler", example = "12")
    private Long totalErrors;
    
    @Schema(description = "Template-Fehler", example = "3")
    private Long templateErrors;
    
    @Schema(description = "PDF-Generierungsfehler", example = "7")
    private Long pdfGenerationErrors;
    
    @Schema(description = "System-Fehler", example = "2")
    private Long systemErrors;
    
    @Schema(description = "Fehlerrate in %", example = "0.8")
    private Double errorRate;
    
    @Schema(description = "Letzter Fehler")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastError;
    
    @Schema(description = "Häufigste Fehlermeldung", example = "Template not found: custom-template.ftl")
    private String mostCommonError;

    // Constructors, Getters and Setters
    public ErrorStatistics() {}
    
    public Long getTotalErrors() { return totalErrors; }
    public void setTotalErrors(Long totalErrors) { this.totalErrors = totalErrors; }
    
    public Long getTemplateErrors() { return templateErrors; }
    public void setTemplateErrors(Long templateErrors) { this.templateErrors = templateErrors; }
    
    public Long getPdfGenerationErrors() { return pdfGenerationErrors; }
    public void setPdfGenerationErrors(Long pdfGenerationErrors) { this.pdfGenerationErrors = pdfGenerationErrors; }
    
    public Long getSystemErrors() { return systemErrors; }
    public void setSystemErrors(Long systemErrors) { this.systemErrors = systemErrors; }
    
    public Double getErrorRate() { return errorRate; }
    public void setErrorRate(Double errorRate) { this.errorRate = errorRate; }
    
    public LocalDateTime getLastError() { return lastError; }
    public void setLastError(LocalDateTime lastError) { this.lastError = lastError; }
    
    public String getMostCommonError() { return mostCommonError; }
    public void setMostCommonError(String mostCommonError) { this.mostCommonError = mostCommonError; }
}