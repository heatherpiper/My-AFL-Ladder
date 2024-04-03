package com.heatherpiper.controller;

import javax.validation.Valid;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.heatherpiper.exception.DaoException;
import com.heatherpiper.exception.UniqueConstraintViolationException;
import com.heatherpiper.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.heatherpiper.dao.UserDao;
import com.heatherpiper.security.jwt.JWTFilter;
import com.heatherpiper.security.jwt.TokenProvider;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public AuthenticationController(TokenProvider tokenProvider,
                                    AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao,
                                    GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    @PostMapping("/google-login")
    public ResponseEntity<LoginResponseDto> googleLogin(@RequestBody String idTokenString) {
        try {
            GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();

                // Check if the user exists in the database
                User user = userDao.getUserByEmail(email);

                // If user doesn't exist, generate a unique username and create a new user
                if (user == null) {
                    String username = generatedUniqueUsername(email);
                    user = new User();
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword("");
                    user.setActivated(true);
                    user.setAuthorities("ROLE_USER");
                    user = userDao.createGoogleUser(user);
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
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid ID token.");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google authentication failed.");
        }
    }

    private String generatedUniqueUsername(String email) {
        String prefix = email.split("@")[0];
        String suffix = "_" + generateRandomString();
        return prefix + suffix;
    }

    private String generateRandomString() {
        return UUID.randomUUID().toString().substring(0, 3);
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

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto newUser) {
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
}

