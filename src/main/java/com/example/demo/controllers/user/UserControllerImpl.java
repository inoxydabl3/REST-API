package com.example.demo.controllers.user;

import com.example.demo.dtos.UserCreationDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.exceptions.responsestatus.MissingFieldException;
import com.example.demo.exceptions.responsestatus.RoleNotValidException;
import com.example.demo.exceptions.responsestatus.UserNotFoundException;
import com.example.demo.services.user.UserService;
import com.example.demo.utils.Role;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final @NonNull UserService userService;

    @Override
    public UserDTO getUser(int userId) {
        return userService.getUser(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public UserDTO createUser(UserCreationDTO user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getRole() == null) {
            throw new MissingFieldException("Request params 'username','password' and 'role' are required.");
        }
        return userService.createUser(user);
    }

    @Override
    public UserDTO updateUser(int userId, UserCreationDTO user) {
        return userService.updateUser(userId, user).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public UserDTO deleteCustomer(int userId) {
        return userService.deleteUser(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public UserDTO changeAdminStatus(int userId, String status) {
        Role role;
        try {
            role = Role.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RoleNotValidException(status);
        }
        return userService.changeStats(userId, role).orElseThrow(() -> new UserNotFoundException(userId));
    }

}
