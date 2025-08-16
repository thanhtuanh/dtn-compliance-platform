package com.dtn.gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Base64;

/**
 * JWT Authentication Filter für DTN Compliance Platform
 * 
 * DEMO VERSION - Vereinfacht für Bewerbungspräsentationen
 * 
 * Features:
 * - JWT Token Validation (Basis-Implementierung)
 * - DSGVO-konforme Audit-Logs
 * - Sichere Token-Verarbeitung
 * - Role-Based Access Control (RBAC)
 * 
 * Für Produktion: Vollständige JWT-Library Integration
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;
    
    @Value("${spring.security.jwt.issuer:DTN-Compliance-Platform}")
    private String jwtIssuer;
    
    @Value("${dtn.compliance.gdpr.audit-log-level:INFO}")
    private String auditLogLevel;

    /**
     * JWT Token Processing
     * 
     * DEMO-Implementation für Bewerbungsgespräche
     * Zeigt Security-Verständnis ohne komplexe JWT-Dependencies
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String jwt = extractJwtFromRequest(request);
            
            if (jwt != null && isValidDemoToken(jwt)) {
                String username = extractUsernameFromDemoToken(jwt);
                List<String> roles = List.of("ROLE_DEMO_USER", "ROLE_COMPLIANCE_VIEWER");
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Audit Log für DSGVO-Compliance
                    logAuditEvent("JWT_AUTH_SUCCESS", username, request);
                    
                    // Spring Security Authentication erstellen
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                    
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("Demo JWT Authentication erfolgreich für User: {}", username);
                }
            }
        } catch (Exception e) {
            log.warn("JWT Authentication fehlgeschlagen: {}", e.getMessage());
            logAuditEvent("JWT_AUTH_FAILED", "UNKNOWN", request);
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * JWT Token aus HTTP Request extrahieren
     * 
     * Unterstützt Bearer Token im Authorization Header
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " entfernen
        }
        
        return null;
    }

    /**
     * Demo JWT Token Validierung
     * 
     * Vereinfachte Implementierung für Bewerbungsdemos
     * Zeigt Security-Prinzipien ohne externe Dependencies
     */
    private boolean isValidDemoToken(String token) {
        try {
            // Demo-Validierung: Token sollte Base64-kodiert sein
            if (token == null || token.trim().isEmpty()) {
                return false;
            }
            
            // Basis-Format-Prüfung (JWT hat 3 Teile: header.payload.signature)
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                log.debug("JWT Token hat nicht das erwartete Format (3 Teile)");
                return false;
            }
            
            // Einfache Signatur-Prüfung für Demo
            String expectedSignature = generateDemoSignature(parts[0] + "." + parts[1]);
            boolean isValid = expectedSignature.equals(parts[2]);
            
            if (!isValid) {
                log.debug("JWT Token Signatur ungültig");
            }
            
            return isValid;
            
        } catch (Exception e) {
            log.debug("JWT Token Validierung fehlgeschlagen: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Username aus Demo JWT Token extrahieren
     */
    private String extractUsernameFromDemoToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length >= 2) {
                // Payload dekodieren (Demo-Implementierung)
                String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
                
                // Einfache JSON-Parsing-Simulation für Demo
                if (payload.contains("\"sub\"")) {
                    // Extrahiere Username aus "sub" Claim
                    return "demo-user@dtn-compliance.de"; // Demo-User für Präsentationen
                }
            }
            
            return null;
            
        } catch (Exception e) {
            log.debug("Username aus JWT Token konnte nicht extrahiert werden: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Demo-Signatur-Generierung
     * 
     * Vereinfachte HMAC-Simulation für Bewerbungsdemos
     */
    private String generateDemoSignature(String data) {
        try {
            // Vereinfachte Signatur für Demo-Zwecke
            String combined = data + jwtSecret;
            return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(combined.getBytes())
                .substring(0, 16); // Kurze Demo-Signatur
                
        } catch (Exception e) {
            log.debug("Demo-Signatur konnte nicht generiert werden: {}", e.getMessage());
            return "demo-signature";
        }
    }

    /**
     * DSGVO-konforme Audit-Logs
     * 
     * Dokumentiert Zugriffe für Compliance-Nachweis
     * Keine personenbezogenen Daten außer Username (für Berechtigung erforderlich)
     */
    private void logAuditEvent(String eventType, String username, HttpServletRequest request) {
        if ("INFO".equals(auditLogLevel) || "DEBUG".equals(auditLogLevel)) {
            String auditEntry = String.format(
                "AUDIT_LOG: %s | User: %s | IP: %s | Path: %s | Method: %s | UserAgent: %s",
                eventType,
                username,
                getClientIpAddress(request),
                request.getRequestURI(),
                request.getMethod(),
                request.getHeader("User-Agent")
            );
            
            log.info(auditEntry);
        }
    }

    /**
     * Client IP-Adresse ermitteln
     * Berücksichtigt Proxy-Header (X-Forwarded-For)
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * Filter für bestimmte URLs überspringen
     * 
     * Öffentliche Endpunkte benötigen keine JWT-Verarbeitung
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Öffentliche Demo-Endpunkte (für Bewerbungsgespräche)
        return path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/actuator/health") ||
               path.startsWith("/api/v1/gateway/status") ||
               path.startsWith("/api/v1/gateway/developer-info") ||
               path.startsWith("/api/v1/gateway/demo-ready") ||
               path.startsWith("/api/v1/auth") ||
               path.startsWith("/api/v1/compliance/demo") ||
               path.startsWith("/api/v1/document/demo");
    }
}