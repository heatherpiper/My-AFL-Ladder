package com.heatherpiper.security;

import com.heatherpiper.dao.UserDao;
import com.heatherpiper.model.Authority;
import com.heatherpiper.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserModelDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserModelDetailsService.class);

    private final UserDao userDao;

    public UserModelDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating user '{}'", login);
        String lowercaseLogin = login.toLowerCase();
        User user = getUserByUsernameOrEmail(lowercaseLogin);
        return createSpringSecurityUser(lowercaseLogin, user);
    }

    private User getUserByUsernameOrEmail(String lowercaseLogin) {
        User user = userDao.getUserByUsername(lowercaseLogin);
        if (user != null) {
            user = userDao.getUserByEmail(lowercaseLogin);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        }
        return user;
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<Authority> userAuthorities = user.getAuthorities();
        for (Authority authority : userAuthorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }

        String password = user.getPassword();
        if (password == null || password.isEmpty()) {
            password = "google-user-password";
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                password,
                grantedAuthorities);
    }
}

