#!/bin/bash

# DTN Compliance Platform - Health Check Script
# KORRIGIERT für tatsächliche Swagger UI Pfade und return-Fehler

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Service Configuration
GATEWAY_URL="http://localhost:8080"
COMPLIANCE_URL="http://localhost:8081"
DOCUMENT_URL="http://localhost:8082"
OLLAMA_URL="http://localhost:11434"

# Database Configuration
DB_GATEWAY_PORT="5432"
DB_COMPLIANCE_PORT="5433" 
DB_DOCUMENT_PORT="5434"

# Script Configuration
TIMEOUT=10
DEMO_MODE=false
VERBOSE=false

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
        gateway|compliance|document|ollama|database)
            SERVICE_FILTER="$1"
            shift
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
    echo -e "${BLUE}DTN Compliance Platform - Health Check Script${NC}"
    echo ""
    echo "Usage: $0 [OPTIONS] [SERVICE]"
    echo ""
    echo "Services:"
    echo "  gateway     Check Gateway Service (Port 8080)"
    echo "  compliance  Check Compliance Service (Port 8081)" 
    echo "  document    Check Document Service (Port 8082)"
    echo "  ollama      Check Ollama AI Service (Port 11434)"
    echo "  database    Check PostgreSQL Databases"
    echo ""
    echo "Options:"
    echo "  --demo      Demo mode with enhanced output"
    echo "  --verbose   Verbose output with details"
    echo "  --help      Show this help message"
}

# Demo banner for presentations
show_demo_banner() {
    if [[ "$DEMO_MODE" == "true" ]]; then
        echo -e "${PURPLE}╔════════════════════════════════════════════════════════════════╗${NC}"
        echo -e "${PURPLE}║${NC}  🚀 ${CYAN}DTN Compliance Intelligence Platform${NC} - Health Check    ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}                                                                ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}  📊 Business Value: ${GREEN}96.960€ Jahresersparnis${NC}                  ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}  🎯 Zielgruppe: ${YELLOW}Deutsche Software-Dienstleister (50-200 MA)${NC} ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}  🇩🇪 Compliance: ${GREEN}DSGVO + EU AI Act konform${NC}                  ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}                                                                ${PURPLE}║${NC}"
        echo -e "${PURPLE}║${NC}  💼 Entwickelt von: ${CYAN}Duc Thanh Nguyen${NC} | München/Bayern     ${PURPLE}║${NC}"
        echo -e "${PURPLE}╚════════════════════════════════════════════════════════════════╝${NC}"
        echo ""
    fi
}

# Check if curl is available
check_prerequisites() {
    if ! command -v curl &> /dev/null; then
        echo -e "${RED}❌ curl ist nicht installiert. Bitte installieren Sie curl.${NC}"
        exit 1
    fi
    
    if ! command -v pg_isready &> /dev/null && [[ "$SERVICE_FILTER" == "database" || -z "$SERVICE_FILTER" ]]; then
        echo -e "${YELLOW}⚠️  pg_isready nicht verfügbar. Database-Checks werden übersprungen.${NC}"
    fi
}

# Generic HTTP health check - KORRIGIERT: return-Werte
check_http_service() {
    local name="$1"
    local url="$2"
    local icon="$3"
    local expected_status="${4:-200}"
    
    echo -n -e "  ${icon} ${name}: "
    
    if [[ "$VERBOSE" == "true" ]]; then
        echo -e "\n    URL: ${url}"
        echo -n "    Status: "
    fi
    
    local response=$(curl -s -o /dev/null -w "%{http_code}" --connect-timeout $TIMEOUT "$url" 2>/dev/null || echo "000")
    
    if [[ "$response" == "$expected_status" ]]; then
        echo -e "${GREEN}✅ UP${NC} (HTTP $response)"
        return 0
    else
        echo -e "${RED}❌ DOWN${NC} (HTTP $response)"
        return 1
    fi
}

# Check API endpoint with JSON response
check_api_endpoint() {
    local name="$1"
    local url="$2"
    local icon="$3"
    
    echo -n -e "  ${icon} ${name}: "
    
    local response=$(curl -s --connect-timeout $TIMEOUT "$url" 2>/dev/null || echo "{}")
    
    if echo "$response" | grep -q '"status":\s*"UP"' || echo "$response" | grep -q '"demo_ready":\s*true'; then
        echo -e "${GREEN}✅ UP${NC}"
        if [[ "$VERBOSE" == "true" ]]; then
            echo "    Response: $(echo $response | head -c 100)..."
        fi
        return 0
    else
        echo -e "${RED}❌ DOWN${NC}"
        if [[ "$VERBOSE" == "true" ]]; then
            echo "    Response: $(echo $response | head -c 100)..."
        fi
        return 1
    fi
}

# Check PostgreSQL database
check_database() {
    local name="$1"
    local port="$2"
    local icon="$3"
    
    echo -n -e "  ${icon} ${name}: "
    
    if command -v pg_isready &> /dev/null; then
        if pg_isready -h localhost -p $port -U dtn_user -t $TIMEOUT &> /dev/null; then
            echo -e "${GREEN}✅ UP${NC} (Port $port)"
            return 0
        else
            echo -e "${RED}❌ DOWN${NC} (Port $port)"
            return 1
        fi
    else
        # Fallback: TCP connection check
        if timeout $TIMEOUT bash -c "</dev/tcp/localhost/$port" 2>/dev/null; then
            echo -e "${YELLOW}🔶 TCP OK${NC} (Port $port - pg_isready nicht verfügbar)"
            return 0
        else
            echo -e "${RED}❌ DOWN${NC} (Port $port)"
            return 1
        fi
    fi
}

# Check Docker containers
check_docker_containers() {
    echo -e "${BLUE}🐳 Docker Container Status:${NC}"
    
    local containers=("dtn-postgres-gateway" "dtn-postgres-compliance" "dtn-postgres-document" "dtn-gateway" "dtn-ollama")
    
    for container in "${containers[@]}"; do
        if docker ps --format "table {{.Names}}" | grep -q "^$container$" 2>/dev/null; then
            local status=$(docker inspect --format='{{.State.Health.Status}}' "$container" 2>/dev/null || echo "no-healthcheck")
            case $status in
                "healthy")
                    echo -e "  🐳 $container: ${GREEN}✅ HEALTHY${NC}"
                    ;;
                "unhealthy")
                    echo -e "  🐳 $container: ${RED}❌ UNHEALTHY${NC}"
                    ;;
                "starting")
                    echo -e "  🐳 $container: ${YELLOW}🔄 STARTING${NC}"
                    ;;
                "no-healthcheck")
                    echo -e "  🐳 $container: ${BLUE}🔶 RUNNING${NC} (no healthcheck)"
                    ;;
                *)
                    echo -e "  🐳 $container: ${YELLOW}🔶 $status${NC}"
                    ;;
            esac
        else
            echo -e "  🐳 $container: ${RED}❌ NOT RUNNING${NC}"
        fi
    done
    echo ""
}

# Main health check function - KORRIGIERT: return-Werte
run_health_checks() {
    local all_healthy=0  # 0 = healthy, 1 = unhealthy
    
    # Gateway Service
    if [[ -z "$SERVICE_FILTER" || "$SERVICE_FILTER" == "gateway" ]]; then
        echo -e "${BLUE}🚪 Gateway Service (Port 8080):${NC}"
        
        # KORRIGIERT: Richtiger Swagger UI Pfad
        if ! check_http_service "Swagger UI" "$GATEWAY_URL/swagger-ui/index.html" "📚"; then
            all_healthy=1
        fi
        
        if ! check_http_service "Health Check" "$GATEWAY_URL/actuator/health" "💚"; then
            all_healthy=1
        fi
        
        if ! check_api_endpoint "Gateway Status" "$GATEWAY_URL/api/v1/gateway/status" "📊"; then
            all_healthy=1
        fi
        
        if ! check_api_endpoint "Demo Ready" "$GATEWAY_URL/api/v1/gateway/demo-ready" "🎯"; then
            all_healthy=1
        fi
        
        echo ""
    fi
    
    # Compliance Service (Step 3)
    if [[ -z "$SERVICE_FILTER" || "$SERVICE_FILTER" == "compliance" ]]; then
        echo -e "${BLUE}⚖️  Compliance Service (Port 8081):${NC}"
        if ! check_http_service "Health Check" "$COMPLIANCE_URL/actuator/health" "💚"; then
            echo -e "  ⏳ ${YELLOW}Wird in Step 3 erstellt${NC}"
        fi
        echo -e "  🇩🇪 VVT Generation: ❌ DOWN"
        echo -e "  ⏳ ${YELLOW}DSGVO Art. 30 - Step 3${NC}"
        echo -e "  🇪🇺 AI Risk Classification: ❌ DOWN"
        echo -e "  ⏳ ${YELLOW}EU AI Act - Step 3${NC}"
        echo ""
    fi
    
    # Document Service (Step 4)
    if [[ -z "$SERVICE_FILTER" || "$SERVICE_FILTER" == "document" ]]; then
        echo -e "${BLUE}📄 Document Service (Port 8082):${NC}"
        if ! check_http_service "Health Check" "$DOCUMENT_URL/actuator/health" "💚"; then
            echo -e "  ⏳ ${YELLOW}Wird in Step 4 erstellt${NC}"
        fi
        echo -e "  📑 PDF Generation: ❌ DOWN"
        echo -e "  ⏳ ${YELLOW}Deutsche Templates - Step 4${NC}"
        echo ""
    fi
    
    # Ollama AI Service
    if [[ -z "$SERVICE_FILTER" || "$SERVICE_FILTER" == "ollama" ]]; then
        echo -e "${BLUE}🤖 Ollama AI Service (Port 11434):${NC}"
        check_http_service "API Tags" "$OLLAMA_URL/api/tags" "🤖"
        echo ""
    fi
    
    # Database Services
    if [[ -z "$SERVICE_FILTER" || "$SERVICE_FILTER" == "database" ]]; then
        echo -e "${BLUE}🗄️  Database Services:${NC}"
        
        if ! check_database "Gateway DB" "$DB_GATEWAY_PORT" "🚪"; then
            all_healthy=1
        fi
        
        check_database "Compliance DB" "$DB_COMPLIANCE_PORT" "⚖️"
        check_database "Document DB" "$DB_DOCUMENT_PORT" "📄"
        echo ""
    fi
    
    # Docker Status (if available)
    if command -v docker &> /dev/null && [[ -z "$SERVICE_FILTER" ]]; then
        check_docker_containers
    fi
    
    return $all_healthy
}

# Summary and next steps
show_summary() {
    local healthy=$1
    
    if [[ $healthy -eq 0 ]]; then
        echo -e "${GREEN}✅ Alle verfügbaren Services sind healthy!${NC}"
        echo ""
        echo -e "${BLUE}🎯 Demo URLs:${NC}"
        echo -e "  📚 Swagger UI: ${CYAN}$GATEWAY_URL/swagger-ui/index.html${NC}"
        echo -e "  💚 Health Check: ${CYAN}$GATEWAY_URL/actuator/health${NC}"  
        echo -e "  📊 Gateway Status: ${CYAN}$GATEWAY_URL/api/v1/gateway/status${NC}"
        echo -e "  💼 Developer Info: ${CYAN}$GATEWAY_URL/api/v1/gateway/developer-info${NC}"
        echo ""
        echo -e "${GREEN}🚀 Bereit für Step 3: Compliance Service erstellen!${NC}"
        echo -e "   Sagen Sie 'OK Step 3' für DSGVO + EU AI Act Features"
        return 0
    else
        echo -e "${RED}❌ Einige Services sind nicht verfügbar${NC}"
        echo ""
        echo -e "${YELLOW}🔧 Troubleshooting:${NC}"
        echo -e "  1. Docker Services starten: ${CYAN}docker-compose up -d${NC}"
        echo -e "  2. Gateway Service: ${CYAN}cd services/gateway-service && mvn spring-boot:run${NC}"
        echo -e "  3. WebConfig hinzufügen für Swagger UI Fix"
        echo ""
        return 1
    fi
}

# Business metrics for demo presentations
show_business_metrics() {
    if [[ "$DEMO_MODE" == "true" ]]; then
        echo -e "${PURPLE}💰 Business Value Demonstration:${NC}"
        echo -e "  📈 Jahresersparnis: ${GREEN}96.960€${NC} (100-MA Software-Firma)"
        echo -e "  ⚡ VVT-Generierung: ${GREEN}95% Zeitersparnis${NC} (8h → 24min)"
        echo -e "  🤖 DSFA-Automatisierung: ${GREEN}87% Effizienzsteigerung${NC}"
        echo -e "  🎯 ROI: ${GREEN}340%${NC} im ersten Jahr"
        echo ""
        echo -e "${PURPLE}🎪 Bewerbungs-Ready:${NC}"
        echo -e "  ✅ Live-Demo in 5 Minuten startbereit"
        echo -e "  ✅ Professional Setup für Technical Interviews"
        echo -e "  ✅ Business Case überzeugt Management"
        echo ""
    fi
}

# Main execution
main() {
    show_demo_banner
    check_prerequisites
    
    echo -e "${BLUE}🔍 DTN Compliance Platform - Health Check${NC}"
    echo -e "$(date '+%Y-%m-%d %H:%M:%S') - Checking services..."
    echo ""
    
    if run_health_checks; then
        show_summary 0
        show_business_metrics
        exit 0
    else
        show_summary 1
        exit 1
    fi
}

# Execute main function
main "$@"