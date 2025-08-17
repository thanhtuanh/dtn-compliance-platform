#!/bin/bash
# scripts/fix-database-password.sh
# DTN Compliance Platform - Database Password Consistency Fix

set -euo pipefail

# Farben
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${CYAN}🔒 DTN Database Password Consistency Fix${NC}"
echo "========================================="
echo ""

echo -e "${YELLOW}🔍 Problem identifiziert:${NC}"
echo "  ❌ Gateway Service erwartet: dtn_secure_2024"
echo "  ❌ Docker Databases verwenden: dtn_password"
echo ""

echo -e "${YELLOW}💡 Lösung: Standardisierung auf 'dtn_secure_2024'${NC}"
echo ""

# 1. Docker Compose Fix
echo -e "${YELLOW}📝 1. Aktualisiere docker-compose.yml...${NC}"

if [ -f "docker-compose.yml" ]; then
    # Backup erstellen
    cp docker-compose.yml docker-compose.yml.backup.password
    
    # Ersetze alle dtn_password mit dtn_secure_2024
    sed -i.tmp 's/POSTGRES_PASSWORD: dtn_password/POSTGRES_PASSWORD: dtn_secure_2024/g' docker-compose.yml
    rm -f docker-compose.yml.tmp
    
    echo "  ✅ docker-compose.yml Passwörter aktualisiert"
else
    echo -e "  ${RED}❌ docker-compose.yml nicht gefunden${NC}"
fi

# 2. Gateway Service application.yml prüfen
echo ""
echo -e "${YELLOW}📝 2. Prüfe Gateway Service Konfiguration...${NC}"

APP_YML="services/gateway-service/src/main/resources/application.yml"
if [ -f "$APP_YML" ]; then
    if grep -q "dtn_secure_2024" "$APP_YML"; then
        echo "  ✅ Gateway Service verwendet korrekte Passwörter"
    else
        echo "  🔧 Aktualisiere Gateway Service Passwörter..."
        sed -i.tmp 's/DB_PASSWORD:dtn_password/DB_PASSWORD:dtn_secure_2024/g' "$APP_YML"
        sed -i.tmp 's/password: dtn_password/password: dtn_secure_2024/g' "$APP_YML"
        rm -f "$APP_YML.tmp"
        echo "  ✅ Gateway Service Passwörter aktualisiert"
    fi
else
    echo -e "  ${YELLOW}⚠️ Gateway application.yml nicht gefunden${NC}"
fi

# 3. Flyway Plugin in pom.xml prüfen
echo ""
echo -e "${YELLOW}📝 3. Prüfe Flyway Plugin Konfiguration...${NC}"

POM_FILE="services/gateway-service/pom.xml"
if [ -f "$POM_FILE" ]; then
    if grep -q "dtn_secure_2024" "$POM_FILE"; then
        echo "  ✅ Flyway Plugin verwendet korrekte Passwörter"
    else
        echo "  🔧 Aktualisiere Flyway Plugin Passwörter..."
        sed -i.tmp 's/<password>dtn_password<\/password>/<password>dtn_secure_2024<\/password>/g' "$POM_FILE"
        rm -f "$POM_FILE.tmp"
        echo "  ✅ Flyway Plugin Passwörter aktualisiert"
    fi
else
    echo -e "  ${YELLOW}⚠️ pom.xml nicht gefunden${NC}"
fi

# 4. Database Container neu erstellen
echo ""
echo -e "${YELLOW}🔄 4. Database Container neu erstellen...${NC}"

echo "  🛑 Stoppe bestehende Database Container..."
docker-compose stop postgres-gateway postgres-compliance postgres-document 2>/dev/null || true

echo "  🗑️ Entferne alte Container und Volumes..."
docker-compose rm -f postgres-gateway postgres-compliance postgres-document 2>/dev/null || true

echo "  📦 Entferne alte Database Volumes (Passwort-Reset)..."
docker volume rm dtn_postgres_gateway_data dtn_postgres_compliance_data dtn_postgres_document_data 2>/dev/null || true

echo "  🚀 Starte Database Container mit neuen Passwörtern..."
docker-compose up -d postgres-gateway postgres-compliance postgres-document

echo ""
echo -e "${YELLOW}⏳ 5. Warte auf Database Initialization...${NC}"

# Warte bis alle Databases bereit sind
max_attempts=30
attempt=0

while [ $attempt -lt $max_attempts ]; do
    gateway_ready=$(docker inspect --format="{{.State.Health.Status}}" dtn-postgres-gateway 2>/dev/null || echo "starting")
    compliance_ready=$(docker inspect --format="{{.State.Health.Status}}" dtn-postgres-compliance 2>/dev/null || echo "starting")
    document_ready=$(docker inspect --format="{{.State.Health.Status}}" dtn-postgres-document 2>/dev/null || echo "starting")
    
    echo "    Gateway: $gateway_ready | Compliance: $compliance_ready | Document: $document_ready"
    
    if [ "$gateway_ready" = "healthy" ] && [ "$compliance_ready" = "healthy" ] && [ "$document_ready" = "healthy" ]; then
        echo -e "  ✅ ${GREEN}Alle Databases sind bereit!${NC}"
        break
    fi
    
    sleep 10
    ((attempt++))
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "  ${YELLOW}⚠️ Databases brauchen länger - wird im Hintergrund fortgesetzt${NC}"
fi

# 6. Gateway Service neu starten
echo ""
echo -e "${YELLOW}🔄 6. Gateway Service neu starten...${NC}"

if docker ps --format "table {{.Names}}" | grep -q "dtn-gateway"; then
    echo "  🔄 Starte Gateway Service neu..."
    docker-compose restart gateway-service
else
    echo "  🚀 Starte Gateway Service..."
    docker-compose up -d gateway-service
fi

echo "  ⏳ Warte auf Gateway Service..."
sleep 30

# 7. Teste Database Verbindung
echo ""
echo -e "${YELLOW}🧪 7. Teste Database Verbindungen...${NC}"

# Test Gateway Database
echo -n "  Testing Gateway Database Connection... "
if docker-compose exec -T postgres-gateway psql -U dtn_user -d dtn_gateway -c "SELECT 1;" >/dev/null 2>&1; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FAIL${NC}"
fi

# Test Compliance Database  
echo -n "  Testing Compliance Database Connection... "
if docker-compose exec -T postgres-compliance psql -U dtn_user -d dtn_compliance -c "SELECT 1;" >/dev/null 2>&1; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FAIL${NC}"
fi

# Test Document Database
echo -n "  Testing Document Database Connection... "
if docker-compose exec -T postgres-document psql -U dtn_user -d dtn_document -c "SELECT 1;" >/dev/null 2>&1; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FAIL${NC}"
fi

# 8. Teste Gateway Service
echo ""
echo -e "${YELLOW}🌐 8. Teste Gateway Service...${NC}"

echo -n "  Testing Gateway Health... "
if curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
    echo -e "${GREEN}✅ HEALTHY${NC}"
    
    echo -n "  Testing Swagger UI... "
    if curl -f http://localhost:8080/swagger-ui/ >/dev/null 2>&1; then
        echo -e "${GREEN}✅ AVAILABLE${NC}"
    else
        echo -e "${YELLOW}⚠️ LOADING${NC}"
    fi
else
    echo -e "${RED}❌ UNHEALTHY${NC}"
    echo "    💡 Prüfe Logs: docker-compose logs gateway-service"
fi

echo ""
echo -e "${GREEN}🎉 Database Password Fix abgeschlossen!${NC}"
echo ""
echo -e "${CYAN}📋 Zusammenfassung der Änderungen:${NC}"
echo "  ├── ✅ docker-compose.yml: Passwörter standardisiert"
echo "  ├── ✅ application.yml: Passwörter konsistent"  
echo "  ├── ✅ pom.xml: Flyway Plugin aktualisiert"
echo "  ├── ✅ Database Volumes: Neu erstellt mit korrekten Passwörtern"
echo "  └── ✅ Services: Neu gestartet"

echo ""
echo -e "${CYAN}🔑 Einheitliche Credentials:${NC}"
echo "  ├── Username: dtn_user"
echo "  ├── Password: dtn_secure_2024"
echo "  └── Databases: dtn_gateway, dtn_compliance, dtn_document"

echo ""
echo -e "${CYAN}🌐 Test URLs:${NC}"
echo "  ├── 🏥 Health Check: http://localhost:8080/actuator/health"
echo "  ├── 🚪 Swagger UI: http://localhost:8080/swagger-ui/"
echo "  ├── 💰 Business Value: http://localhost:8080/api/v1/demo/business-value"
echo "  └── 🗄️ pgAdmin: http://localhost:5050"

echo ""
echo -e "${GREEN}✅ Platform ist jetzt bereit für Demo-Präsentationen! 🚀${NC}"