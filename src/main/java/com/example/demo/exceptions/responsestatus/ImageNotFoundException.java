package com.example.demo.exceptions.responsestatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ImageNotFoundException extends ResponseStatusException {

    public ImageNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
