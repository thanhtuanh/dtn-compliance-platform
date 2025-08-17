package com.dtn.document.controller;

import com.dtn.document.service.PDFGenerationService;
import com.dtn.document.dto.DocumentGenerationRequest;
import com.dtn.document.dto.DocumentGenerationResponse;
import com.dtn.document.dto.DocumentHealthResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * DTN Document Controller - REST API für PDF-Generation
 * 
 * Hauptfunktionen:
 * - VVT (Verzeichnis von Verarbeitungstätigkeiten) PDF Export
 * - DSFA (Datenschutz-Folgenabschätzung) PDF Export
 * - Health Check für Service-Monitoring
 * - Asynchrone PDF-Generierung für Performance
 * 
 * Business Value:
 * - Automatisiert manuelle Dokumentenerstellung (8h → 15min)
 * - DSGVO Art. 30 & 35 konforme PDF-Ausgabe
 * - Corporate Design für professionelle Präsentation
 * - RESTful API für Frontend/Mobile Integration
 * 
 * @author DTN Compliance Team
 * @version 1.0.0
 * @since 2025-08-17
 */
@RestController
@RequestMapping("/api/v1/document")
@Tag(name = "Document Generation", description = "PDF-Generierung für DSGVO & EU AI Act Compliance")
@Validated
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    
    private final PDFGenerationService pdfGenerationService;

    @Autowired
    public DocumentController(PDFGenerationService pdfGenerationService) {
        this.pdfGenerationService = pdfGenerationService;
    }

    /**
     * Health Check Endpoint
     */
    @GetMapping("/health")
    @Operation(
        summary = "Service Health Check",
        description = "Überprüft den Status des Document Service und der PDF-Engine"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service ist operational"),
        @ApiResponse(responseCode = "503", description = "Service nicht verfügbar")
    })
    public ResponseEntity<DocumentHealthResponse> healthCheck() {
        try {
            logger.info("Health Check angefordert");
            
            boolean isHealthy = pdfGenerationService.isServiceHealthy();
            String status = isHealthy ? "UP" : "DOWN";
            
            DocumentHealthResponse response = DocumentHealthResponse.builder()
                .status(status)
                .timestamp(LocalDateTime.now())
                .serviceName("DTN Document Service")
                .version("1.0.0")
                .templatesAvailable(pdfGenerationService.areTemplatesAvailable())
                .pdfEngineStatus(pdfGenerationService.getPdfEngineStatus())
                .processingCapacity(pdfGenerationService.getProcessingCapacity())
                .build();
                
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Health Check fehlgeschlagen: {}", e.getMessage());
            
            DocumentHealthResponse response = DocumentHealthResponse.builder()
                .status("DOWN")
                .timestamp(LocalDateTime.now())
                .serviceName("DTN Document Service")
                .version("1.0.0")
                .error(e.getMessage())
                .build();
                
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }

    /**
     * VVT PDF Generierung
     */
    @PostMapping("/vvt")
    @Operation(
        summary = "VVT PDF generieren",
        description = "Generiert ein PDF-Dokument für das Verzeichnis von Verarbeitungstätigkeiten (DSGVO Art. 30)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF erfolgreich generiert",
                    content = @Content(mediaType = "application/pdf")),
        @ApiResponse(responseCode = "400", description = "Ungültige Anfrage"),
        @ApiResponse(responseCode = "500", description = "Fehler bei PDF-Generierung")
    })
    public ResponseEntity<ByteArrayResource> generateVVTPdf(
            @Parameter(description = "VVT-Daten für PDF-Generierung", required = true)
            @Valid @RequestBody DocumentGenerationRequest request) {
        
        try {
            logger.info("VVT PDF-Generierung gestartet für Organisation: {}", request.getOrganization());
            
            // PDF generieren
            byte[] pdfBytes = pdfGenerationService.generateVVTPdf(request);
            
            // Filename erstellen
            String filename = createFilename("VVT", request.getOrganization());
            
            // Response Headers setzen
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            headers.set("X-Document-Type", "VVT");
            headers.set("X-Generated-At", getCurrentTimestamp());
            
            logger.info("VVT PDF erfolgreich generiert. Größe: {} bytes", pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ByteArrayResource(pdfBytes));
                    
        } catch (Exception e) {
            logger.error("Fehler bei VVT PDF-Generierung: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DSFA PDF Generierung
     */
    @PostMapping("/dsfa")
    @Operation(
        summary = "DSFA PDF generieren", 
        description = "Generiert ein PDF-Dokument für die Datenschutz-Folgenabschätzung (DSGVO Art. 35)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF erfolgreich generiert",
                    content = @Content(mediaType = "application/pdf")),
        @ApiResponse(responseCode = "400", description = "Ungültige Anfrage"),
        @ApiResponse(responseCode = "500", description = "Fehler bei PDF-Generierung")
    })
    public ResponseEntity<ByteArrayResource> generateDSFAPdf(
            @Parameter(description = "DSFA-Daten für PDF-Generierung", required = true)
            @Valid @RequestBody DocumentGenerationRequest request) {
        
        try {
            logger.info("DSFA PDF-Generierung gestartet für Organisation: {}", request.getOrganization());
            
            // PDF generieren
            byte[] pdfBytes = pdfGenerationService.generateDSFAPdf(request);
            
            // Filename erstellen
            String filename = createFilename("DSFA", request.getOrganization());
            
            // Response Headers setzen
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            headers.set("X-Document-Type", "DSFA");
            headers.set("X-Generated-At", getCurrentTimestamp());
            
            logger.info("DSFA PDF erfolgreich generiert. Größe: {} bytes", pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ByteArrayResource(pdfBytes));
                    
        } catch (Exception e) {
            logger.error("Fehler bei DSFA PDF-Generierung: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Asynchrone PDF Generierung
     */
    @PostMapping("/generate-async")
    @Operation(
        summary = "Asynchrone PDF-Generierung",
        description = "Startet PDF-Generierung asynchron und gibt Request-ID zurück"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "PDF-Generierung gestartet"),
        @ApiResponse(responseCode = "400", description = "Ungültige Anfrage")
    })
    public ResponseEntity<DocumentGenerationResponse> generatePdfAsync(
            @Parameter(description = "Dokument-Daten für PDF-Generierung", required = true)
            @Valid @RequestBody DocumentGenerationRequest request) {
        
        try {
            logger.info("Asynchrone PDF-Generierung gestartet für Typ: {} Organisation: {}", 
                       request.getDocumentType(), request.getOrganization());
            
            // Asynchrone Verarbeitung starten
            CompletableFuture<String> future = pdfGenerationService.generatePdfAsync(request);
            
            DocumentGenerationResponse response = DocumentGenerationResponse.builder()
                .requestId(java.util.UUID.randomUUID().toString())
                .status("PROCESSING")
                .message("PDF-Generierung gestartet")
                .timestamp(LocalDateTime.now())
                .estimatedCompletionMinutes(2)
                .documentType(request.getDocumentType())
                .organization(request.getOrganization())
                .build();
            
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
            
        } catch (Exception e) {
            logger.error("Fehler bei asynchroner PDF-Generierung: {}", e.getMessage(), e);
            
            DocumentGenerationResponse response = DocumentGenerationResponse.builder()
                .status("ERROR")
                .message("Fehler bei PDF-Generierung: " + e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
                
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Template-Validierung
     */
    @GetMapping("/templates/validate")
    @Operation(
        summary = "Template-Validierung",
        description = "Überprüft die Verfügbarkeit und Gültigkeit der PDF-Templates"
    )
    public ResponseEntity<Map<String, Object>> validateTemplates() {
        try {
            logger.info("Template-Validierung angefordert");
            
            Map<String, Object> validationResult = pdfGenerationService.validateTemplates();
            
            return ResponseEntity.ok(validationResult);
            
        } catch (Exception e) {
            logger.error("Fehler bei Template-Validierung: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Service-Statistiken
     */
    @GetMapping("/stats")
    @Operation(
        summary = "Service-Statistiken",
        description = "Gibt Statistiken über generierte PDFs und Performance zurück"
    )
    public ResponseEntity<Map<String, Object>> getServiceStatistics() {
        try {
            logger.info("Service-Statistiken angefordert");
            
            Map<String, Object> stats = pdfGenerationService.getServiceStatistics();
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Fehler beim Abrufen der Service-Statistiken: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Demo-Endpoint für Bewerbungsgespräche
     */
    @GetMapping("/demo")
    @Operation(
        summary = "Demo PDF generieren",
        description = "Generiert Demo-PDF für Bewerbungspräsentationen"
    )
    public ResponseEntity<ByteArrayResource> generateDemoDocument(
            @Parameter(description = "Demo-Typ") @RequestParam(defaultValue = "VVT") String type) {
        
        try {
            logger.info("Demo PDF-Generierung für Typ: {}", type);
            
            // Demo-Request erstellen
            DocumentGenerationRequest demoRequest = DocumentGenerationRequest.builder()
                .documentType(type)
                .organization("Demo AG - Bewerbungspräsentation")
                .requestedBy("DTN Compliance Team")
                .metadata(Map.of(
                    "demo", "true",
                    "presentation", "Bewerbungsgespräch",
                    "features", "DSGVO Art. 30 & 35 Automatisierung"
                ))
                .build();
            
            byte[] pdfBytes = "VVT".equals(type) ? 
                pdfGenerationService.generateVVTPdf(demoRequest) :
                pdfGenerationService.generateDSFAPdf(demoRequest);
            
            String filename = "DEMO_" + type + "_DTN_Compliance_" + getCurrentTimestamp() + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.set("X-Demo-Purpose", "Bewerbungspräsentation");
            
            return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(pdfBytes));
            
        } catch (Exception e) {
            logger.error("Fehler bei Demo PDF-Generierung: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ===== Helper Methods =====

    private String createFilename(String documentType, String organization) {
        String sanitizedOrg = organization.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        return String.format("%s_%s_%s.pdf", 
                           documentType, 
                           sanitizedOrg, 
                           getCurrentTimestamp());
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }
}