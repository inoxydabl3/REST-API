package com.example.demo.services.user;

import com.example.demo.dtos.UserCreationDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.utils.Role;

import java.util.Optional;

public interface UserSerivce {

    Optional<UserDTO> getUser(int userId);

    UserDTO createUser(UserCreationDTO user);

    Optional<UserDTO> updateUser(int userId, UserCreationDTO user);

    Optional<UserDTO> deleteUser(int userId);

    Optional<UserDTO> changeStats(int userId, Role role);

}
