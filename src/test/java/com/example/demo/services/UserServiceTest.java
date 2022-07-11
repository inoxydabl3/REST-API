package com.example.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.example.demo.dtos.UserCreationDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.services.user.UserService;
import com.example.demo.utils.Role;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Random;

@Slf4j
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("test")
class UserServiceTest {

    private static final Random RND = new Random();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final int LOWER_BOUND = 5000;
    public static final int UPPER_BOUND = 10000;

    private static final int UPDATED_USER = 5;
    private static final int DELETED_USER = 6;

    private static int generateRandomInt() {
        return RND.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
    }

    private final UserService service;
    private final PasswordEncoder passwordEncoder;

    @Test
    @Order(1)
    void contextLoads() {
        assertNotNull(service);
        assertNotNull(passwordEncoder);
        log.info("Context have loaded successfully");
    }

    @Test
    void get() {
        int userId = UPDATED_USER;
        Optional<UserDTO> user = service.getUser(userId);
        assertThat(user).isPresent();
        assertThat(user.get())
                .matches(u -> userId == u.getId(), "userId")
                .hasNoNullFieldsOrProperties();
        log.info("User fetched successfully");
        log.info("{}", GSON.toJson(user.get()));
    }

    @Test
    void getNonExisting() {
        int userId = generateRandomInt();
        Optional<UserDTO> user = service.getUser(userId);
        assertThat(user).isEmpty();
        log.info("User with id '{}' is not existing", userId);
    }

    @Test
    void create() {
        String username = "createUser";
        String password = "createUser";
        Role role = Role.USER;
        UserDTO user = service.createUser(UserCreationDTO.builder()
                .username(username)
                .password(password)
                .role(role)
                .build());
        assertThat(user).isNotNull().hasNoNullFieldsOrProperties()
                .matches(u -> username.equals(u.getUsername()), "username")
                .matches(u -> passwordEncoder.matches(password, u.getPassword()), "encoded password")
                .matches(u -> role.equals(u.getRole()), "role");
        log.info("User created successfully");
        log.info("{}", GSON.toJson(user));
    }

    @Test
    void updatePartial() {
        int userId = UPDATED_USER;
        String password = "updatePartial";
        Role role = Role.ADMIN;
        Optional<UserDTO> maybeUser = service.updateUser(userId, UserCreationDTO.builder()
                .password(password)
                .role(role)
                .build());
        assertThat(maybeUser).isPresent();
        UserDTO user = maybeUser.get();
        assertThat(user).hasNoNullFieldsOrProperties()
                .matches(u -> userId == u.getId(), "userId")
                .matches(u -> passwordEncoder.matches(password, u.getPassword()), "encoded password")
                .matches(u -> role.equals(u.getRole()), "role");
        log.info("User updated successfully");
        log.info("{}", GSON.toJson(user));
    }

    @Test
    void updateFull() {
        int userId = UPDATED_USER;
        String username = "updateFull";
        String password = "updateFull";
        Role role = Role.USER;
        Optional<UserDTO> maybeUser = service.updateUser(userId, UserCreationDTO.builder()
                .username(username)
                .password(password)
                .role(role)
                .build());
        assertThat(maybeUser).isPresent();
        UserDTO user = maybeUser.get();
        assertThat(user).isNotNull().hasNoNullFieldsOrProperties()
                .matches(u -> userId == u.getId(), "userId")
                .matches(u -> username.equals(u.getUsername()), "username")
                .matches(u -> passwordEncoder.matches(password, u.getPassword()), "encoded password")
                .matches(u -> role.equals(u.getRole()), "role");
        log.info("User updated successfully");
        log.info("{}", GSON.toJson(user));
    }

    @Test
    void updateNonExisting() {
        int userId = generateRandomInt();
        Optional<UserDTO> user = service.updateUser(userId, UserCreationDTO.builder()
                .role(Role.USER)
                .build());
        assertThat(user).isEmpty();
        log.info("User with id '{}' is not existing", userId);
    }

    @Test
    void delete() {
        int userId = DELETED_USER;
        Optional<UserDTO> user = service.deleteUser(userId);
        assertThat(user).isPresent();
        assertThat(user.get().getId()).isEqualTo(userId);
        log.info("User delete successfully");
        log.info("{}", GSON.toJson(user.get()));
    }

    @Test
    void deleteNonExisting() {
        int userId = generateRandomInt();
        Optional<UserDTO> user = service.deleteUser(userId);
        assertThat(user).isEmpty();
        log.info("User with id '{}' is not existing", userId);
    }

    @Test
    void changeStatus() {
        int userId = UPDATED_USER;
        Role role = Role.ADMIN;
        Optional<UserDTO> maybeUser = service.changeStats(userId, role);
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get())
                .matches(u -> userId == u.getId(), "userId")
                .matches(u -> role.equals(u.getRole()), "role");
        log.info("User status updated successfully");
        log.info("{}", GSON.toJson(maybeUser.get()));
    }

    @Test
    void changeStatusNonExisting() {
        int userId = generateRandomInt();
        Optional<UserDTO> user = service.changeStats(userId, Role.ADMIN);
        assertThat(user).isEmpty();
        log.info("User with id '{}' is not existing", userId);
    }

}
