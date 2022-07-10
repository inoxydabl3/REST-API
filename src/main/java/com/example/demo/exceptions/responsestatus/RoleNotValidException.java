package com.example.demo.exceptions.responsestatus;

import com.example.demo.utils.Role;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

public class RoleNotValidException extends ResponseStatusException {

    public RoleNotValidException(String role) {
        super(HttpStatus.BAD_REQUEST, String.format("Not a valid role (%s). Possible values: %s", role,
                Arrays.toString(Role.values())));
    }

}
