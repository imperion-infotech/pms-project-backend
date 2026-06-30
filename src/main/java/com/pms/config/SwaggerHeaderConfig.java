package com.pms.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerHeaderConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .components(new Components()
                        .addParameters("BusinessTraceHeader",
                                new Parameter()
                                        .in("header")
                                        .name("X-Business-Trace-Id")
                                        .description("Business Trace Id for multi-step guest flow")
                                        .required(false)
                        ));
    }
}