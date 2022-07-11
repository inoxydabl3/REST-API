package com.example.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.configurations.AppProperties;
import com.example.demo.controllers.user.UserController;
import com.example.demo.dtos.UserCreationDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.utils.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("test")
class UserControllerTest {

    private static final Random RND = new Random();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final int LOWER_BOUND = 5000;
    public static final int UPPER_BOUND = 10000;

    private static final String USER = "user";
    private static final String ADMIN = "admin";
    private static final String FOO = "foo";
    private static final String PASSWORD = "password";

    private static final int UPDATED_USER = 3;
    private static final int DELETED_USER = 4;

    private static int generateRandomInt() {
        return RND.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
    }

    private static ObjectMapper objectMapper;

    private final AppProperties properties;

    private final MockMvc mockMvc;

    private final UserController controller;
    private final PasswordEncoder passwordEncoder;

    @BeforeAll
    static void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void contextLoads() {
        assertNotNull(controller);
        assertNotNull(passwordEncoder);
        log.info("Context have loaded successfully");
    }

    @Test
    void getWithoutAuthentication() throws Exception {
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(UPDATED_USER));
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void getUnauthorized() throws Exception {
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(UPDATED_USER));
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void getForbidden() throws Exception {
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(UPDATED_USER));
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void getOk() throws Exception {
        int userId = UPDATED_USER;
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(userId));
        MvcResult result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().isOk()).andReturn();
        UserDTO user = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertThat(user).isNotNull()
                .matches(u -> userId == u.getId(), "userId")
                .hasNoNullFieldsOrProperties();
        log.info("User fetched successfully");
        log.info("{}", GSON.toJson(user));
    }


    @Test
    void getNonExisting() throws Exception {
        int userId = generateRandomInt();
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(userId));
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void createWithoutAuthentication() throws Exception {
        String url = properties.getUsersEndpoint();
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void createUnauthorized() throws Exception {
        String url = properties.getUsersEndpoint();
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void createForbidden() throws Exception {
        String url = properties.getUsersEndpoint();
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void createWithoutParams() throws Exception {
        String url = properties.getUsersEndpoint();
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void createMissingParam() throws Exception {
        UserCreationDTO creation = UserCreationDTO.builder()
                .username("createMissingParam")
                .build();
        String url = properties.getUsersEndpoint();
        mockMvc.perform(post(url).with(httpBasic(ADMIN, PASSWORD)).content(GSON.toJson(creation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void createOk() throws Exception {
        UserCreationDTO creation = UserCreationDTO.builder()
                .username("createOk")
                .password("createOk")
                .role(Role.USER)
                .build();
        String url = properties.getUsersEndpoint();
        MvcResult result = mockMvc.perform(post(url).with(httpBasic(ADMIN, PASSWORD)).content(GSON.toJson(creation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        UserDTO user = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertThat(user).isNotNull().hasNoNullFieldsOrProperties()
                .matches(u -> creation.getUsername().equals(u.getUsername()), "username")
                .matches(u -> passwordEncoder.matches(creation.getPassword(), u.getPassword()), "encoded password")
                .matches(u -> creation.getRole().equals(u.getRole()), "role");
        log.info("User created successfully");
        log.info("{}", GSON.toJson(user));
    }

    @Test
    void updateWithoutAuthentication() throws Exception {
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(UPDATED_USER));
        mockMvc.perform(patch(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void updateUnauthorized() throws Exception {
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(UPDATED_USER));
        mockMvc.perform(patch(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void updateForbidden() throws Exception {
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(UPDATED_USER));
        mockMvc.perform(patch(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void updatePartial() throws Exception {
        int userId = UPDATED_USER;
        UserCreationDTO creation = UserCreationDTO.builder()
                .password("updatePartial")
                .role(Role.USER)
                .build();
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(userId));
        MvcResult result = mockMvc.perform(patch(url).with(httpBasic(ADMIN, PASSWORD)).content(GSON.toJson(creation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        UserDTO user = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertThat(user).hasNoNullFieldsOrProperties()
                .matches(u -> userId == u.getId(), "userId")
                .matches(u -> passwordEncoder.matches(creation.getPassword(), u.getPassword()), "encoded password")
                .matches(u -> creation.getRole().equals(u.getRole()), "role");
        log.info("User updated successfully");
        log.info("{}", GSON.toJson(user));
    }

    @Test
    void updateFull() throws Exception {
        int userId = UPDATED_USER;
        UserCreationDTO creation = UserCreationDTO.builder()
                .username("updateFull")
                .password("updateFull")
                .role(Role.USER)
                .build();
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(userId));
        MvcResult result = mockMvc.perform(patch(url).with(httpBasic(ADMIN, PASSWORD)).content(GSON.toJson(creation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        UserDTO user = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertThat(user).isNotNull().hasNoNullFieldsOrProperties()
                .matches(u -> userId == u.getId(), "userId")
                .matches(u -> creation.getUsername().equals(u.getUsername()), "username")
                .matches(u -> passwordEncoder.matches(creation.getPassword(), u.getPassword()), "encoded password")
                .matches(u -> creation.getRole().equals(u.getRole()), "role");
        log.info("User updated successfully");
        log.info("{}", GSON.toJson(user));
    }

    @Test
    void updateNonExisting() throws Exception {
        int userId = generateRandomInt();
        UserCreationDTO creation = UserCreationDTO.builder()
                .password("updateNonExisting")
                .role(Role.USER)
                .build();
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(userId));
        mockMvc.perform(patch(url).with(httpBasic(ADMIN, PASSWORD)).content(GSON.toJson(creation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void deleteWithoutAuthentication() throws Exception {
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(DELETED_USER));
        mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void deleteUnauthorized() throws Exception {
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(DELETED_USER));
        mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void deleteForbidden() throws Exception {
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(DELETED_USER));
        mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void deleteOk() throws Exception {
        int userId = DELETED_USER;
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(userId));
        MvcResult result = mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON)
                        .with(httpBasic(ADMIN, PASSWORD))).andExpect(status().isOk()).andReturn();
        UserDTO user = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertThat(user).isNotNull()
                .matches(u -> userId == u.getId(), "userId")
                .hasNoNullFieldsOrProperties();
        log.info("User delete successfully");
        log.info("{}", GSON.toJson(user));
    }

    @Test
    void deleteNonExisting() throws Exception {
        int userId = generateRandomInt();
        String url = properties.getUsersEndpoint().concat("/").concat(String.valueOf(userId));
        mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void changeStatusWithoutAuthentication() throws Exception {
        Role role = Role.ADMIN;
        String url = properties.getUsersEndpoint() + "/" + UPDATED_USER + "/status/" + role;
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void changeStatusUnauthorized() throws Exception {
        Role role = Role.ADMIN;
        String url = properties.getUsersEndpoint() + "/" + UPDATED_USER + "/status/" + role;
        mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void changeStatusForbidden() throws Exception {
        Role role = Role.ADMIN;
        String url = properties.getUsersEndpoint() + "/" + UPDATED_USER + "/status/" + role;
        mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void changeStatusNotValid() throws Exception {
        int userId = generateRandomInt();
        String url = properties.getUsersEndpoint() + "/" + userId + "/status/" + FOO;
        mockMvc.perform(patch(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void changeStatus() throws Exception {
        int userId = UPDATED_USER;
        Role role = Role.ADMIN;
        String url = properties.getUsersEndpoint() + "/" + userId + "/status/" + role;
        MvcResult result = mockMvc.perform(patch(url).accept(MediaType.APPLICATION_JSON)
                        .with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().isOk()).andReturn();
        UserDTO user = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertThat(user)
                .matches(u -> userId == u.getId(), "userId")
                .matches(u -> role.equals(u.getRole()), "role");
        log.info("User status updated successfully");
        log.info("{}", GSON.toJson(user));
    }

    @Test
    void changeStatusNonExisting() throws Exception {
        int userId = generateRandomInt();
        Role role = Role.ADMIN;
        String url = properties.getUsersEndpoint() + "/" + userId + "/status/" + role;
        mockMvc.perform(patch(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

}
