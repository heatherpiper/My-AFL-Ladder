package com.heatherpiper.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.heatherpiper.controller.AuthenticationController;
import com.heatherpiper.dao.UserDao;
import com.heatherpiper.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

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
}
