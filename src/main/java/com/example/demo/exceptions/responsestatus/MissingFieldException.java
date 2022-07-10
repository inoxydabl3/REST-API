package com.example.demo.exceptions.responsestatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MissingFieldException extends ResponseStatusException {

    public MissingFieldException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
