package com.example.demo.dtos;

import com.example.demo.utils.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDTO implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private Role role;

}
