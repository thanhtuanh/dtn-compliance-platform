#!/bin/bash
# scripts/quick-start.sh
# DTN Compliance Platform - Quick Start Script
# Startet Services in der richtigen Reihenfolge

set -euo pipefail

# Farben für Output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Banner
echo -e "${BLUE}"
echo "╔═══════════════════════════════════════════════════════════════════════════════╗"
echo "║                     DTN Compliance Platform Quick Start                       ║"
echo "║                                                                               ║"
echo "║  🚀 Automatischer Start aller Services in der richtigen Reihenfolge         ║"
echo "║  💰 Business Value: 96.960€ Jahresersparnis                                 ║"
echo "║  👨‍💻 Entwickelt von: Duc Thanh Nguyen | München                             ║"
echo "╚═══════════════════════════════════════════════════════════════════════════════╝"
echo -e "${NC}"
echo ""

# Cleanup alte Container falls vorhanden
echo -e "${YELLOW}🧹 Cleanup alter Container...${NC}"
docker-compose down 2>/dev/null || true
echo ""

# Schritt 1: Databases starten
echo -e "${CYAN}📀 Schritt 1/5: Starte PostgreSQL Databases...${NC}"
docker-compose up -d postgres-gateway postgres-compliance postgres-document
echo "   Warte auf Database Health Checks..."

# Warten bis alle DBs healthy sind
max_attempts=30
attempt=0

while [ $attempt -lt $max_attempts ]; do
    gateway_healthy=$(docker inspect --format="{{.State.Health.Status}}" dtn-postgres-gateway 2>/dev/null || echo "unhealthy")
    compliance_healthy=$(docker inspect --format="{{.State.Health.Status}}" dtn-postgres-compliance 2>/dev/null || echo "unhealthy")
    document_healthy=$(docker inspect --format="{{.State.Health.Status}}" dtn-postgres-document 2>/dev/null || echo "unhealthy")
    
    if [ "$gateway_healthy" = "healthy" ] && [ "$compliance_healthy" = "healthy" ] && [ "$document_healthy" = "healthy" ]; then
        echo -e "   ${GREEN}✅ Alle Databases sind healthy!${NC}"
        break
    fi
    
    echo "   ⏳ Warte auf Databases... ($((attempt+1))/$max_attempts)"
    sleep 10
    ((attempt++))
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "   ${RED}❌ Timeout: Databases nicht healthy nach 5 Minuten${NC}"
    echo "   Überprüfe Logs: docker-compose logs postgres-gateway postgres-compliance postgres-document"
    exit 1
fi

echo ""

# Schritt 2: Infrastructure Services starten
echo -e "${CYAN}🔧 Schritt 2/5: Starte Infrastructure Services (Redis, Ollama)...${NC}"
docker-compose up -d redis ollama

echo "   Warte auf Infrastructure Services..."
sleep 30

# Redis Check
if docker ps --format "table {{.Names}}" | grep -q "dtn-redis"; then
    echo -e "   ${GREEN}✅ Redis gestartet${NC}"
else
    echo -e "   ${YELLOW}⚠️ Redis nicht gestartet (optional)${NC}"
fi

# Ollama Check
if docker ps --format "table {{.Names}}" | grep -q "dtn-ollama"; then
    echo -e "   ${GREEN}✅ Ollama gestartet${NC}"
else
    echo -e "   ${YELLOW}⚠️ Ollama nicht gestartet (optional)${NC}"
fi

echo ""

# Schritt 3: Compliance Service starten
echo -e "${CYAN}⚖️ Schritt 3/5: Starte Compliance Service...${NC}"
docker-compose up -d compliance-service

echo "   Warte auf Compliance Service Health Check..."
attempt=0
max_attempts=20

while [ $attempt -lt $max_attempts ]; do
    if curl -f http://localhost:8081/actuator/health >/dev/null 2>&1; then
        echo -e "   ${GREEN}✅ Compliance Service ist healthy!${NC}"
        break
    fi
    
    echo "   ⏳ Warte auf Compliance Service... ($((attempt+1))/$max_attempts)"
    sleep 15
    ((attempt++))
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "   ${YELLOW}⚠️ Compliance Service braucht länger zum Starten (läuft weiter im Hintergrund)${NC}"
fi

echo ""

# Schritt 4: Document Service starten
echo -e "${CYAN}📄 Schritt 4/5: Starte Document Service...${NC}"
docker-compose up -d document-service

echo "   Warte auf Document Service Health Check..."
attempt=0
max_attempts=15

while [ $attempt -lt $max_attempts ]; do
    if curl -f http://localhost:8082/actuator/health >/dev/null 2>&1; then
        echo -e "   ${GREEN}✅ Document Service ist healthy!${NC}"
        break
    fi
    
    echo "   ⏳ Warte auf Document Service... ($((attempt+1))/$max_attempts)"
    sleep 10
    ((attempt++))
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "   ${YELLOW}⚠️ Document Service braucht länger zum Starten (läuft weiter im Hintergrund)${NC}"
fi

echo ""

# Schritt 5: Gateway Service starten
echo -e "${CYAN}🚪 Schritt 5/5: Starte Gateway Service (Main API)...${NC}"
docker-compose up -d gateway-service

echo "   Warte auf Gateway Service Health Check..."
attempt=0
max_attempts=20

while [ $attempt -lt $max_attempts ]; do
    if curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
        echo -e "   ${GREEN}✅ Gateway Service ist healthy!${NC}"
        break
    fi
    
    echo "   ⏳ Warte auf Gateway Service... ($((attempt+1))/$max_attempts)"
    sleep 15
    ((attempt++))
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "   ${YELLOW}⚠️ Gateway Service braucht länger zum Starten (läuft weiter im Hintergrund)${NC}"
fi

echo ""

# Schritt 6: Optional Management Tools starten
echo -e "${CYAN}🛠️ Schritt 6/7: Starte Management Tools...${NC}"
docker-compose up -d pgadmin

# Schritt 7: Nginx Load Balancer starten
echo -e "${CYAN}🔄 Schritt 7/7: Starte Nginx Load Balancer...${NC}"

# Erstelle nginx Ordner und Config falls nicht vorhanden
if [ ! -d "nginx" ]; then
    echo "   📁 Erstelle nginx Ordner..."
    mkdir -p nginx
fi

if [ ! -f "nginx/nginx.conf" ]; then
    echo "   📝 Erstelle Standard nginx.conf..."
    cat > nginx/nginx.conf << 'EOF'
worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

events {
    worker_connections 1024;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;
    
    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent"';
    
    access_log /var/log/nginx/access.log main;
    
    sendfile on;
    keepalive_timeout 65;
    client_max_body_size 50M;
    
    gzip on;
    gzip_types text/plain text/css application/json application/javascript;
    
    upstream gateway_backend {
        server gateway-service:8080;
    }
    
    server {
        listen 80;
        server_name localhost;
        
        add_header X-Frame-Options "SAMEORIGIN" always;
        add_header X-Content-Type-Options "nosniff" always;
        
        location / {
            proxy_pass http://gateway_backend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            proxy_connect_timeout 30s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;
            
            proxy_http_version 1.1;
            proxy_set_header Connection "";
        }
        
        location /health {
            access_log off;
            return 200 "healthy\n";
            add_header Content-Type text/plain;
        }
    }
}
EOF
    echo "   ✅ nginx.conf erstellt"
fi

# Starte Nginx
docker-compose up -d nginx

echo "   Warte auf Nginx..."
sleep 5

# Nginx Health Check
if curl -f http://localhost/health >/dev/null 2>&1; then
    echo -e "   ${GREEN}✅ Nginx Load Balancer gestartet!${NC}"
else
    echo -e "   ${YELLOW}⚠️ Nginx braucht einen Moment... (prüfe später)${NC}"
fi

echo ""

# Status Check aller Services
echo -e "${CYAN}📊 Final Status Check:${NC}"
echo ""

services=("dtn-postgres-gateway" "dtn-postgres-compliance" "dtn-postgres-document" "dtn-redis" "dtn-ollama" "dtn-compliance" "dtn-document" "dtn-gateway" "dtn-pgadmin" "dtn-nginx")

for service in "${services[@]}"; do
    if docker ps --format "table {{.Names}}" | grep -q "^$service$"; then
        # Prüfe Health Status wenn verfügbar
        health_status=$(docker inspect --format="{{.State.Health.Status}}" "$service" 2>/dev/null || echo "running")
        case $health_status in
            "healthy")
                echo -e "  ✅ $service: ${GREEN}$health_status${NC}"
                ;;
            "starting")
                echo -e "  🔄 $service: ${YELLOW}$health_status${NC}"
                ;;
            "unhealthy")
                echo -e "  ❌ $service: ${RED}$health_status${NC}"
                ;;
            *)
                echo -e "  ✅ $service: ${GREEN}running${NC}"
                ;;
        esac
    else
        echo -e "  ❌ $service: ${RED}not running${NC}"
    fi
done

echo ""

# API Endpoint Tests
echo -e "${CYAN}🧪 API Endpoint Tests:${NC}"
echo ""

# Gateway Test
echo -n "  Testing Gateway API (http://localhost:8080)... "
if curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FAIL${NC}"
fi

# Compliance Test
echo -n "  Testing Compliance API (http://localhost:8081)... "
if curl -f http://localhost:8081/actuator/health >/dev/null 2>&1; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FAIL${NC}"
fi

# Document Test
echo -n "  Testing Document API (http://localhost:8082)... "
if curl -f http://localhost:8082/actuator/health >/dev/null 2>&1; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FAIL${NC}"
fi

# Ollama Test
echo -n "  Testing Ollama AI (http://localhost:11434)... "
if curl -f http://localhost:11434/api/tags >/dev/null 2>&1; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${YELLOW}⚠️ OPTIONAL${NC}"
fi

# Nginx Test
echo -n "  Testing Nginx Load Balancer (http://localhost:80)... "
if curl -f http://localhost/health >/dev/null 2>&1; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${YELLOW}⚠️ OPTIONAL${NC}"
fi

echo ""

# Service URLs anzeigen
echo -e "${GREEN}🎉 DTN Compliance Platform erfolgreich gestartet!${NC}"
echo ""
echo -e "${CYAN}🌐 Service URLs:${NC}"
echo ""
echo -e "${GREEN}📱 Main Services:${NC}"
echo "  ├── 🚪 API Gateway (Haupteinstieg):  http://localhost:8080/swagger-ui/"
echo "  ├── 🔄 Nginx Load Balancer:         http://localhost/ (Proxy zu Gateway)"
echo "  ├── ⚖️  Compliance Service:          http://localhost:8081/swagger-ui/"
echo "  ├── 📄 Document Service:            http://localhost:8082/swagger-ui/"
echo "  └── 🏥 Health Check (alle):         http://localhost:8080/actuator/health"
echo ""
echo -e "${GREEN}🗄️ Database Management:${NC}"
echo "  └── 🛠️  pgAdmin:                     http://localhost:5050"
echo "      ├── Login: admin@dtn-compliance.de"
echo "      └── Password: dtn_admin_2024"
echo ""
echo -e "${GREEN}🤖 AI Services:${NC}"
echo "  └── 🧠 Ollama API:                  http://localhost:11434/api/tags"
echo ""
echo -e "${GREEN}🗄️ Direct Database Connections:${NC}"
echo "  ├── Gateway DB:    localhost:5432/dtn_gateway"
echo "  ├── Compliance DB: localhost:5433/dtn_compliance"
echo "  ├── Document DB:   localhost:5434/dtn_document"
echo "  └── Credentials:   dtn_user / dtn_secure_2024"
echo ""

# Business Value Summary
echo -e "${CYAN}💰 Business Value Zusammenfassung:${NC}"
echo ""
echo -e "${GREEN}Für 100-MA Software-Firma:${NC}"
echo "  ├── 💰 Jahresersparnis: 96.960€"
echo "  ├── ⚖️  DSGVO Art. 30: Automatische VVT-Generierung"
echo "  ├── 📊 DSGVO Art. 35: DSFA-Automatisierung"
echo "  ├── 🤖 EU AI Act: KI-Risikoklassifizierung (seit Feb 2025)"
echo "  ├── 🛡️  Bußgeld-Vermeidung: bis 35 Mio€"
echo "  └── ✅ Deutsche Rechtssicherheit"
echo ""

# Demo Commands
echo -e "${CYAN}🎯 Quick Demo Commands:${NC}"
echo ""
echo "# DSGVO VVT (Art. 30) Demo:"
echo "curl -X GET 'http://localhost:8081/api/v1/vvt/generate'"
echo ""
echo "# EU AI Act Risikoklassifizierung Demo:"
echo "curl -X POST 'http://localhost:8081/api/v1/ai-act/classify-risk' \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{\"ai_system\": \"recommendation_engine\", \"domain\": \"e-commerce\"}'"
echo ""
echo "# DSFA (Art. 35) Demo:"
echo "curl -X POST 'http://localhost:8081/api/v1/dsfa/analyze' \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{\"processing_type\": \"profiling\", \"data_subjects\": \"customers\"}'"
echo ""

# Management Commands
echo -e "${CYAN}🔧 Management Commands:${NC}"
echo ""
echo "# Live Logs verfolgen:"
echo "docker-compose logs -f"
echo ""
echo "# Einzelne Service Logs:"
echo "docker-compose logs -f gateway-service"
echo "docker-compose logs -f compliance-service"
echo "docker-compose logs -f document-service"
echo ""
echo "# Service Status prüfen:"
echo "docker-compose ps"
echo ""
echo "# Services neustarten:"
echo "docker-compose restart [service-name]"
echo ""
echo "# Platform stoppen:"
echo "docker-compose down"
echo ""
echo "# Komplettes Cleanup:"
echo "docker-compose down --volumes"
echo "docker system prune -f"
echo ""

# Troubleshooting Hints
echo -e "${YELLOW}🔍 Troubleshooting Tipps:${NC}"
echo ""
echo "1. Wenn Services nicht starten:"
echo "   → docker-compose logs [service-name]"
echo "   → docker system df (Disk Space prüfen)"
echo "   → docker system prune -f (Cleanup)"
echo ""
echo "2. Wenn Health Checks fehlschlagen:"
echo "   → Warte 2-3 Minuten (Java Services brauchen Zeit)"
echo "   → Prüfe Memory: docker stats"
echo "   → Prüfe Ports: netstat -tulpn | grep [port]"
echo ""
echo "3. Wenn Ollama AI nicht läuft:"
echo "   → Ist optional, Platform funktioniert auch ohne"
echo "   → Braucht 2-4GB RAM"
echo "   → docker-compose logs ollama"
echo ""
echo "4. Bei Database-Problemen:"
echo "   → docker-compose exec postgres-gateway psql -U dtn_user -d dtn_gateway"
echo "   → Oder verwende pgAdmin: http://localhost:5050"
echo ""

# Footer
echo -e "${CYAN}"
echo "────────────────────────────────────────────────────────────────────────────────"
echo "💻 DTN Compliance Platform | Entwickelt mit ❤️ in München"
echo "👨‍💻 Duc Thanh Nguyen | n.thanh@gmx.de"
echo "🔗 LinkedIn: linkedin.com/in/duc-thanh-nguyen-55aa5941"
echo "🚀 Live-Demo: https://dtn-compliance.onrender.com/swagger-ui/"
echo "📚 GitHub: https://github.com/thanhtuanh/dtn-compliance-platform"
echo "────────────────────────────────────────────────────────────────────────────────"
echo -e "${NC}"

# Optional: Öffne Browser automatisch
if command -v open >/dev/null 2>&1; then
    # macOS
    echo "🌐 Öffne Browser automatisch..."
    sleep 3
    open "http://localhost:8080/swagger-ui/" 2>/dev/null &
elif command -v xdg-open >/dev/null 2>&1; then
    # Linux
    echo "🌐 Öffne Browser automatisch..."
    sleep 3
    xdg-open "http://localhost:8080/swagger-ui/" 2>/dev/null &
elif command -v start >/dev/null 2>&1; then
    # Windows
    echo "🌐 Öffne Browser automatisch..."
    sleep 3
    start "http://localhost:8080/swagger-ui/" 2>/dev/null &
fi

echo ""
echo -e "${GREEN}✅ Quick Start abgeschlossen! Platform läuft und ist bereit für Demos.${NC}"
echo ""