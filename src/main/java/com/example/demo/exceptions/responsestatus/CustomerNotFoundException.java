package com.example.demo.exceptions.responsestatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomerNotFoundException extends ResponseStatusException {

    public CustomerNotFoundException(int id) {
        super(HttpStatus.NOT_FOUND, String.format("Could not find customer with id: %s", id));
    }

}
