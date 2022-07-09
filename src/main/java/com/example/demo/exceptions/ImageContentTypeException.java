package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ImageContentTypeException extends ResponseStatusException {

    public ImageContentTypeException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public ImageContentTypeException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }

}
