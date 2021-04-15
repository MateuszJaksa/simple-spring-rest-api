package com.jaksa.springapp.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Could not find employee " + id);
    }

    public BookNotFoundException(String title) {
        super("Could not find employee " + title);
    }
}
