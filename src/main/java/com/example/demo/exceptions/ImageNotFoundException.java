package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ImageNotFoundException extends ResponseStatusException {

    public ImageNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public ImageNotFoundException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }

}
