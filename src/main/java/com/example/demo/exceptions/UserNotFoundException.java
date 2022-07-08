package com.example.demo.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(int id) {
        super(String.format("Could not find user with id: %s", id));
    }

}
