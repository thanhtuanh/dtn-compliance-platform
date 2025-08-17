package com.dtn.compliance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DTN Compliance API")
                        .version("1.0.0")
                        .description("DSGVO + EU AI Act konforme KI-Lösung für deutsche Unternehmen")
                        .contact(new Contact()
                                .name("Duc Thanh Nguyen")
                                .email("n.thanh@gmx.de")
                                .url("https://github.com/thanhtuanh")));
    }
}
