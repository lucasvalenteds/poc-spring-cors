package com.example.internal;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "service.cors")
@Getter
@Setter
public final class CrossOriginConfigurationProperties {

    private List<String> allowedUrls;

    private List<String> allowedMethods;

    private String pattern;
}
