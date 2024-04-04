package com.heatherpiper.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.util.Collections;

@Configuration
public class AppConfig {

    @Profile("!test")
    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        String googleClientId = System.getenv("GOOGLE_CLIENT_ID");
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
