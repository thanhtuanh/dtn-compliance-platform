package com.dtn.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class GatewayApplication {

    private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

    public static void main(String[] args) {
        System.out.println("🚀 DTN Gateway Service wird gestartet...");
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeader("User-Agent", "DTN-Gateway/1.0.0");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();
        String port = env.getProperty("server.port", "8080");
        
        logger.info("🚀 DTN Gateway Service erfolgreich gestartet!");
        logger.info("🌐 Gateway URL: http://localhost:{}", port);
        logger.info("💚 Health Check: http://localhost:{}/actuator/health", port);
        
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  🎯 DTN Gateway Service BEREIT!                               ║");
        System.out.println("║  🌐 Gateway: http://localhost:" + port + "                        ║");
        System.out.println("║  💚 Health: http://localhost:" + port + "/actuator/health         ║");
        System.out.println("║  ✅ WebFlux Reactive Stack                                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    @EventListener
    public void onShutdown(org.springframework.context.event.ContextClosedEvent event) {
        logger.info("🛑 DTN Gateway Service wird heruntergefahren...");
    }
}
