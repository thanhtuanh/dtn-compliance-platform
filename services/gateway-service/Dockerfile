# DTN Compliance Platform - Gateway Service Dockerfile
# Optimiert für:
# - Render.com Deployment (Live-Demo)
# - Lokale Development (Docker Compose)
# - Production-Ready Setup

# Build Stage - Maven Compilation
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build

# Metadata für Container Registry
LABEL maintainer="Duc Thanh Nguyen <n.thanh@gmx.de>"
LABEL description="DTN Compliance Gateway - DSGVO + EU AI Act konforme KI-Lösung"
LABEL version="1.0.0"

# Arbeitsverzeichnis für Build
WORKDIR /build

# Maven Dependencies Cache (bessere Build-Performance)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Source Code kopieren und kompilieren
COPY src ./src
RUN mvn clean package -DskipTests=true -B

# Runtime Stage - Production Container
FROM eclipse-temurin:21-jre-alpine

# System Updates und Tools für Production
RUN apk add --no-cache \
    curl \
    tzdata \
    && rm -rf /var/cache/apk/*

# Timezone für deutsche Compliance (DSGVO)
ENV TZ=Europe/Berlin
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# DTN Application User (Security Best Practice)
RUN addgroup -g 1001 dtn && \
    adduser -D -s /bin/sh -u 1001 -G dtn dtn

# Application Directory
WORKDIR /app

# Build Informationen für Bewerbungen (vor User Switch)
RUN echo "DTN Compliance Gateway v1.0.0" > /tmp/version.txt && \
    echo "Build: $(date)" >> /tmp/version.txt

# Startup Banner für Demo-Präsentationen (vor User Switch)
RUN echo "DTN Compliance Intelligence Platform" > /tmp/startup-banner.txt && \
    echo "Gateway Service erfolgreich gestartet!" >> /tmp/startup-banner.txt && \
    echo "" >> /tmp/startup-banner.txt && \
    echo "Business Value: 96.960 EUR Jahresersparnis" >> /tmp/startup-banner.txt && \
    echo "Zielgruppe: Deutsche Software-Dienstleister (50-200 MA)" >> /tmp/startup-banner.txt && \
    echo "Compliance: DSGVO + EU AI Act konform" >> /tmp/startup-banner.txt && \
    echo "" >> /tmp/startup-banner.txt && \
    echo "Demo URLs:" >> /tmp/startup-banner.txt && \
    echo "  Swagger UI: http://localhost:8080/swagger-ui/" >> /tmp/startup-banner.txt && \
    echo "  Health Check: http://localhost:8080/actuator/health" >> /tmp/startup-banner.txt && \
    echo "  Gateway Status: http://localhost:8080/api/v1/gateway/status" >> /tmp/startup-banner.txt && \
    echo "" >> /tmp/startup-banner.txt && \
    echo "Entwickelt fuer Bewerbungsgespraeche und Live-Demos" >> /tmp/startup-banner.txt && \
    echo "Duc Thanh Nguyen | Muenchen/Bayern" >> /tmp/startup-banner.txt

# JAR File aus Build Stage kopieren
COPY --from=build /build/target/gateway-service-*.jar app.jar

# Dateien kopieren und Ownership für dtn user setzen
RUN cp /tmp/version.txt /app/version.txt && \
    cp /tmp/startup-banner.txt /app/startup-banner.txt && \
    chown -R dtn:dtn /app && \
    rm /tmp/version.txt /tmp/startup-banner.txt

# Switch zu non-root user
USER dtn

# Health Check für Container Orchestration
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Port für Gateway Service
EXPOSE 8080

# Environment Variables für Render.com
ENV SPRING_PROFILES_ACTIVE=production
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Startup Command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]