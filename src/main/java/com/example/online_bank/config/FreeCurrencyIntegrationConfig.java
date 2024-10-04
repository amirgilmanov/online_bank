package com.example.online_bank.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
@ConfigurationProperties("currency-integration")
public class FreeCurrencyIntegrationConfig {
    private String baseUrl;
    private String token;
    private String headerTokenName;
}
