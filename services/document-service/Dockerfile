# Multi-stage build für DTN Document Service
# Stage 1: Build Stage mit Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Arbeitsverzeichnis setzen
WORKDIR /app

# Metadata für bessere Dokumentation
LABEL stage=build
LABEL service=dtn-document-service
LABEL version=1.0.0

# POM-Datei kopieren für Dependency-Caching
COPY pom.xml .

# Dependencies herunterladen (wird gecacht wenn pom.xml sich nicht ändert)
RUN mvn dependency:go-offline -B

# Source Code kopieren
COPY src ./src

# Application bauen
RUN mvn clean package -DskipTests -B

# Build-Informationen ausgeben
RUN echo "Build completed at $(date)" && \
    ls -la target/ && \
    echo "JAR size: $(du -h target/*.jar | cut -f1)"

# Stage 2: Runtime Stage
FROM eclipse-temurin:21-jre-alpine AS runtime

# Metadata für Production Image
LABEL maintainer="DTN Compliance Team"
LABEL service="dtn-document-service"
LABEL version="1.0.0"
LABEL description="PDF Generation Service für DSGVO VVT und DSFA"

# Non-root User erstellen für Sicherheit
RUN addgroup -g 1001 -S dtn && \
    adduser -u 1001 -S dtn -G dtn

# Arbeitsverzeichnis erstellen
WORKDIR /app

# System-Dependencies für PDF-Generation installieren
RUN apk add --no-cache \
    fontconfig \
    ttf-dejavu \
    ttf-liberation \
    curl \
    && rm -rf /var/cache/apk/*

# Schriftarten für deutsche Dokumente
RUN mkdir -p /usr/share/fonts/truetype/dejavu && \
    fc-cache -fv

# JAR-Datei aus Build Stage kopieren
COPY --from=build /app/target/*.jar app.jar

# Ownership auf dtn User setzen
RUN chown -R dtn:dtn /app

# Template-Verzeichnis erstellen
RUN mkdir -p /app/templates && \
    chown -R dtn:dtn /app/templates

# Health Check Script erstellen
RUN echo '#!/bin/sh' > /app/healthcheck.sh && \
    echo 'curl -f http://localhost:8082/actuator/health || exit 1' >> /app/healthcheck.sh && \
    chmod +x /app/healthcheck.sh && \
    chown dtn:dtn /app/healthcheck.sh

# Non-root User verwenden
USER dtn

# Port exposieren
EXPOSE 8082

# JVM-Optimierungen für Container
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+UnlockExperimentalVMOptions \
               -XX:+UseJVMCICompiler \
               -Djava.security.egd=file:/dev/./urandom \
               -Dfile.encoding=UTF-8 \
               -Duser.timezone=Europe/Berlin"

# Spring Profile für Docker setzen
ENV SPRING_PROFILES_ACTIVE=docker

# Health Check konfigurieren
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD /app/healthcheck.sh

# Application starten
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Build-Informationen als ENV verfügbar machen
ENV BUILD_DATE=""
ENV VERSION="1.0.0"
ENV SERVICE_NAME="dtn-document-service"

# Signal Handling für graceful shutdown
STOPSIGNAL SIGTERM

# Startup-Log
RUN echo "DTN Document Service Docker Image erstellt $(date)" > /tmp/build-info.txt