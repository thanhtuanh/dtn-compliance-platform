# DTN Compliance Service Dockerfile
# Java 21 + Spring Boot optimiert für Render.com und Docker Compose

# Build Stage
FROM eclipse-temurin:21-jdk-alpine AS build

# Metadata
LABEL maintainer="DTN Compliance Platform"
LABEL description="DSGVO + EU AI Act Compliance Service"
LABEL version="1.0.0"

# Install Maven
RUN apk add --no-cache maven

# Working directory
WORKDIR /app

# Copy Maven configuration
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies (for Docker layer caching)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application (skip tests for faster build)
RUN mvn clean package -DskipTests -Ddependency-check.skip=true

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine AS runtime

# Install additional packages for production
RUN apk add --no-cache \
    curl \
    wget \
    tzdata \
    && cp /usr/share/zoneinfo/Europe/Berlin /etc/localtime \
    && echo "Europe/Berlin" > /etc/timezone

# Create app user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Working directory
WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/compliance-service-*.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs && \
    chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

# Expose port
EXPOSE 8081

# Environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Start application
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]