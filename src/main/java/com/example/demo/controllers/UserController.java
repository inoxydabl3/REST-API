package com.example.demo.controllers;

import com.example.demo.dtos.UserCreationDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.exceptions.responsestatus.MissingFieldException;
import com.example.demo.exceptions.responsestatus.RoleNotValidException;
import com.example.demo.exceptions.responsestatus.UserNotFoundException;
import com.example.demo.services.user.UserSerivce;
import com.example.demo.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${app.usersEndpoint}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserSerivce userSerivce;

    @GetMapping(path = "/{userId}")
    public UserDTO getUser(@PathVariable int userId) {
        return userSerivce.getUser(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody UserCreationDTO user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getRole() == null) {
            throw new MissingFieldException("Request params 'username','password' and 'role' are required.");
        }
        return userSerivce.createUser(user);
    }

    @PatchMapping(path = "/{userId}")
    public UserDTO updateUser(@PathVariable int userId, @RequestBody UserCreationDTO user) {
        return userSerivce.updateUser(userId, user).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @DeleteMapping(path = "/{userId}")
    public UserDTO deleteCustomer(@PathVariable int userId) {
        return userSerivce.deleteUser(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @PatchMapping(path = "/{userId}/status/{status}")
    public UserDTO changeAdminStatus(@PathVariable int userId, @PathVariable String status) {
        Role role;
        try {
            role = Role.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RoleNotValidException(status);
        }
        return userSerivce.changeStats(userId, role).orElseThrow(() -> new UserNotFoundException(userId));
    }

}
