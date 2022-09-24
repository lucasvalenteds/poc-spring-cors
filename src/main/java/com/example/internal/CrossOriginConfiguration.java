package com.example.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CrossOriginConfiguration {

    @Bean
    public CorsFilter corsFilter(final CrossOriginConfigurationProperties properties) {
        final var config = new CorsConfiguration();
        config.setAllowedOrigins(properties.getAllowedUrls());
        config.setAllowedMethods(properties.getAllowedMethods());
        config.applyPermitDefaultValues();

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(properties.getPattern(), config);

        return new CorsFilter(source);
    }
}
