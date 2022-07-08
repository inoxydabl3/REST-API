package com.example.demo.exceptions;

import com.example.demo.dtos.UserDTO;
import lombok.Getter;

public class MissingUserFieldException extends RuntimeException {

    @Getter
    private final UserDTO user;

    public MissingUserFieldException(String message, UserDTO user) {
        super(message);
        this.user = user;
    }

}
