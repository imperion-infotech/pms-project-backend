package com.pms.config;

import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerGlobalHeaderConfig {

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (operation, handlerMethod) -> {

            Parameter headerParameter = new Parameter()
                    .in("header")
                    .name("X-Business-Trace-Id")
                    .required(false)
                    .description("Business Trace Id for request tracking");

            operation.addParametersItem(headerParameter);

            return operation;
        };
    }
}