package com.example.demo.exceptions;

import com.example.demo.utils.Role;

import java.util.Arrays;

public class RoleNotValidException extends RuntimeException {

    public RoleNotValidException(String role) {
        super(String.format("Not a valid role (%s). Possible values: %s", role, Arrays.toString(Role.values())));
    }

}
