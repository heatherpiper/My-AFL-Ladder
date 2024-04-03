package com.heatherpiper.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.heatherpiper.dao.UserDao;
import com.heatherpiper.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String idTokenString = userRequest.getAdditionalParameters().get("id_token").toString();

        try {
            GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                User user = userDao.getUserByEmail(email);

                if (user == null) {
                    user = new User();
                    user.setEmail(email);
                    user.setUsername(generateUniqueUsername(email));
                    user.setActivated(true);
                    user.setAuthorities("ROLE_USER");
                    userDao.createGoogleUser(user);
                }
                return oAuth2User;
            } else {
                throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Invalid ID token", null));
            }
        } catch (Exception e) {
            throw new OAuth2AuthenticationException(new OAuth2Error("authentication_failed", "Google authentication " +
                    "failed", null));
        }
    }

    private String generateUniqueUsername(String email) {
        String prefix = email.split("@")[0];
        String suffix = "_" + generateRandomString();
        return prefix + suffix;
    }

    private String generateRandomString() {
        return UUID.randomUUID().toString().substring(0, 3);
    }
}
