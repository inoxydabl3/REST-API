package com.example.demo.services;

import com.example.demo.dtos.UserDTO;
import com.example.demo.utils.Role;

import java.util.Optional;

public interface UserSerivce {

    Optional<UserDTO> getUser(int userId);

    UserDTO createUser(UserDTO user);

    Optional<UserDTO> updateUser(int userId, UserDTO user);

    Optional<UserDTO> deleteUser(int userId);

    Optional<UserDTO> changeStats(int userId, Role role);

}
