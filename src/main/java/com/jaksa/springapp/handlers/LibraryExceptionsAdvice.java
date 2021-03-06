package com.jaksa.springapp.handlers;

import com.jaksa.springapp.exceptions.BookNotFoundException;
import com.jaksa.springapp.exceptions.UserNotFoundException;
import com.jaksa.springapp.exceptions.UsernameExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LibraryExceptionsAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    protected ResponseEntity<Object> bookNotFoundHandler(BookNotFoundException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> userNotFoundHandler(UserNotFoundException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(UsernameExistsException.class)
    protected ResponseEntity<Object> usernameTakenHandler(UsernameExistsException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
