package com.heatherpiper.security;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PasswordValidator implements Validator {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 30;

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Password cannot be empty.");
        String password = (String) target;

        if (password.length() < MIN_LENGTH) {
            errors.rejectValue("password", "password.length.short", "Password must be at least " + MIN_LENGTH + " characters long");
        }

        if (password.length() > MAX_LENGTH) {
            errors.rejectValue("password", "password.length.long", "Password must be no more than " + MAX_LENGTH + " characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            errors.rejectValue("password", "password.uppercase", "Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            errors.rejectValue("password", "password.lowercase", "Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            errors.rejectValue("password", "password.digit", "Password must contain at least one digit");
        }

        if (!password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?].*")) {
            errors.rejectValue("password", "password.specialChar", "Password must contain at least one special character");
        }
    }
}
