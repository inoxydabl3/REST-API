package com.example.demo.controllers.user;

import com.example.demo.dtos.UserCreationDTO;
import com.example.demo.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(path = "${app.usersEndpoint}")
public interface UserController {

    @GetMapping(path = "/{userId}")
    UserDTO getUser(@PathVariable int userId);

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    UserDTO createUser(@RequestBody UserCreationDTO user);

    @PatchMapping(path = "/{userId}")
    UserDTO updateUser(@PathVariable int userId, @RequestBody UserCreationDTO user);

    @DeleteMapping(path = "/{userId}")
    UserDTO deleteCustomer(@PathVariable int userId);

    @PatchMapping(path = "/{userId}/status/{status}")
    UserDTO changeAdminStatus(@PathVariable int userId, @PathVariable String status);
}
