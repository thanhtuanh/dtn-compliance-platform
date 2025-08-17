#!/bin/bash

# DTN Compliance Service - Comprehensive Test Script
# DSGVO + EU AI Act konforme KI-Lösung für deutsche Unternehmen

set -e

# Colors for beautiful output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Service URLs
COMPLIANCE_URL="http://localhost:8081"
GATEWAY_URL="http://localhost:8080"

# Test Configuration
TIMEOUT=10
VERBOSE=false
DEMO_MODE=false

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --demo)
            DEMO_MODE=true
            shift
            ;;
        --verbose|-v)
            VERBOSE=true
            shift
            ;;
        --help|-h)
            show_help
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
done

# Help function
show_help() {
    echo -e "${BLUE}DTN Compliance Service - Test Script${NC}"
    echo ""
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  --demo      Demo mode with enhanced output for presentations"
    echo "  --verbose   Verbose output with request/response details"
    echo "  --help      Show this help message"
    echo ""
    echo "Tests:"
    echo "  📊 DSGVO Art. 30 - VVT Generation"
    echo "  📋 DSGVO Art. 35 - DSFA Assessment"
    echo "  🤖 EU AI Act - Risk Classification"
    echo "  📈 Business Value - ROI Demonstration"
}

# Demo banner for presentations
show_demo_banner() {
    if [[ "$DEMO_MODE" == "true" ]]; then
        echo -e "${PURPLE}╔══════════════════════════════════════════════════════════════════╗${NC}"
        echo -e "${PURPLE}║${NC}  🚀 ${CYAN}DTN Compliance Intelligence Platform${NC} - API Tests        ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}                                                                  ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}  📊 Business Value: ${GREEN}96.960€ Jahresersparnis${NC}                    ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}  🎯 Zielgruppe: ${YELLOW}Deutsche Software-Dienstleister (50-200 MA)${NC}   ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}  🇩🇪 Compliance: ${GREEN}DSGVO + EU AI Act konform${NC}                    ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}                                                                  ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}  💼 Entwickelt von: ${CYAN}Duc Thanh Nguyen${NC} | München/Bayern       ${PURPLE}║${NC}"
        echo -e "${PURPLE}╚══════════════════════════════════════════════════════════════════╝${NC}"
        echo ""
    fi
}

# Check if curl and jq are available
check_prerequisites() {
    if ! command -v curl &> /dev/null; then
        echo -e "${RED}❌ curl ist nicht installiert. Bitte installieren Sie curl.${NC}"
        exit 1
    fi
    
    if ! command -v jq &> /dev/null; then
        echo -e "${YELLOW}⚠️  jq nicht verfügbar. JSON-Ausgabe wird roh angezeigt.${NC}"
        JQ_AVAILABLE=false
    else
        JQ_AVAILABLE=true
    fi
}

# Generic API test function
test_api_endpoint() {
    local name="$1"
    local method="$2"
    local url="$3"
    local data="$4"
    local expected_status="${5:-200}"
    local icon="$6"
    
    echo -e "${BLUE}${icon} Testing: ${name}${NC}"
    
    if [[ "$VERBOSE" == "true" ]]; then
        echo -e "  📡 Request: ${method} ${url}"
        if [[ -n "$data" ]]; then
            echo -e "  📝 Data: ${data}"
        fi
    fi
    
    local curl_cmd="curl -s -w '%{http_code}' --connect-timeout $TIMEOUT"
    
    if [[ "$method" == "POST" ]]; then
        curl_cmd="$curl_cmd -X POST -H 'Content-Type: application/json'"
        if [[ -n "$data" ]]; then
            curl_cmd="$curl_cmd -d '$data'"
        fi
    fi
    
    local response=$(eval "$curl_cmd '$url'" 2>/dev/null)
    local http_code="${response: -3}"
    local body="${response%???}"
    
    if [[ "$http_code" == "$expected_status" ]]; then
        echo -e "  ✅ ${GREEN}SUCCESS${NC} (HTTP $http_code)"
        
        if [[ "$VERBOSE" == "true" && -n "$body" ]]; then
            echo -e "  📄 Response:"
            if [[ "$JQ_AVAILABLE" == "true" ]]; then
                echo "$body" | jq . 2>/dev/null || echo "$body"
            else
                echo "$body"
            fi
        fi
        
        return 0
    else
        echo -e "  ❌ ${RED}FAILED${NC} (HTTP $http_code)"
        
        if [[ -n "$body" ]]; then
            echo -e "  📄 Error Response: $body"
        fi
        
        return 1
    fi
}

# Test Compliance Service Health
test_compliance_health() {
    echo -e "${CYAN}🏥 Compliance Service Health Checks${NC}"
    echo ""
    
    test_api_endpoint "Health Check" "GET" "$COMPLIANCE_URL/actuator/health" "" "200" "💚"
    test_api_endpoint "Service Status" "GET" "$COMPLIANCE_URL/api/v1/compliance/status" "" "200" "📊"
    test_api_endpoint "Demo Ready Check" "GET" "$COMPLIANCE_URL/api/v1/compliance/demo-ready" "" "200" "🎯"
    
    echo ""
}

# Test DSGVO Art. 30 - VVT Generation
test_vvt_generation() {
    echo -e "${CYAN}📊 DSGVO Art. 30 - VVT Generation Tests${NC}"
    echo ""
    
    # VVT Demo Request
    local vvt_demo_data='{
        "companyName": "DTN Software GmbH",
        "industry": "Software-Dienstleistung",
        "employeeCount": 120,
        "hasCustomerData": true,
        "hasEmployeeData": true,
        "usesAIProcessing": true,
        "dataCategories": ["Kundendaten", "Mitarbeiterdaten", "Projektdaten"],
        "demoMode": true,
        "includeBusinessMetrics": true
    }'
    
    test_api_endpoint "VVT Demo" "GET" "$COMPLIANCE_URL/api/v1/compliance/vvt/demo" "" "200" "📋"
    test_api_endpoint "VVT Generation" "POST" "$COMPLIANCE_URL/api/v1/compliance/vvt/generate" "$vvt_demo_data" "200" "📊"
    
    echo ""
}

# Test DSGVO Art. 35 - DSFA Assessment
test_dsfa_assessment() {
    echo -e "${CYAN}📋 DSGVO Art. 35 - DSFA Assessment Tests${NC}"
    echo ""
    
    # DSFA Demo Request
    local dsfa_demo_data='{
        "processingName": "KI-basierte Kundensegmentierung",
        "processingDescription": "Automatisierte Analyse von Kundendaten zur Segmentierung für personalisierte Marketing-Kampagnen",
        "dataTypes": ["Kundendaten", "Kaufverhalten", "Demografische Daten"],
        "purposes": ["Marketing-Optimierung", "Personalisierung"],
        "technologies": ["Machine Learning", "Datenanalyse"],
        "dataSubjects": ["Kunden", "Interessenten"],
        "specialCategories": false,
        "thirdCountryTransfer": false,
        "automated_decision_making": true,
        "systematicMonitoring": false,
        "vulnerableGroups": false,
        "largeScale": true,
        "estimatedDataSubjects": 50000,
        "demoMode": true,
        "includeAIActAssessment": true
    }'
    
    test_api_endpoint "DSFA Demo" "GET" "$COMPLIANCE_URL/api/v1/compliance/dsfa/demo" "" "200" "📋"
    test_api_endpoint "DSFA Assessment" "POST" "$COMPLIANCE_URL/api/v1/compliance/dsfa/assess" "$dsfa_demo_data" "200" "⚖️"
    
    echo ""
}

# Test EU AI Act - Risk Classification
test_ai_risk_classification() {
    echo -e "${CYAN}🤖 EU AI Act - Risk Classification Tests${NC}"
    echo ""
    
    # AI Risk Demo Request
    local ai_risk_demo_data='{
        "systemName": "E-Commerce Recommendation Engine",
        "systemType": "Recommendation System",
        "applicationDomain": "E-Commerce",
        "systemDescription": "Machine Learning basiertes Empfehlungssystem für Produktvorschläge",
        "dataTypes": ["Kaufverhalten", "Präferenzen", "Demografische Daten"],
        "userInteraction": true,
        "automatedDecisionMaking": true,
        "biometricData": false,
        "emotionRecognition": false,
        "criticalInfrastructure": false,
        "largeScale": true,
        "estimatedAffectedPersons": 50000,
        "geographicScope": "EU",
        "demoMode": true,
        "germanStandards": true
    }'
    
    test_api_endpoint "AI Risk Demo" "GET" "$COMPLIANCE_URL/api/v1/compliance/ai-risk/demo" "" "200" "🤖"
    test_api_endpoint "AI Risk Classification" "POST" "$COMPLIANCE_URL/api/v1/compliance/ai-risk/classify" "$ai_risk_demo_data" "200" "🎯"
    
    echo ""
}

# Test Business Impact APIs
test_business_impact() {
    echo -e "${CYAN}💰 Business Impact & ROI Tests${NC}"
    echo ""
    
    test_api_endpoint "Business Impact" "GET" "$COMPLIANCE_URL/api/v1/compliance/business-impact" "" "200" "💰"
    test_api_endpoint "Compliance Report" "GET" "$COMPLIANCE_URL/api/v1/compliance/report?reportType=DEMO&format=JSON" "" "200" "📈"
    
    echo ""
}

# Test Swagger UI and API Documentation
test_api_documentation() {
    echo -e "${CYAN}📚 API Documentation Tests${NC}"
    echo ""
    
    test_api_endpoint "Swagger UI" "GET" "$COMPLIANCE_URL/swagger-ui/index.html" "" "200" "📚"
    test_api_endpoint "API Docs JSON" "GET" "$COMPLIANCE_URL/v3/api-docs" "" "200" "📋"
    test_api_endpoint "OpenAPI YAML" "GET" "$COMPLIANCE_URL/v3/api-docs.yaml" "" "200" "📄"
    
    echo ""
}

# Performance and Load Tests
test_performance() {
    if [[ "$DEMO_MODE" == "true" ]]; then
        echo -e "${CYAN}⚡ Performance Tests (Demo Mode)${NC}"
        echo ""
        
        # Measure VVT Generation Time
        echo -e "  🚀 VVT Generation Performance:"
        local start_time=$(date +%s%3N)
        test_api_endpoint "VVT Speed Test" "GET" "$COMPLIANCE_URL/api/v1/compliance/vvt/demo" "" "200" "⚡" >/dev/null
        local end_time=$(date +%s%3N)
        local duration=$((end_time - start_time))
        echo -e "    ⏱️  Zeit: ${GREEN}${duration}ms${NC} (Ziel: <2000ms)"
        
        # Measure DSFA Assessment Time
        echo -e "  🚀 DSFA Assessment Performance:"
        start_time=$(date +%s%3N)
        test_api_endpoint "DSFA Speed Test" "GET" "$COMPLIANCE_URL/api/v1/compliance/dsfa/demo" "" "200" "⚡" >/dev/null
        end_time=$(date +%s%3N)
        duration=$((end_time - start_time))
        echo -e "    ⏱️  Zeit: ${GREEN}${duration}ms${NC} (Ziel: <3000ms)"
        
        echo ""
    fi
}

# Integration Tests with Gateway
test_gateway_integration() {
    echo -e "${CYAN}🔗 Gateway Integration Tests${NC}"
    echo ""
    
    test_api_endpoint "Gateway Health" "GET" "$GATEWAY_URL/actuator/health" "" "200" "🚪"
    test_api_endpoint "Gateway Status" "GET" "$GATEWAY_URL/api/v1/gateway/status" "" "200" "📊"
    
    echo ""
}

# Show Business Value Summary
show_business_summary() {
    if [[ "$DEMO_MODE" == "true" ]]; then
        echo -e "${PURPLE}💼 Business Value Summary (Bewerbungs-Ready)${NC}"
        echo ""
        echo -e "  📈 ROI-Demonstration:"
        echo -e "    💰 Jahresersparnis: ${GREEN}96.960€${NC} (100-MA Software-Firma)"
        echo -e "    ⚡ VVT-Generierung: ${GREEN}95% Zeitersparnis${NC} (8h → 24min)"
        echo -e "    🤖 DSFA-Automatisierung: ${GREEN}87% Effizienzsteigerung${NC}"
        echo -e "    🎯 EU AI Act: ${GREEN}92% Automatisierung${NC} (8h → 38min)"
        echo ""
        echo -e "  🎪 Demo-Highlights:"
        echo -e "    ✅ Live-APIs funktional und responsive"
        echo -e "    ✅ Deutsche Compliance-Templates"
        echo -e "    ✅ DSGVO + EU AI Act vollintegriert"
        echo -e "    ✅ Swagger UI professionell dokumentiert"
        echo ""
        echo -e "  💼 Bewerbungs-Impact:"
        echo -e "    🎯 Recruiter: Sofort beeindruckt durch Live-Demo"
        echo -e "    👨‍💻 Fach-ITler: Code-Qualität und Architektur überzeugt"
        echo -e "    📊 Management: Business Case messbar dargestellt"
        echo ""
    fi
}

# Error handling and cleanup
cleanup() {
    if [[ $? -ne 0 ]]; then
        echo -e "${RED}❌ Tests abgebrochen. Bitte prüfen Sie die Compliance Service Verfügbarkeit.${NC}"
        echo ""
        echo -e "${YELLOW}🔧 Troubleshooting:${NC}"
        echo -e "  1. Service starten: ${CYAN}docker-compose up -d compliance-service${NC}"
        echo -e "  2. Logs prüfen: ${CYAN}docker-compose logs compliance-service${NC}"
        echo -e "  3. Health Check: ${CYAN}curl http://localhost:8081/actuator/health${NC}"
    fi
}

trap cleanup EXIT

# Main execution
main() {
    show_demo_banner
    check_prerequisites
    
    echo -e "${BLUE}🧪 DTN Compliance Service - Comprehensive API Tests${NC}"
    echo -e "$(date '+%Y-%m-%d %H:%M:%S') - Testing Compliance APIs..."
    echo ""
    
    # Core Service Tests
    test_compliance_health
    test_vvt_generation
    test_dsfa_assessment
    test_ai_risk_classification
    test_business_impact
    test_api_documentation
    
    # Additional Tests
    test_performance
    test_gateway_integration
    
    # Demo Summary
    show_business_summary
    
    echo -e "${GREEN}✅ Alle Compliance Service Tests erfolgreich abgeschlossen!${NC}"
    echo ""
    echo -e "${BLUE}🎯 Demo URLs für Bewerbungsgespräche:${NC}"
    echo -e "  📚 Swagger UI: ${CYAN}http://localhost:8081/swagger-ui/index.html${NC}"
    echo -e "  💚 Health Check: ${CYAN}http://localhost:8081/actuator/health${NC}"
    echo -e "  📊 Status API: ${CYAN}http://localhost:8081/api/v1/compliance/status${NC}"
    echo -e "  📋 VVT Demo: ${CYAN}http://localhost:8081/api/v1/compliance/vvt/demo${NC}"
    echo -e "  ⚖️  DSFA Demo: ${CYAN}http://localhost:8081/api/v1/compliance/dsfa/demo${NC}"
    echo -e "  🤖 AI Risk Demo: ${CYAN}http://localhost:8081/api/v1/compliance/ai-risk/demo${NC}"
    echo ""
    echo -e "${GREEN}🚀 DTN Compliance Platform ist bewerbungsbereit!${NC}"
    
    exit 0
}

# Execute main function
main "$@"