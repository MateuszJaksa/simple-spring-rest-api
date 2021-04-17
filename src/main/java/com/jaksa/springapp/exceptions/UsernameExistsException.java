package com.jaksa.springapp.exceptions;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String username) {
        super("There is already an account with username " + username);
    }
}
