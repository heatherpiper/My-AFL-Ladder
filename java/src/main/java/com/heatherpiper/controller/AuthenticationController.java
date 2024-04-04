package com.heatherpiper.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.heatherpiper.dao.UserDao;
import com.heatherpiper.exception.DaoException;
import com.heatherpiper.exception.UniqueConstraintViolationException;
import com.heatherpiper.model.LoginDto;
import com.heatherpiper.model.LoginResponseDto;
import com.heatherpiper.model.RegisterUserDto;
import com.heatherpiper.model.User;
import com.heatherpiper.security.jwt.JWTFilter;
import com.heatherpiper.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;

    @Autowired
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Autowired
    private RestTemplate restTemplate;

    public AuthenticationController(TokenProvider tokenProvider,
                                    AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao,
                                    GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);

        User user;
        try {
            user = userDao.getUserByUsername(loginDto.getUsername());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect.");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new LoginResponseDto(jwt, user), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/auth/google/exchange")
    public ResponseEntity<LoginResponseDto> exchangeAuthorizationCode(@RequestBody String authorizationCode) {
        try {
            String accessToken = exchangeAuthorizationCodeForAccessToken(authorizationCode);

            OAuth2User oAuth2User = retrieveUserInfo(accessToken);

            String email = oAuth2User.getAttribute("email");

            User user = userDao.getUserByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }

            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    null,
                    grantedAuthorities
            );

            String jwt = tokenProvider.createToken(authentication, false);

            LoginResponseDto responseDto = new LoginResponseDto(jwt, user);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google token exchange failed.");
        }
    }

    private String exchangeAuthorizationCodeForAccessToken(String authorizationCode) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new com.google.api.client.json.gson.GsonFactory(),
                    "https://oauth2.googleapis.com/token",
                    System.getenv("GOOGLE_CLIENT_ID"),
                    System.getenv("GOOGLE_CLIENT_SECRET"),
                    authorizationCode,
                    "https://later-ladder.onrender.com/auth/google/callback"
            ).execute();

            return tokenResponse.getAccessToken();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to exchange authorization code for " +
                    "access token.");
        }
    }

    private OAuth2User retrieveUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> userAttributes = response.getBody();
            if (userAttributes != null) {
                return new DefaultOAuth2User(Collections.emptyList(), userAttributes, "email");
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User info from Google is empty.");
            }
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to retrieve user info from Google.");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto newUser) {
        // Validate email format
        if (!isValidEmail(newUser.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        try {
            User user = userDao.createUser(newUser);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UniqueConstraintViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username is already taken.");
        } catch (DaoException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User registration failed due to an unexpected error.");
        }
    }

    @PostMapping("/auth/google")
    public void initializeGoogleSignIn() {
        // This is just a placeholder for the Google sign-in flow. The actual sign-in flow is handled by the frontend.
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}

