package com.example.demo.services;

import com.example.demo.dtos.UserDTO;

import java.util.Optional;

public interface UserSerivce {

    Optional<UserDTO> getUser(int userId);

    UserDTO createUser(UserDTO user);

}
