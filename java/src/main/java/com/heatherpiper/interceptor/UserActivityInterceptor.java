package com.heatherpiper.interceptor;

import com.heatherpiper.dao.JdbcUserDao;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor to update the last active time of the user.
 */
@Component
public class UserActivityInterceptor extends HandlerInterceptorAdapter {

    private final JdbcUserDao userDao;

    public UserActivityInterceptor(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userDao.updateLastActive(userDetails.getUsername());
        }
        return true;
    }
}
