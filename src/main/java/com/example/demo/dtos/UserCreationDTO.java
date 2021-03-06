package com.example.demo.dtos;

import com.example.demo.utils.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreationDTO implements Serializable {

    private String username;
    private String password;
    private Role role;

}
