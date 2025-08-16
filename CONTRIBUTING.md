# DTN Compliance Platform - Development Guidelines

## 🎯 Projekt-Ziel
DSGVO + EU AI Act konforme KI-Lösung für deutsche Software-Dienstleister (50-200 MA)

## 🏗️ Architektur-Prinzipien

### 1. Privacy by Design
- Alle KI-Verarbeitung lokal (Ollama)
- Datenminimierung vor Verarbeitung
- Pseudonymisierung standardmäßig
- Audit-Logs für DSGVO-Nachweis

### 2. Deutsche Rechtssicherheit
- DSGVO Art. 30 (VVT) Compliance
- DSGVO Art. 35 (DSFA) Automatisierung
- EU AI Act Risikoklassifizierung
- BfDI + Landesdatenschutzbehörden konform

### 3. Enterprise-Patterns
- Clean Architecture (Controller → Service → Repository)
- SOLID Principles
- Dependency Injection
- Exception Handling

## 📝 Code-Standards

### Java Coding Standards
- Java 21 Features nutzen (Virtual Threads, Records, Pattern Matching)
- Spring Boot Best Practices
- Lombok für Boilerplate-Reduktion
- Comprehensive Javadoc (deutsch)

### Testing Standards
- Unit Tests: JUnit 5 + Mockito
- Integration Tests: TestContainers + TestRestTemplate
- API Tests: RestAssured
- Coverage: >80%

### Documentation Standards
- README.md: Business Case + Setup
- API Documentation: OpenAPI 3 + Swagger
- Architecture Decision Records (ADRs)
- Deutsche Kommentare für Compliance-Code

## 🚀 Development Workflow

### 1. Local Development
```bash
# Setup
git clone <repository>
cd dtn-compliance-platform
docker-compose up -d

# Development
mvn clean install
mvn spring-boot:run

# Testing
mvn test
mvn integration-test