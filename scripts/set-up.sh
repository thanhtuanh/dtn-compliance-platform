#!/bin/bash
# scripts/setup.sh
# DTN Compliance Platform - Initial Setup Script
# Erstellt alle notwendigen Ordner und Dateien

set -euo pipefail

echo "🚀 DTN Compliance Platform - Initial Setup"
echo "=========================================="
echo ""

# Farben
GREEN='\033[0;32m'
CYAN='\033[0;36m'
NC='\033[0m'

# Erstelle Ordnerstruktur
echo -e "${CYAN}📁 Erstelle Ordnerstruktur...${NC}"

directories=(
    "scripts"
    "nginx"
    "services/gateway-service"
    "services/compliance-service" 
    "services/document-service"
    "backups"
    "logs"
)

for dir in "${directories[@]}"; do
    if [ ! -d "$dir" ]; then
        mkdir -p "$dir"
        echo "  ✅ $dir"
    else
        echo "  ✓ $dir (bereits vorhanden)"
    fi
done

echo ""

# Erstelle nginx.conf falls nicht vorhanden
echo -e "${CYAN}📝 Erstelle Nginx Konfiguration...${NC}"

if [ ! -f "nginx/nginx.conf" ]; then
    cat > nginx/nginx.conf << 'EOF'
# DTN Compliance Platform - Nginx Load Balancer
worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

events {
    worker_connections 1024;
    use epoll;
    multi_accept on;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;
    
    # Logging
    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';
    
    access_log /var/log/nginx/access.log main;
    
    # Performance
    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    client_max_body_size 50M;
    
    # Compression
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;
    
    # Security Headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header Referrer-Policy "strict-origin-when-cross-origin" always;
    
    # Rate Limiting
    limit_req_zone $binary_remote_addr zone=api:10m rate=10r/s;
    
    # Upstream
    upstream gateway_backend {
        server gateway-service:8080 max_fails=3 fail_timeout=30s;
        keepalive 32;
    }
    
    # Main Server
    server {
        listen 80;
        server_name localhost dtn-compliance.local;
        
        # Main API Gateway Route
        location / {
            proxy_pass http://gateway_backend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            # Timeouts
            proxy_connect_timeout 30s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;
            
            # Keep-alive
            proxy_http_version 1.1;
            proxy_set_header Connection "";
            
            # Rate Limiting
            limit_req zone=api burst=20 nodelay;
        }
        
        # Health Check
        location /health {
            access_log off;
            return 200 "DTN Compliance Platform Nginx - healthy\n";
            add_header Content-Type text/plain;
        }
        
        # Nginx Status (für Monitoring)
        location /nginx_status {
            stub_status on;
            access_log off;
            allow 127.0.0.1;
            allow 172.20.0.0/16;
            deny all;
        }
        
        # Security: Block hidden files
        location ~ /\. {
            deny all;
            access_log off;
            log_not_found off;
        }
        
        # Security: Block sensitive files
        location ~ \.(sql|log|conf)$ {
            deny all;
            access_log off;
            log_not_found off;
        }
    }
}
EOF
    echo "  ✅ nginx/nginx.conf erstellt"
else
    echo "  ✓ nginx/nginx.conf (bereits vorhanden)"
fi

echo ""

# Scripts ausführbar machen
echo -e "${CYAN}🔧 Mache Scripts ausführbar...${NC}"

scripts=(
    "scripts/quick-start.sh"
    "scripts/dtn-manage.sh" 
    "scripts/setup.sh"
)

for script in "${scripts[@]}"; do
    if [ -f "$script" ]; then
        chmod +x "$script"
        echo "  ✅ $script"
    else
        echo "  ⚠️ $script (nicht gefunden - wird später erstellt)"
    fi
done

echo ""

# .gitignore erstellen
echo -e "${CYAN}📄 Erstelle .gitignore...${NC}"

if [ ! -f ".gitignore" ]; then
    cat > .gitignore << 'EOF'
# DTN Compliance Platform - Git Ignore

# Docker
.docker/
docker-compose.override.yml

# Logs
logs/
*.log

# Backups
backups/

# IDE
.vscode/
.idea/
*.iml

# OS
.DS_Store
Thumbs.db

# Environment
.env
.env.local
.env.production

# Build artifacts
target/
build/
*.jar
*.war

# Database dumps
*.sql
*.dump

# SSL Certificates
nginx/ssl/
*.pem
*.crt
*.key

# Temporary files
tmp/
temp/
*.tmp
*.swp
*.swo

# Node modules (falls Frontend hinzugefügt wird)
node_modules/
dist/
EOF
    echo "  ✅ .gitignore erstellt"
else
    echo "  ✓ .gitignore (bereits vorhanden)"
fi

echo ""

# README.md erstellen/aktualisieren
echo -e "${CYAN}📖 Erstelle README.md...${NC}"

if [ ! -f "README.md" ]; then
    cat > README.md << 'EOF'
# DTN Compliance Platform

🏢 **DSGVO + EU AI Act konforme KI-Lösung für deutsche Unternehmen**

## 💰 Business Value

- **96.960€ Jahresersparnis** für 100-MA Software-Firma
- **DSGVO Art. 30**: Automatische VVT-Generierung
- **DSGVO Art. 35**: DSFA-Automatisierung
- **EU AI Act**: KI-Risikoklassifizierung (seit Feb 2025)
- **Bußgeld-Vermeidung**: bis 35 Mio€
- **Deutsche Rechtssicherheit**

## 🚀 Quick Start

```bash
# 1. Repository klonen
git clone https://github.com/thanhtuanh/dtn-compliance-platform
cd dtn-compliance-platform

# 2. Setup ausführen
./scripts/setup.sh

# 3. Platform starten
./scripts/quick-start.sh
```

## 🌐 Service URLs

- **API Gateway**: http://localhost:8080/swagger-ui/
- **Compliance**: http://localhost:8081/swagger-ui/
- **Document**: http://localhost:8082/swagger-ui/
- **Load Balancer**: http://localhost/
- **pgAdmin**: http://localhost:5050

## 🏗️ Architektur

- 3x PostgreSQL Databases
- 3x Spring Boot Microservices (Java 21)
- 1x Ollama AI (Local/Privacy by Design)
- 1x Redis Cache
- 1x Nginx Load Balancer
- 1x pgAdmin Management UI

## 👨‍💻 Entwickler

**Duc Thanh Nguyen**
- 📧 E-Mail: n.thanh@gmx.de
- 💼 LinkedIn: [duc-thanh-nguyen-55aa5941](https://www.linkedin.com/in/duc-thanh-nguyen-55aa5941)
- 🌐 GitHub: [thanhtuanh](https://github.com/thanhtuanh)
- 📍 München, Bayern

## 🚀 Live Demo

[https://dtn-compliance.onrender.com/swagger-ui/](https://dtn-compliance.onrender.com/swagger-ui/)
EOF
    echo "  ✅ README.md erstellt"
else
    echo "  ✓ README.md (bereits vorhanden)"
fi

echo ""

# Zusammenfassung
echo -e "${GREEN}✅ Setup abgeschlossen!${NC}"
echo ""
echo "📁 Ordnerstruktur erstellt"
echo "📝 Konfigurationsdateien erstellt"
echo "🔧 Scripts ausführbar gemacht"
echo "📖 Dokumentation erstellt"
echo ""
echo -e "${CYAN}🚀 Nächste Schritte:${NC}"
echo "1. Docker Compose File in Hauptverzeichnis speichern"
echo "2. Services implementieren (falls noch nicht vorhanden)"
echo "3. Platform starten: ./scripts/quick-start.sh"
echo ""
echo -e "${GREEN}Ready für DTN Compliance Platform! 🎉${NC}"