package com.example.apodui.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UIConfig {
    @Bean
    public WebClient getWebClient(){
        return WebClient.create();
    }
}
