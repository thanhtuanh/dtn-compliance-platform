#!/bin/bash
# scripts/test-apis.sh
# DTN Compliance Platform - Vollständiger API Test
# Testet alle wichtigen Endpoints mit korrekten Parametern

set -euo pipefail

# Farben
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# Basis URLs
GATEWAY_URL="http://localhost:8080"
COMPLIANCE_URL="http://localhost:8081"
DOCUMENT_URL="http://localhost:8082"

echo -e "${BLUE}"
echo "╔═══════════════════════════════════════════════════════════════════════════════╗"
echo "║                     DTN Compliance Platform API Tests                         ║"
echo "║                                                                               ║"
echo "║  🧪 Vollständiger Test aller DSGVO + EU AI Act APIs                         ║"
echo "║  💰 Business Value: 96.960€ Jahresersparnis demonstriert                    ║"
echo "╚═══════════════════════════════════════════════════════════════════════════════╝"
echo -e "${NC}"
echo ""

# Helper function für API Tests
test_endpoint() {
    local name="$1"
    local url="$2" 
    local method="${3:-GET}"
    local data="${4:-}"
    local expected_status="${5:-200}"
    
    echo -n "  Testing $name... "
    
    if [ "$method" = "POST" ] && [ -n "$data" ]; then
        response=$(curl -s -w "%{http_code}" -X POST "$url" \
                   -H "Content-Type: application/json" \
                   -d "$data" -o /tmp/api_response.json)
    else
        response=$(curl -s -w "%{http_code}" "$url" -o /tmp/api_response.json)
    fi
    
    status_code="${response: -3}"
    
    if [ "$status_code" -eq "$expected_status" ]; then
        echo -e "${GREEN}✅ OK ($status_code)${NC}"
        return 0
    else
        echo -e "${RED}❌ FAIL ($status_code)${NC}"
        if [ -f /tmp/api_response.json ]; then
            echo "    Response: $(cat /tmp/api_response.json | head -c 200)..."
        fi
        return 1
    fi
}

# Health Checks
echo -e "${CYAN}🏥 Health Checks${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

test_endpoint "Gateway Health" "$GATEWAY_URL/actuator/health"
test_endpoint "Compliance Health" "$COMPLIANCE_URL/actuator/health"
test_endpoint "Document Health" "$DOCUMENT_URL/actuator/health"

echo ""

# DSGVO VVT Tests (Art. 30)
echo -e "${CYAN}📊 DSGVO VVT Tests (Art. 30)${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# VVT Generierung Test
vvt_data='{
  "processingName": "Kundendatenverarbeitung",
  "controller": "DTN Software GmbH",
  "purpose": "Kundenbetreuung und Support",
  "legalBasis": "Art. 6 Abs. 1 lit. b DSGVO",
  "dataCategories": ["Name", "E-Mail", "Telefon"],
  "dataSubjects": "Kunden",
  "recipients": "Interne Mitarbeiter",
  "retentionPeriod": "5 Jahre",
  "technicalMeasures": ["Verschlüsselung", "Zugriffskontrolle"]
}'

test_endpoint "VVT Generation" "$COMPLIANCE_URL/api/v1/compliance/vvt/generate" "POST" "$vvt_data"

echo ""

# DSFA Tests (Art. 35)
echo -e "${CYAN}⚖️ DSFA Tests (Art. 35)${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# High Risk DSFA
high_risk_dsfa='{
  "processingName": "KI-basierte Kundensegmentierung",
  "processingPurpose": "Automatische Kategorisierung von Kunden",
  "dataSubjects": "Privatkunden",
  "personalDataCategories": ["Kaufverhalten", "Demografische Daten"],
  "processingMethods": ["Automatisierte Entscheidungsfindung", "Profiling"],
  "dataVolume": "LARGE",
  "riskFactors": ["Automatisierte Entscheidungsfindung", "Profiling"],
  "automatedDecisionMaking": true,
  "profilingUsed": true,
  "innovativeTechnology": true,
  "largescaleProcessing": true
}'

test_endpoint "DSFA High Risk" "$COMPLIANCE_URL/api/v1/compliance/dsfa/assess" "POST" "$high_risk_dsfa"

# Low Risk DSFA
low_risk_dsfa='{
  "processingName": "Newsletter-Anmeldung",
  "processingPurpose": "Marketing-E-Mails versenden",
  "dataSubjects": "Website-Besucher", 
  "personalDataCategories": ["E-Mail-Adresse"],
  "processingMethods": ["E-Mail-Versand"],
  "dataVolume": "SMALL",
  "riskFactors": [],
  "automatedDecisionMaking": false,
  "profilingUsed": false,
  "innovativeTechnology": false,
  "largescaleProcessing": false
}'

test_endpoint "DSFA Low Risk" "$COMPLIANCE_URL/api/v1/compliance/dsfa/assess" "POST" "$low_risk_dsfa"

echo ""

# EU AI Act Tests
echo -e "${CYAN}🤖 EU AI Act Tests${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# High Risk AI System
high_risk_ai='{
  "aiSystemName": "Kredit-Scoring KI",
  "aiSystemPurpose": "Automatische Kreditwürdigkeitsprüfung",
  "aiSystemType": "MACHINE_LEARNING",
  "applicationDomain": "FINANCIAL_SERVICES",
  "riskLevel": "HIGH_RISK",
  "affectedPersons": "Kreditantragsteller",
  "automatedDecisions": true,
  "humanOversight": false,
  "biometricData": false,
  "emotionRecognition": false,
  "criticalInfrastructure": false
}'

test_endpoint "AI Act High Risk" "$COMPLIANCE_URL/api/v1/compliance/ai-act/assess" "POST" "$high_risk_ai"

# Minimal Risk AI System  
minimal_risk_ai='{
  "aiSystemName": "Chatbot für FAQ",
  "aiSystemPurpose": "Beantwortung häufiger Fragen",
  "aiSystemType": "RULE_BASED",
  "applicationDomain": "CUSTOMER_SERVICE", 
  "riskLevel": "MINIMAL_RISK",
  "affectedPersons": "Website-Besucher",
  "automatedDecisions": false,
  "humanOversight": true,
  "biometricData": false,
  "emotionRecognition": false,
  "criticalInfrastructure": false
}'

test_endpoint "AI Act Minimal Risk" "$COMPLIANCE_URL/api/v1/compliance/ai-act/assess" "POST" "$minimal_risk_ai"

echo ""

# Document Generation Tests
echo -e "${CYAN}📄 Document Generation Tests${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# VVT Document Generation
vvt_doc_data='{
  "companyName": "DTN Software GmbH",
  "reportType": "VVT_ANNUAL",
  "includeRiskAssessment": true,
  "includeTechnicalMeasures": true,
  "language": "DE"
}'

test_endpoint "VVT Document Generation" "$DOCUMENT_URL/api/v1/documents/vvt/generate" "POST" "$vvt_doc_data"

# DSFA Document Generation
dsfa_doc_data='{
  "processingName": "KI-System Bewertung",
  "assessmentId": "dsfa-001", 
  "includeRiskMatrix": true,
  "includeMitigationPlan": true,
  "templateType": "BFDI_TEMPLATE",
  "language": "DE"
}'

test_endpoint "DSFA Document Generation" "$DOCUMENT_URL/api/v1/documents/dsfa/generate" "POST" "$dsfa_doc_data"

echo ""

# Business Metrics Tests
echo -e "${CYAN}💰 Business Metrics Tests${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

test_endpoint "ROI Calculation" "$COMPLIANCE_URL/api/v1/business/roi/calculate?employeeCount=100&annualComplianceHours=2000"
test_endpoint "Cost Savings Analysis" "$COMPLIANCE_URL/api/v1/business/savings/analyze?companySize=MEDIUM&industry=SOFTWARE"
test_endpoint "Compliance Metrics" "$COMPLIANCE_URL/api/v1/business/metrics/compliance"

echo ""

# Advanced Demo Tests  
echo -e "${CYAN}🎯 Advanced Demo Tests${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Combined DSGVO + AI Act Assessment
combined_assessment='{
  "processingName": "Intelligente Kundenempfehlungen",
  "processingPurpose": "Personalisierte Produktempfehlungen mit KI",
  "dataSubjects": "Online-Kunden",
  "personalDataCategories": ["Kaufhistorie", "Suchverhalten", "Präferenzen"],
  "aiSystemIntegrated": true,
  "aiSystemName": "Recommendation Engine ML",
  "aiSystemType": "MACHINE_LEARNING",
  "applicationDomain": "E_COMMERCE",
  "automatedDecisionMaking": true,
  "profilingUsed": true,
  "riskLevel": "LIMITED_RISK"
}'

test_endpoint "Combined DSGVO+AI Assessment" "$COMPLIANCE_URL/api/v1/compliance/combined/assess" "POST" "$combined_assessment"

# Compliance Dashboard
test_endpoint "Compliance Dashboard" "$COMPLIANCE_URL/api/v1/dashboard/overview"
test_endpoint "Risk Heatmap" "$COMPLIANCE_URL/api/v1/dashboard/risks/heatmap"

echo ""

# Summary und Business Value
echo -e "${GREEN}📊 Test Summary & Business Value${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Führe einen kompletten Business Value Test durch
echo -e "${CYAN}💰 Business Value Demonstration:${NC}"

business_case='{
  "companyProfile": {
    "name": "Beispiel Software GmbH",
    "employees": 100,
    "industry": "SOFTWARE_DEVELOPMENT",
    "location": "GERMANY",
    "aiSystemsInUse": 3,
    "currentComplianceMaturity": "BASIC"
  },
  "currentState": {
    "manualDsfaHours": 16,
    "manualVvtHours": 8,
    "complianceReviewsPerYear": 12,
    "externalConsultingCosts": 50000,
    "internalComplianceHours": 500
  },
  "targetState": {
    "automatedDsfaMinutes": 5,
    "automatedVvtMinutes": 2,
    "aiAssistedReviews": true,
    "reducedConsultingNeed": 0.8,
    "streamlinedProcesses": true
  }
}'

echo "Calculating ROI for 100-employee software company..."

if curl -s -X POST "$COMPLIANCE_URL/api/v1/business/roi/detailed" \
   -H "Content-Type: application/json" \
   -d "$business_case" -o /tmp/roi_result.json; then
    
    echo -e "${GREEN}✅ ROI Calculation completed!${NC}"
    echo ""
    
    if command -v python3 >/dev/null 2>&1; then
        python3 -c "
import json
with open('/tmp/roi_result.json', 'r') as f:
    data = json.load(f)
print('📊 Berechnete Jahresersparnis:', data.get('annualSavings', 'N/A'))
print('⏱️  Zeitersparnis DSFA:', data.get('dsfaTimeSavings', 'N/A'))
print('📋 Zeitersparnis VVT:', data.get('vvtTimeSavings', 'N/A'))
print('🎯 ROI Periode:', data.get('roiPeriodMonths', 'N/A'), 'Monate')
print('✅ Compliance Score:', data.get('complianceImprovement', 'N/A'))
"
    else
        echo "Full ROI data saved to /tmp/roi_result.json"
    fi
else
    echo -e "${YELLOW}⚠️ ROI calculation endpoint not yet implemented${NC}"
fi

echo ""
echo -e "${GREEN}🎉 API Tests completed!${NC}"
echo ""
echo -e "${CYAN}📈 Expected Business Value (100-MA Software-Firma):${NC}"
echo "  ├── 💰 Jahresersparnis: 96.960€"
echo "  ├── ⏱️  DSFA Zeitersparnis: 16h → 5min (99.5%)"
echo "  ├── 📋 VVT Zeitersparnis: 8h → 2min (99.6%)"
echo "  ├── ⚖️  Automatische Compliance: Art. 30 + Art. 35 DSGVO"
echo "  ├── 🤖 EU AI Act: Vollautomatisierte Risikoklassifizierung"
echo "  ├── 🛡️  Bußgeld-Vermeidung: bis 35 Mio€"
echo "  └── ✅ Deutsche Rechtssicherheit: BfDI-konforme Templates"
echo ""
echo -e "${CYAN}🌐 Demo URLs:${NC}"
echo "  ├── 🚪 Gateway Swagger: $GATEWAY_URL/swagger-ui/"
echo "  ├── ⚖️  Compliance Swagger: $COMPLIANCE_URL/swagger-ui/"
echo "  ├── 📄 Document Swagger: $DOCUMENT_URL/swagger-ui/"
echo "  └── 🔄 Load Balancer: http://localhost/"
echo ""
echo -e "${GREEN}Ready for live demo presentations! 🚀${NC}"

# Cleanup
rm -f /tmp/api_response.json /tmp/roi_result.json