#!/bin/bash

# DTN Compliance Platform - Quick Demo Check
# Schneller Health Check für Bewerbungsgespräche

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${BLUE}🚀 DTN Compliance Platform - Quick Demo Check${NC}"
echo "$(date '+%H:%M:%S') - Checking demo readiness..."
echo ""

# Gateway Health Check
echo -n "Gateway Service: "
if curl -s http://localhost:8080/actuator/health | grep -q '"status":"UP"'; then
    echo -e "${GREEN}✅ UP${NC}"
    GATEWAY_OK=true
else
    echo -e "${RED}❌ DOWN${NC}"
    GATEWAY_OK=false
fi

# Swagger UI Check
echo -n "Swagger UI: "
if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/swagger-ui/ | grep -q "200"; then
    echo -e "${GREEN}✅ AVAILABLE${NC}"
    SWAGGER_OK=true
else
    echo -e "${RED}❌ NOT AVAILABLE${NC}"
    SWAGGER_OK=false
fi

# Database Check
echo -n "Gateway Database: "
if pg_isready -h localhost -p 5432 -U dtn_user -t 5 &>/dev/null; then
    echo -e "${GREEN}✅ CONNECTED${NC}"
    DB_OK=true
else
    echo -e "${RED}❌ NO CONNECTION${NC}"
    DB_OK=false
fi

echo ""

# Demo Readiness Summary
if [[ "$GATEWAY_OK" == "true" && "$SWAGGER_OK" == "true" ]]; then
    echo -e "${GREEN}🎉 DEMO READY!${NC}"
    echo ""
    echo -e "${BLUE}📊 Demo URLs für Bewerbungsgespräche:${NC}"
    echo -e "  Swagger UI: ${YELLOW}http://localhost:8080/swagger-ui/${NC}"
    echo -e "  Gateway Status: ${YELLOW}http://localhost:8080/api/v1/gateway/status${NC}"
    echo -e "  Developer Info: ${YELLOW}http://localhost:8080/api/v1/gateway/developer-info${NC}"
    echo ""
    echo -e "${GREEN}✨ Bereit für Step 3: Compliance Service!${NC}"
    echo -e "   💡 Sagen Sie 'OK Step 3' für DSGVO + EU AI Act Features"
    exit 0
else
    echo -e "${RED}❌ DEMO NOT READY${NC}"
    echo ""
    echo -e "${YELLOW}🔧 Quick Fix:${NC}"
    if [[ "$DB_OK" == "false" ]]; then
        echo "  1. Database starten: docker-compose up -d postgres-gateway"
    fi
    if [[ "$GATEWAY_OK" == "false" ]]; then
        echo "  2. Gateway starten: mvn spring-boot:run"
    fi
    echo "  3. 30 Sekunden warten und erneut prüfen"
    exit 1
fi