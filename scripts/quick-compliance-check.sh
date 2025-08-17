#!/bin/bash

# DTN Compliance Service - Quick Health Check
# Speziell für Compliance Service Validierung

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${BLUE}🚀 DTN Compliance Service - Quick Check${NC}"
echo "$(date '+%H:%M:%S') - Checking compliance service readiness..."
echo ""

# Service URLs
COMPLIANCE_URL="http://localhost:8081"
GATEWAY_URL="http://localhost:8080"

# Compliance Service Health
echo -n "Compliance Service: "
if curl -s "$COMPLIANCE_URL/actuator/health" | grep -q '"status":"UP"'; then
    echo -e "${GREEN}✅ UP${NC}"
    COMPLIANCE_OK=true
else
    echo -e "${RED}❌ DOWN${NC}"
    COMPLIANCE_OK=false
fi

# Compliance Database
echo -n "Compliance Database: "
if curl -s "$COMPLIANCE_URL/actuator/health" | grep -q '"db":{"status":"UP"'; then
    echo -e "${GREEN}✅ CONNECTED${NC}"
    DB_OK=true
else
    echo -e "${RED}❌ DISCONNECTED${NC}"
    DB_OK=false
fi

# Swagger UI Check
echo -n "Compliance Swagger UI: "
if curl -s -o /dev/null -w "%{http_code}" "$COMPLIANCE_URL/swagger-ui/index.html" | grep -q "200"; then
    echo -e "${GREEN}✅ AVAILABLE${NC}"
    SWAGGER_OK=true
else
    echo -e "${RED}❌ NOT AVAILABLE${NC}"
    SWAGGER_OK=false
fi

# DSGVO VVT API Check
echo -n "DSGVO VVT API: "
if curl -s -o /dev/null -w "%{http_code}" "$COMPLIANCE_URL/api/v1/compliance/vvt/demo" | grep -q "200"; then
    echo -e "${GREEN}✅ WORKING${NC}"
    VVT_OK=true
else
    echo -e "${RED}❌ NOT WORKING${NC}"
    VVT_OK=false
fi

# EU AI Act API Check  
echo -n "EU AI Act API: "
if curl -s -o /dev/null -w "%{http_code}" "$COMPLIANCE_URL/api/v1/compliance/ai-risk/demo" | grep -q "200"; then
    echo -e "${GREEN}✅ WORKING${NC}"
    AI_OK=true
else
    echo -e "${RED}❌ NOT WORKING${NC}"
    AI_OK=false
fi

# DSFA API Check
echo -n "DSFA Assessment API: "
if curl -s -o /dev/null -w "%{http_code}" "$COMPLIANCE_URL/api/v1/compliance/dsfa/demo" | grep -q "200"; then
    echo -e "${GREEN}✅ WORKING${NC}"
    DSFA_OK=true
else
    echo -e "${RED}❌ NOT WORKING${NC}"
    DSFA_OK=false
fi

echo ""

# Demo Readiness Summary
if [[ "$COMPLIANCE_OK" == "true" && "$DB_OK" == "true" && "$SWAGGER_OK" == "true" && 
      "$VVT_OK" == "true" && "$AI_OK" == "true" && "$DSFA_OK" == "true" ]]; then
    echo -e "${GREEN}🎉 COMPLIANCE SERVICE DEMO-READY!${NC}"
    echo ""
    echo -e "${BLUE}📊 Business Value URLs:${NC}"
    echo -e "  📚 Swagger UI: ${YELLOW}http://localhost:8081/swagger-ui/index.html${NC}"
    echo -e "  💚 Health Check: ${YELLOW}http://localhost:8081/actuator/health${NC}"
    echo -e "  📋 VVT Demo: ${YELLOW}http://localhost:8081/api/v1/compliance/vvt/demo${NC}"
    echo -e "  ⚖️  DSFA Demo: ${YELLOW}http://localhost:8081/api/v1/compliance/dsfa/demo${NC}"
    echo -e "  🤖 AI Risk Demo: ${YELLOW}http://localhost:8081/api/v1/compliance/ai-risk/demo${NC}"
    echo -e "  💰 Business Impact: ${YELLOW}http://localhost:8081/api/v1/compliance/business-impact${NC}"
    echo ""
    echo -e "${GREEN}✨ Bereit für Step 4: Document Service!${NC}"
    echo -e "   💡 Sagen Sie 'OK Step 4' für PDF-Export Features"
    exit 0
else
    echo -e "${RED}❌ COMPLIANCE SERVICE NOT READY${NC}"
    echo ""
    echo -e "${YELLOW}🔧 Quick Fix:${NC}"
    
    if [[ "$COMPLIANCE_OK" == "false" ]]; then
        echo "  1. Compliance Service starten:"
        echo "     cd services/compliance-service && mvn spring-boot:run"
        echo "     oder: docker-compose up -d compliance-service"
    fi
    
    if [[ "$DB_OK" == "false" ]]; then
        echo "  2. Database starten: docker-compose up -d postgres-compliance"
    fi
    
    if [[ "$SWAGGER_OK" == "false" ]]; then
        echo "  3. SwaggerConfig prüfen und WebConfig hinzufügen"
    fi
    
    echo "  4. 60 Sekunden warten und erneut prüfen"
    echo ""
    echo -e "${BLUE}💡 Troubleshooting:${NC}"
    echo "  - Logs: docker-compose logs compliance-service"
    echo "  - Build: cd services/compliance-service && mvn clean install"
    echo "  - Health: curl -v http://localhost:8081/actuator/health"
    
    exit 1
fi