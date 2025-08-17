# DTN Compliance Service - Docker Image
# DSGVO + EU AI Act konforme KI-Lösung für deutsche Unternehmen

# Multi-stage build für optimierte Image-Größe
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Build-Umgebung
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Maven Dependencies cachen
RUN mvn dependency:go-offline -B

# Application bauen (ohne Tests für schnelleren Build)
RUN mvn clean package -DskipTests -B

# Production Image
FROM eclipse-temurin:21-jre-alpine

# Labels für Container-Dokumentation
LABEL org.opencontainers.image.title="DTN Compliance Service"
LABEL org.opencontainers.image.description="DSGVO + EU AI Act konforme KI-Lösung"
LABEL org.opencontainers.image.vendor="DTN Software"
LABEL org.opencontainers.image.version="1.0.0"

# Non-root User für Security
RUN addgroup -g 1001 dtn && \
    adduser -D -u 1001 -G dtn dtn

# Working Directory
WORKDIR /app

# System Dependencies für Alpine
RUN apk add --no-cache \
    curl \
    tzdata \
    && rm -rf /var/cache/apk/*

# Timezone für deutsche Compliance
ENV TZ=Europe/Berlin

# JAR File kopieren
COPY --from=builder /app/target/compliance-service-*.jar app.jar

# Logs Directory
RUN mkdir -p /app/logs && \
    chown -R dtn:dtn /app

# User wechseln
USER dtn

# JVM Optimierung für Container
ENV JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health Check für Docker
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

# Port Exposition
EXPOSE 8081

# Application Start
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Demo-Ready Features:
# - Java 21 Latest LTS
# - Alpine Linux (Security + Performance)
# - Non-root User (Security Best Practice)
# - Health Check integriert
# - Multi-stage Build (Optimierte Image-Größe)
# - Deutsche Timezone für Compliance-Logs