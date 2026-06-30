/**
 * 
 */
package com.pms.security.configuration;

/**
 * 
 */
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // Multiple property files can be loaded
        messageSource.setBasenames(
            "classpath:messages",
            "classpath:errors",
            "classpath:labels"
        );
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600); // Reload every hour
        return messageSource;
    }
}

