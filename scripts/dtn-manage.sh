#!/bin/bash
# scripts/dtn-manage.sh
# DTN Compliance Platform - Management Script
# Autor: Duc Thanh Nguyen | München | n.thanh@gmx.de

set -euo pipefail

# Farben für bessere Übersicht
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# DTN Platform Banner
print_banner() {
    echo -e "${BLUE}"
    echo "╔═══════════════════════════════════════════════════════════════════════════════╗"
    echo "║                     DTN Compliance Platform Manager                           ║"
    echo "║                                                                               ║"
    echo "║  🏢 DSGVO + EU AI Act konforme KI-Lösung für deutsche Unternehmen           ║"
    echo "║  💰 Business Value: 96.960€ Jahresersparnis                                 ║"
    echo "║  👨‍💻 Entwickelt von: Duc Thanh Nguyen | München                             ║"
    echo "║  📧 Kontakt: n.thanh@gmx.de                                                 ║"
    echo "╚═══════════════════════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Hilfefunktion
show_help() {
    echo -e "${CYAN}DTN Platform Management Commands:${NC}"
    echo ""
    echo -e "${GREEN}🚀 Startup Commands:${NC}"
    echo "  start          - Startet alle Services"
    echo "  start-dev      - Startet nur Databases und Infrastructure"
    echo "  start-ai       - Startet nur AI Services (Ollama)"
    echo ""
    echo -e "${GREEN}🛑 Shutdown Commands:${NC}"
    echo "  stop           - Stoppt alle Services"
    echo "  stop-services  - Stoppt nur Microservices"
    echo "  restart        - Neustart aller Services"
    echo ""
    echo -e "${GREEN}📊 Monitoring Commands:${NC}"
    echo "  status         - Zeigt Status aller Services"
    echo "  health         - Health Check aller Services"
    echo "  logs           - Zeigt Live-Logs aller Services"
    echo "  logs-gateway   - Zeigt nur Gateway Logs"
    echo "  logs-compliance - Zeigt nur Compliance Logs"
    echo "  logs-document  - Zeigt nur Document Logs"
    echo "  logs-ollama    - Zeigt nur Ollama Logs"
    echo ""
    echo -e "${GREEN}🗄️ Database Commands:${NC}"
    echo "  db-status      - Zeigt Database Status"
    echo "  db-migrate     - Führt Database Migration aus"
    echo "  db-reset       - Reset aller Databases (VORSICHT!)"
    echo "  db-backup      - Erstellt Database Backup"
    echo "  db-connect     - Verbindet zu Gateway Database"
    echo ""
    echo -e "${GREEN}🧹 Maintenance Commands:${NC}"
    echo "  cleanup        - Aufräumen (stopped containers, unused images)"
    echo "  cleanup-all    - Komplettes Aufräumen (inkl. Volumes)"
    echo "  update         - Update aller Docker Images"
    echo "  rebuild        - Rebuild aller Services"
    echo ""
    echo -e "${GREEN}🧪 Development Commands:${NC}"
    echo "  test           - Führt alle Tests aus"
    echo "  test-api       - Testet API Endpoints"
    echo "  demo-data      - Lädt Demo-Daten"
    echo "  shell-gateway  - Shell zu Gateway Service"
    echo "  shell-db       - Shell zu Gateway Database"
    echo ""
    echo -e "${GREEN}📖 Information Commands:${NC}"
    echo "  urls           - Zeigt alle Service URLs"
    echo "  info           - Zeigt Platform Information"
    echo "  version        - Zeigt Versions-Informationen"
    echo ""
}

# Service URLs anzeigen
show_urls() {
    echo -e "${CYAN}🌐 DTN Platform Service URLs:${NC}"
    echo ""
    echo -e "${GREEN}📱 Main Services:${NC}"
    echo "  ├── 🚪 API Gateway:     http://localhost:8080/swagger-ui/"
    echo "  ├── ⚖️  Compliance:      http://localhost:8081/swagger-ui/"
    echo "  ├── 📄 Document:        http://localhost:8082/swagger-ui/"
    echo "  └── 🔄 Load Balancer:   http://localhost"
    echo ""
    echo -e "${GREEN}🏥 Health Checks:${NC}"
    echo "  ├── Gateway Health:    http://localhost:8080/actuator/health"
    echo "  ├── Compliance Health: http://localhost:8081/actuator/health"
    echo "  └── Document Health:   http://localhost:8082/actuator/health"
    echo ""
    echo -e "${GREEN}🗄️ Database Management:${NC}"
    echo "  └── pgAdmin:           http://localhost:5050"
    echo "      └── Login: admin@dtn-compliance.de / dtn_admin_2024"
    echo ""
    echo -e "${GREEN}🤖 AI Services:${NC}"
    echo "  └── Ollama API:        http://localhost:11434/api/tags"
    echo ""
    echo -e "${GREEN}📊 Monitoring:${NC}"
    echo "  ├── Nginx Status:      http://localhost/nginx_status"
    echo "  └── Redis Info:        redis://localhost:6379"
    echo ""
}

# Platform Information
show_info() {
    echo -e "${CYAN}📊 DTN Compliance Platform Information:${NC}"
    echo ""
    echo -e "${GREEN}💼 Business Value:${NC}"
    echo "  ├── Jahresersparnis: 96.960€ (100-MA Software-Firma)"
    echo "  ├── DSGVO Art. 30: Automatische VVT-Generierung"
    echo "  ├── DSGVO Art. 35: DSFA-Automatisierung"  
    echo "  ├── EU AI Act: KI-Risikoklassifizierung (seit Feb 2025)"
    echo "  └── Bußgeld-Vermeidung: bis 35 Mio€"
    echo ""
    echo -e "${GREEN}🏗️ Architektur:${NC}"
    echo "  ├── 3x PostgreSQL Databases (Gateway, Compliance, Document)"
    echo "  ├── 3x Spring Boot Microservices (Java 21)"
    echo "  ├── 1x Ollama AI (Local/Privacy by Design)"
    echo "  ├── 1x Redis Cache"
    echo "  ├── 1x Nginx Load Balancer"
    echo "  └── 1x pgAdmin Management UI"
    echo ""
    echo -e "${GREEN}🛡️ Compliance Features:${NC}"
    echo "  ├── DSGVO Art. 30: VVT automatisch"
    echo "  ├── DSGVO Art. 35: DSFA-Templates"
    echo "  ├── EU AI Act: Risikoklassifizierung"
    echo "  ├── Privacy by Design: Lokale KI"
    echo "  ├── Audit Logging: DSGVO-konform"
    echo "  └── Deutsche Rechtssicherheit"
    echo ""
}

# Status aller Services
check_status() {
    echo -e "${CYAN}📊 DTN Platform Service Status:${NC}"
    echo ""
    
    services=("dtn-postgres-gateway" "dtn-postgres-compliance" "dtn-postgres-document" "dtn-gateway" "dtn-compliance" "dtn-document" "dtn-ollama" "dtn-redis" "dtn-nginx" "dtn-pgadmin")
    
    for service in "${services[@]}"; do
        if docker ps --format "table {{.Names}}" | grep -q "^$service$"; then
            status=$(docker inspect --format="{{.State.Health.Status}}" "$service" 2>/dev/null || echo "running")
            if [ "$status" = "healthy" ] || [ "$status" = "running" ]; then
                echo -e "  ✅ $service: ${GREEN}$status${NC}"
            else
                echo -e "  ⚠️  $service: ${YELLOW}$status${NC}"
            fi
        else
            echo -e "  ❌ $service: ${RED}stopped${NC}"
        fi
    done
    echo ""
}

# Health Check aller Services
health_check() {
    echo -e "${CYAN}🏥 DTN Platform Health Check:${NC}"
    echo ""
    
    # Gateway Health
    if curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
        echo -e "  ✅ Gateway Service: ${GREEN}healthy${NC}"
    else
        echo -e "  ❌ Gateway Service: ${RED}unhealthy${NC}"
    fi
    
    # Compliance Health
    if curl -f http://localhost:8081/actuator/health >/dev/null 2>&1; then
        echo -e "  ✅ Compliance Service: ${GREEN}healthy${NC}"
    else
        echo -e "  ❌ Compliance Service: ${RED}unhealthy${NC}"
    fi
    
    # Document Health
    if curl -f http://localhost:8082/actuator/health >/dev/null 2>&1; then
        echo -e "  ✅ Document Service: ${GREEN}healthy${NC}"
    else
        echo -e "  ❌ Document Service: ${RED}unhealthy${NC}"
    fi
    
    # Ollama Health
    if curl -f http://localhost:11434/api/tags >/dev/null 2>&1; then
        echo -e "  ✅ Ollama AI: ${GREEN}healthy${NC}"
    else
        echo -e "  ❌ Ollama AI: ${RED}unhealthy${NC}"
    fi
    
    # Database Health
    for port in 5432 5433 5434; do
        if nc -z localhost $port >/dev/null 2>&1; then
            echo -e "  ✅ Database ($port): ${GREEN}healthy${NC}"
        else
            echo -e "  ❌ Database ($port): ${RED}unhealthy${NC}"
        fi
    done
    
    echo ""
}

# Database Status
db_status() {
    echo -e "${CYAN}🗄️ Database Status:${NC}"
    echo ""
    
    databases=("dtn_gateway:5432" "dtn_compliance:5433" "dtn_document:5434")
    
    for db in "${databases[@]}"; do
        db_name=$(echo $db | cut -d: -f1)
        port=$(echo $db | cut -d: -f2)
        
        if nc -z localhost $port >/dev/null 2>&1; then
            echo -e "  ✅ $db_name: ${GREEN}running on port $port${NC}"
        else
            echo -e "  ❌ $db_name: ${RED}not accessible on port $port${NC}"
        fi
    done
    echo ""
}

# API Tests
test_api() {
    echo -e "${CYAN}🧪 API Endpoint Tests:${NC}"
    echo ""
    
    # Gateway Test
    echo -n "  Testing Gateway API... "
    if curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
        echo -e "${GREEN}✅ OK${NC}"
    else
        echo -e "${RED}❌ FAIL${NC}"
    fi
    
    # Compliance Test
    echo -n "  Testing Compliance API... "
    if curl -f http://localhost:8081/actuator/health >/dev/null 2>&1; then
        echo -e "${GREEN}✅ OK${NC}"
    else
        echo -e "${RED}❌ FAIL${NC}"
    fi
    
    # Document Test
    echo -n "  Testing Document API... "
    if curl -f http://localhost:8082/actuator/health >/dev/null 2>&1; then
        echo -e "${GREEN}✅ OK${NC}"
    else
        echo -e "${RED}❌ FAIL${NC}"
    fi
    
    # Swagger UI Test
    echo -n "  Testing Swagger UI... "
    if curl -f http://localhost:8080/swagger-ui/ >/dev/null 2>&1; then
        echo -e "${GREEN}✅ OK${NC}"
    else
        echo -e "${RED}❌ FAIL${NC}"
    fi
    
    echo ""
}

# Main Command Handler
case "${1:-help}" in
    "start")
        print_banner
        echo -e "${GREEN}🚀 Starte DTN Compliance Platform...${NC}"
        docker-compose up -d
        echo -e "${GREEN}✅ Platform gestartet!${NC}"
        show_urls
        ;;
    "start-dev")
        print_banner
        echo -e "${GREEN}🔧 Starte Development Environment (nur DBs und Infrastructure)...${NC}"
        docker-compose up -d postgres-gateway postgres-compliance postgres-document redis ollama pgadmin
        echo -e "${GREEN}✅ Development Environment gestartet!${NC}"
        ;;
    "start-ai")
        print_banner
        echo -e "${GREEN}🤖 Starte AI Services...${NC}"
        docker-compose up -d ollama
        echo -e "${GREEN}✅ AI Services gestartet!${NC}"
        ;;
    "stop")
        echo -e "${YELLOW}🛑 Stoppe DTN Compliance Platform...${NC}"
        docker-compose down
        echo -e "${GREEN}✅ Platform gestoppt!${NC}"
        ;;
    "stop-services")
        echo -e "${YELLOW}🛑 Stoppe nur Microservices...${NC}"
        docker-compose stop gateway-service compliance-service document-service nginx
        echo -e "${GREEN}✅ Microservices gestoppt!${NC}"
        ;;
    "restart")
        echo -e "${YELLOW}🔄 Neustart DTN Compliance Platform...${NC}"
        docker-compose down
        docker-compose up -d
        echo -e "${GREEN}✅ Platform neu gestartet!${NC}"
        ;;
    "status")
        print_banner
        check_status
        ;;
    "health")
        print_banner
        health_check
        ;;
    "logs")
        echo -e "${CYAN}📋 Live-Logs aller Services:${NC}"
        docker-compose logs -f
        ;;
    "logs-gateway")
        echo -e "${CYAN}📋 Gateway Service Logs:${NC}"
        docker-compose logs -f gateway-service
        ;;
    "logs-compliance")
        echo -e "${CYAN}📋 Compliance Service Logs:${NC}"
        docker-compose logs -f compliance-service
        ;;
    "logs-document")
        echo -e "${CYAN}📋 Document Service Logs:${NC}"
        docker-compose logs -f document-service
        ;;
    "logs-ollama")
        echo -e "${CYAN}📋 Ollama AI Logs:${NC}"
        docker-compose logs -f ollama
        ;;
    "db-status")
        print_banner
        db_status
        ;;
    "db-migrate")
        echo -e "${CYAN}🗄️ Database Migration...${NC}"
        docker-compose exec gateway-service java -jar app.jar --spring.profiles.active=migrate
        docker-compose exec compliance-service java -jar app.jar --spring.profiles.active=migrate
        docker-compose exec document-service java -jar app.jar --spring.profiles.active=migrate
        echo -e "${GREEN}✅ Migration abgeschlossen!${NC}"
        ;;
    "db-reset")
        echo -e "${RED}⚠️  ACHTUNG: Database Reset löscht ALLE Daten!${NC}"
        read -p "Wirklich fortfahren? (yes/no): " confirm
        if [ "$confirm" = "yes" ]; then
            echo -e "${YELLOW}🗄️ Resette Databases...${NC}"
            docker-compose down postgres-gateway postgres-compliance postgres-document
            docker volume rm dtn_postgres_gateway_data dtn_postgres_compliance_data dtn_postgres_document_data || true
            docker-compose up -d postgres-gateway postgres-compliance postgres-document
            echo -e "${GREEN}✅ Databases resettet!${NC}"
        else
            echo -e "${GREEN}❌ Database Reset abgebrochen.${NC}"
        fi
        ;;
    "db-backup")
        echo -e "${CYAN}💾 Erstelle Database Backup...${NC}"
        backup_dir="./backups/$(date +%Y%m%d_%H%M%S)"
        mkdir -p "$backup_dir"
        
        docker-compose exec postgres-gateway pg_dump -U dtn_user dtn_gateway > "$backup_dir/gateway.sql"
        docker-compose exec postgres-compliance pg_dump -U dtn_user dtn_compliance > "$backup_dir/compliance.sql"
        docker-compose exec postgres-document pg_dump -U dtn_user dtn_document > "$backup_dir/document.sql"
        
        echo -e "${GREEN}✅ Backup erstellt in: $backup_dir${NC}"
        ;;
    "db-connect")
        echo -e "${CYAN}🔌 Verbinde zu Gateway Database...${NC}"
        docker-compose exec postgres-gateway psql -U dtn_user -d dtn_gateway
        ;;
    "cleanup")
        echo -e "${YELLOW}🧹 Aufräumen (stopped containers, unused images)...${NC}"
        docker system prune -f
        echo -e "${GREEN}✅ Aufräumen abgeschlossen!${NC}"
        ;;
    "cleanup-all")
        echo -e "${RED}⚠️  ACHTUNG: Komplettes Aufräumen löscht ALLE Volumes!${NC}"
        read -p "Wirklich fortfahren? (yes/no): " confirm
        if [ "$confirm" = "yes" ]; then
            echo -e "${YELLOW}🧹 Komplettes Aufräumen...${NC}"
            docker-compose down --volumes
            docker system prune -af --volumes
            echo -e "${GREEN}✅ Komplettes Aufräumen abgeschlossen!${NC}"
        else
            echo -e "${GREEN}❌ Aufräumen abgebrochen.${NC}"
        fi
        ;;
    "update")
        echo -e "${CYAN}⬆️ Update aller Docker Images...${NC}"
        docker-compose pull
        docker-compose up -d
        echo -e "${GREEN}✅ Update abgeschlossen!${NC}"
        ;;
    "rebuild")
        echo -e "${CYAN}🔨 Rebuild aller Services...${NC}"
        docker-compose down
        docker-compose build --no-cache
        docker-compose up -d
        echo -e "${GREEN}✅ Rebuild abgeschlossen!${NC}"
        ;;
    "test")
        print_banner
        echo -e "${CYAN}🧪 Führe alle Tests aus...${NC}"
        test_api
        health_check
        ;;
    "test-api")
        print_banner
        test_api
        ;;
    "demo-data")
        echo -e "${CYAN}📊 Lade Demo-Daten...${NC}"
        
        # VVT Demo-Daten
        curl -X POST http://localhost:8081/api/v1/vvt/demo-data
        
        # DSFA Demo-Daten
        curl -X POST http://localhost:8081/api/v1/dsfa/demo-data
        
        # AI Act Demo-Daten
        curl -X POST http://localhost:8081/api/v1/ai-act/demo-data
        
        echo -e "${GREEN}✅ Demo-Daten geladen!${NC}"
        ;;
    "shell-gateway")
        echo -e "${CYAN}🐚 Shell zu Gateway Service...${NC}"
        docker-compose exec gateway-service /bin/bash
        ;;
    "shell-db")
        echo -e "${CYAN}🐚 Shell zu Gateway Database...${NC}"
        docker-compose exec postgres-gateway /bin/bash
        ;;
    "urls")
        print_banner
        show_urls
        ;;
    "info")
        print_banner
        show_info
        ;;
    "version")
        print_banner
        echo -e "${CYAN}📖 DTN Platform Versions:${NC}"
        echo ""
        echo -e "${GREEN}Platform Version:${NC} 5.0 - Complete Setup"
        echo -e "${GREEN}Docker Compose:${NC} $(docker-compose version --short)"
        echo -e "${GREEN}Docker Engine:${NC} $(docker version --format '{{.Server.Version}}')"
        echo -e "${GREEN}Java Version:${NC} 21 (OpenJDK)"
        echo -e "${GREEN}Spring Boot:${NC} 3.2+"
        echo -e "${GREEN}PostgreSQL:${NC} 15"
        echo -e "${GREEN}Ollama:${NC} Latest"
        echo ""
        echo -e "${GREEN}Services:${NC}"
        docker-compose ps --format "table {{.Name}}\t{{.Image}}\t{{.Status}}"
        echo ""
        ;;
    "help"|*)
        print_banner
        show_help
        ;;
esac

# Footer
echo -e "${PURPLE}"
echo "────────────────────────────────────────────────────────────────────────────────"
echo "💻 DTN Compliance Platform | Entwickelt mit ❤️ in München"
echo "👨‍💻 Duc Thanh Nguyen | n.thanh@gmx.de | LinkedIn: duc-thanh-nguyen-55aa5941"
echo "🚀 Live-Demo: https://dtn-compliance.onrender.com/swagger-ui/"
echo "📚 GitHub: https://github.com/thanhtuanh/dtn-compliance-platform"
echo "────────────────────────────────────────────────────────────────────────────────"
echo -e "${NC}"