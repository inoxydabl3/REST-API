package com.example.demo.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(int id) {
        super(String.format("Could not find customer with id: %s", id));
    }

}
