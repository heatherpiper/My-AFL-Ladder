package com.heatherpiper.exception;

public class UserCreationException extends RuntimeException {
    public UserCreationException (String message) {
        super(message);
    }
}
