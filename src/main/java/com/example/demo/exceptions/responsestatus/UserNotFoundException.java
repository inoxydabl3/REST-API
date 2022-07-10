package com.example.demo.exceptions.responsestatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(int id) {
        super(HttpStatus.NOT_FOUND, String.format("Could not find user with id: %s", id));
    }

}
