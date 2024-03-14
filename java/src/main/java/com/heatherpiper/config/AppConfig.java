package com.heatherpiper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.http.HttpClient;

@Configuration
public class AppConfig {

    @Profile("!test")
    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
}
