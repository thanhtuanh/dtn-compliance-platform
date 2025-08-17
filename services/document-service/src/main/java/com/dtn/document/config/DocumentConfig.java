package com.dtn.document.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * DTN Document Service Configuration
 * 
 * Konfiguriert:
 * - FreeMarker Template Engine für deutsche DSGVO-Templates
 * - Swagger/OpenAPI für API-Dokumentation
 * - Async Processing für Performance
 * - WebClient für Service-Integration
 * 
 * Business Value:
 * - Template-basierte PDF-Generierung (15min statt 8h)
 * - Deutsche DSGVO-Compliance (Art. 30 & 35)
 * - API-First Design für Frontend-Integration
 * - Skalierbare Async-Verarbeitung
 * 
 * @author DTN Compliance Team
 * @version 1.0.0
 * @since 2025-08-17
 */
@org.springframework.context.annotation.Configuration
@EnableAsync
public class DocumentConfig {

    private static final Logger logger = LoggerFactory.getLogger(DocumentConfig.class);

    @Value("${dtn.document.templates.base-path:classpath:/templates/german/}")
    private String templatesBasePath;
    
    @Value("${dtn.document.performance.max-concurrent-generations:5}")
    private int maxConcurrentGenerations;
    
    @Value("${server.port:8082}")
    private String serverPort;

    /**
     * FreeMarker Configuration für deutsche Templates
     */
    @Bean
    @Primary
    public Configuration freemarkerConfiguration(ResourceLoader resourceLoader) {
        logger.info("Konfiguriere FreeMarker Template Engine für deutsche DSGVO-Templates");
        
        Configuration config = new Configuration(Configuration.VERSION_2_3_32);
        
        try {
            // Template Loader konfigurieren
            config.setClassLoaderForTemplateLoading(
                this.getClass().getClassLoader(), 
                "templates/german"
            );
            
            // Deutsche Lokalisierung
            config.setLocale(Locale.GERMANY);
            config.setDefaultEncoding("UTF-8");
            config.setOutputEncoding("UTF-8");
            
            // Template-Einstellungen für DSGVO-Compliance
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            config.setLogTemplateExceptions(true);
            config.setWrapUncheckedExceptions(true);
            config.setFallbackOnNullLoopVariable(false);
            
            // Deutsche Formatierung
            config.setDateFormat("dd.MM.yyyy");
            config.setTimeFormat("HH:mm:ss");
            config.setDateTimeFormat("dd.MM.yyyy HH:mm:ss");
            config.setNumberFormat("#,##0.##");
            config.setBooleanFormat("Ja,Nein");
            
            // Performance-Optimierungen
            config.setCacheStorage(new freemarker.cache.MruCacheStorage(100, 300));
            config.setTemplateUpdateDelayMilliseconds(5000);
            config.setWhitespaceStripping(true);
            
            // Deutsche Template-Makros aktivieren
            config.setSharedVariable("germanDateFormat", "dd.MM.yyyy");
            config.setSharedVariable("companyName", "DTN Compliance Solutions");
            config.setSharedVariable("currentYear", java.time.Year.now().getValue());
            
            logger.info("✅ FreeMarker konfiguriert: Deutsche Templates, UTF-8, DSGVO-optimiert");
            
            return config;
            
        } catch (Exception e) {
            logger.error("❌ Fehler bei FreeMarker-Konfiguration: {}", e.getMessage(), e);
            throw new RuntimeException("FreeMarker Template Engine konnte nicht konfiguriert werden", e);
        }
    }

    /**
     * WebClient für Service-zu-Service Kommunikation
     */
    @Bean
    public WebClient documentWebClient() {
        logger.info("Konfiguriere WebClient für Compliance Service Integration");
        
        return WebClient.builder()
                .codecs(configurer -> {
                    // 10MB Buffer für große Compliance-Daten
                    configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024);
                })
                .defaultHeader("User-Agent", "DTN-Document-Service/1.0.0")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    /**
     * Async Task Executor für PDF-Generierung
     */
    @Bean(name = "pdfGenerationExecutor")
    public Executor pdfGenerationExecutor() {
        logger.info("Konfiguriere Async Executor für PDF-Generierung (Max: {} concurrent)", 
                   maxConcurrentGenerations);
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Thread Pool Configuration
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(maxConcurrentGenerations);
        executor.setQueueCapacity(20);
        executor.setKeepAliveSeconds(60);
        
        // Thread Naming für besseres Debugging
        executor.setThreadNamePrefix("DTN-PDF-Gen-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        
        // Exception Handler
        executor.setRejectedExecutionHandler((task, taskExecutor) -> {
            logger.warn("PDF-Generierung abgelehnt - Task Queue voll. Aktuelle Pool-Größe: {}", 
                       taskExecutor.getActiveCount());
            throw new RuntimeException("PDF-Generierung momentan nicht verfügbar - Service überlastet");
        });
        
        executor.initialize();
        
        logger.info("✅ PDF Generation Executor konfiguriert: Core={}, Max={}, Queue={}", 
                   executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());
        
        return executor;
    }

    /**
     * OpenAPI/Swagger Konfiguration
     */
    @Bean
    public OpenAPI documentServiceOpenAPI() {
        logger.info("Konfiguriere OpenAPI/Swagger für Document Service API");
        
        return new OpenAPI()
                .info(new Info()
                        .title("DTN Document Service API")
                        .description("""
                            🚀 **PDF-Generierung für DSGVO & EU AI Act Compliance**
                            
                            ## Business Value
                            - ⚡ **8h → 15min**: Automatisierte Dokumentenerstellung  
                            - 🇩🇪 **Deutsche Templates**: DSGVO Art. 30 & 35 konform
                            - 📄 **Corporate Design**: Professionelle PDF-Layouts
                            - 🔗 **API-First**: Frontend & Mobile Integration
                            
                            ## Hauptfunktionen
                            - **VVT PDF**: Verzeichnis von Verarbeitungstätigkeiten
                            - **DSFA PDF**: Datenschutz-Folgenabschätzung  
                            - **Async Processing**: Skalierbare Performance
                            - **Health Monitoring**: Service-Überwachung
                            
                            ## Demo für Bewerbungen
                            - 🎪 `/demo` Endpoint für Live-Präsentationen
                            - 📊 Performance-Metriken für Technical Interviews
                            - 🎯 Business Case: 96.960€ Ersparnis/Jahr
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DTN Compliance Team")
                                .email("compliance@dtn-solutions.de")
                                .url("https://dtn-solutions.de"))
                        .license(new License()
                                .name("DTN Solutions Proprietary")
                                .url("https://dtn-solutions.de/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server"),
                        new Server()
                                .url("https://dtn-compliance.onrender.com")
                                .description("Live Demo Server (Bewerbungen)"),
                        new Server()
                                .url("http://document-service:8082")
                                .description("Docker Container Network")
                ));
    }

    /**
     * PDF-spezifische Bean-Konfiguration
     */
    @Bean
    public PDFSettings pdfSettings() {
        logger.info("Konfiguriere PDF-Einstellungen für Corporate Design");
        
        return PDFSettings.builder()
                .defaultFormat("A4")
                .marginTop("20mm")
                .marginBottom("20mm") 
                .marginLeft("20mm")
                .marginRight("20mm")
                .fontFamily("DejaVu Sans")
                .fontSize(10)
                .encoding("UTF-8")
                .compression(true)
                .qualityHigh(true)
                .watermarkEnabled(false)
                .corporateColorsEnabled(true)
                .primaryColor("#1f4e79")
                .secondaryColor("#6c757d")
                .build();
    }

    /**
     * Template-Cache-Konfiguration
     */
    @Bean
    public TemplateCacheConfig templateCacheConfig() {
        logger.info("Konfiguriere Template-Cache für Performance");
        
        return TemplateCacheConfig.builder()
                .cacheEnabled(true)
                .maxCacheSize(100)
                .cacheTtlSeconds(3600)
                .preloadTemplates(List.of("vvt-template.ftl", "dsfa-template.ftl"))
                .validateOnStartup(true)
                .build();
    }

    // ===== Inner Classes für Configuration =====

    /**
     * PDF-Einstellungen
     */
    public static class PDFSettings {
        private String defaultFormat;
        private String marginTop;
        private String marginBottom;
        private String marginLeft;
        private String marginRight;
        private String fontFamily;
        private int fontSize;
        private String encoding;
        private boolean compression;
        private boolean qualityHigh;
        private boolean watermarkEnabled;
        private boolean corporateColorsEnabled;
        private String primaryColor;
        private String secondaryColor;

        // Builder Pattern
        public static PDFSettingsBuilder builder() {
            return new PDFSettingsBuilder();
        }

        public static class PDFSettingsBuilder {
            private PDFSettings settings = new PDFSettings();

            public PDFSettingsBuilder defaultFormat(String format) {
                settings.defaultFormat = format;
                return this;
            }

            public PDFSettingsBuilder marginTop(String margin) {
                settings.marginTop = margin;
                return this;
            }

            public PDFSettingsBuilder marginBottom(String margin) {
                settings.marginBottom = margin;
                return this;
            }

            public PDFSettingsBuilder marginLeft(String margin) {
                settings.marginLeft = margin;
                return this;
            }

            public PDFSettingsBuilder marginRight(String margin) {
                settings.marginRight = margin;
                return this;
            }

            public PDFSettingsBuilder fontFamily(String font) {
                settings.fontFamily = font;
                return this;
            }

            public PDFSettingsBuilder fontSize(int size) {
                settings.fontSize = size;
                return this;
            }

            public PDFSettingsBuilder encoding(String enc) {
                settings.encoding = enc;
                return this;
            }

            public PDFSettingsBuilder compression(boolean comp) {
                settings.compression = comp;
                return this;
            }

            public PDFSettingsBuilder qualityHigh(boolean high) {
                settings.qualityHigh = high;
                return this;
            }

            public PDFSettingsBuilder watermarkEnabled(boolean enabled) {
                settings.watermarkEnabled = enabled;
                return this;
            }

            public PDFSettingsBuilder corporateColorsEnabled(boolean enabled) {
                settings.corporateColorsEnabled = enabled;
                return this;
            }

            public PDFSettingsBuilder primaryColor(String color) {
                settings.primaryColor = color;
                return this;
            }

            public PDFSettingsBuilder secondaryColor(String color) {
                settings.secondaryColor = color;
                return this;
            }

            public PDFSettings build() {
                return settings;
            }
        }

        // Getters
        public String getDefaultFormat() { return defaultFormat; }
        public String getMarginTop() { return marginTop; }
        public String getMarginBottom() { return marginBottom; }
        public String getMarginLeft() { return marginLeft; }
        public String getMarginRight() { return marginRight; }
        public String getFontFamily() { return fontFamily; }
        public int getFontSize() { return fontSize; }
        public String getEncoding() { return encoding; }
        public boolean isCompression() { return compression; }
        public boolean isQualityHigh() { return qualityHigh; }
        public boolean isWatermarkEnabled() { return watermarkEnabled; }
        public boolean isCorporateColorsEnabled() { return corporateColorsEnabled; }
        public String getPrimaryColor() { return primaryColor; }
        public String getSecondaryColor() { return secondaryColor; }
    }

    /**
     * Template-Cache-Konfiguration
     */
    public static class TemplateCacheConfig {
        private boolean cacheEnabled;
        private int maxCacheSize;
        private int cacheTtlSeconds;
        private List<String> preloadTemplates;
        private boolean validateOnStartup;

        // Builder Pattern
        public static TemplateCacheConfigBuilder builder() {
            return new TemplateCacheConfigBuilder();
        }

        public static class TemplateCacheConfigBuilder {
            private TemplateCacheConfig config = new TemplateCacheConfig();

            public TemplateCacheConfigBuilder cacheEnabled(boolean enabled) {
                config.cacheEnabled = enabled;
                return this;
            }

            public TemplateCacheConfigBuilder maxCacheSize(int size) {
                config.maxCacheSize = size;
                return this;
            }

            public TemplateCacheConfigBuilder cacheTtlSeconds(int ttl) {
                config.cacheTtlSeconds = ttl;
                return this;
            }

            public TemplateCacheConfigBuilder preloadTemplates(List<String> templates) {
                config.preloadTemplates = templates;
                return this;
            }

            public TemplateCacheConfigBuilder validateOnStartup(boolean validate) {
                config.validateOnStartup = validate;
                return this;
            }

            public TemplateCacheConfig build() {
                return config;
            }
        }

        // Getters
        public boolean isCacheEnabled() { return cacheEnabled; }
        public int getMaxCacheSize() { return maxCacheSize; }
        public int getCacheTtlSeconds() { return cacheTtlSeconds; }
        public List<String> getPreloadTemplates() { return preloadTemplates; }
        public boolean isValidateOnStartup() { return validateOnStartup; }
    }
}