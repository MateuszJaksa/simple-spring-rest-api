package com.jaksa.springapp.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Could not find user with a username: " + username);
    }
}
