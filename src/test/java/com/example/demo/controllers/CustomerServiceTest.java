package com.example.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.configurations.AppProperties;
import com.example.demo.controllers.customer.CustomerController;
import com.example.demo.dtos.CustomerDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.FileSystemUtils;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("test")
class CustomerServiceTest {

    private static final Random RND = new Random();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final int LOWER_BOUND = 5000;
    public static final int UPPER_BOUND = 10000;

    private static final String USER = "user";
    private static final String ADMIN = "admin";
    private static final String FOO = "foo";
    private static final String PASSWORD = "password";

    private static final int UPDATED_CUSTOMER = 1;
    private static final int DELETED_CUSTOMER = 2;

    private static final String PHOTO_NAME = "photo.jpg";
    private static final String JSON_NAME = "test.json";

    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String PHOTO = "photo";

    private static int generateRandomInt() {
        return RND.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
    }

    private static ObjectMapper objectMapper;

    private final AppProperties properties;

    private final MockMvc mockMvc;

    private final CustomerController controller;

    @BeforeAll
    static void init() {
        objectMapper = new ObjectMapper();
    }

    @AfterAll
    static void destroy(@Value("${app.imagesPath}") Path image) {
        FileSystemUtils.deleteRecursively(image.toFile());
    }

    @Test
    @Order(1)
    void contextLoads() {
        assertNotNull(controller);
        log.info("Context have loaded successfully");
    }

    @Test
    void getAllWithoutAuthentication() throws Exception {
        String url = properties.getCustomersEndpoint();
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void getAllUnauthorized() throws Exception {
        String url = properties.getCustomersEndpoint();
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void getAllOk() throws Exception {
        String url = properties.getCustomersEndpoint();
        MvcResult result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(USER, PASSWORD)))
                .andExpect(status().isOk()).andReturn();
        List<CustomerDTO> customers = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});
        log.info("Customers fetched successfully: {}", customers.size());
        customers.forEach(c -> log.info(String.valueOf(c)));
    }

    @Test
    void getWithoutAuthentication() throws Exception {
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(UPDATED_CUSTOMER));
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void getUnauthorized() throws Exception {
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(UPDATED_CUSTOMER));
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON)
                        .with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void getOk() throws Exception {
        int customerId = UPDATED_CUSTOMER;
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(customerId));
        MvcResult result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(USER, PASSWORD)))
                .andExpect(status().isOk()).andReturn();
        CustomerDTO customer = objectMapper.readValue(
                result.getResponse().getContentAsString(), CustomerDTO.class);
        assertThat(customer).isNotNull()
                .matches(c -> customerId == c.getId(), "customerId");
        log.info("Customer fetched successfully");
        log.info("{}", GSON.toJson(customer));
    }

    @Test
    void getNonExisting() throws Exception {
        int customerId = generateRandomInt();
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(customerId));
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value())).andReturn();
    }

    @Test
    void createWithoutAuthentication() throws Exception {
        String url = properties.getCustomersEndpoint();
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void createUnauthorized() throws Exception {
        String url = properties.getCustomersEndpoint();
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void createWithoutParams() throws Exception {
        String url = properties.getCustomersEndpoint();
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn();
    }

    @Test
    void createMissingParam() throws Exception {
        String name = "createWithoutPhoto";
        String url = properties.getCustomersEndpoint();
        mockMvc.perform(MockMvcRequestBuilders.multipart(URI.create(url))
                        .param(NAME, name)
                        .with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn();
    }

    @Test
    void createWithoutPhoto() throws Exception {
        String name = "createWithoutPhoto";
        String surname = "createWithoutPhoto";
        String user = USER;
        String url = properties.getCustomersEndpoint();
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.multipart(URI.create(url))
                                .param(NAME, name)
                                .param(SURNAME, surname)
                                .with(httpBasic(user, PASSWORD)))
                .andExpect(status().isCreated()).andReturn();
        CustomerDTO customer = objectMapper.readValue(
                result.getResponse().getContentAsString(), CustomerDTO.class);
        assertThat(customer).isNotNull()
                .matches(c -> name.equals(c.getName()), "name")
                .matches(c -> surname.equals(c.getSurname()), "surname")
                .matches(c -> user.equals(c.getUserRef()), "userRef")
                .matches(c -> c.getPhotoUrl() == null, "photo");
        log.info("Customer created successfully");
        log.info("{}", GSON.toJson(customer));
    }

    @Test
    void createWithPhotoButJSONFile() throws Exception {
        MockMultipartFile photo = new MockMultipartFile(PHOTO, JSON_NAME, MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(JSON_NAME)));
        String name = "createWithPhotoButJSONFile";
        String surname = "createWithPhotoButJSONFile";
        String url = properties.getCustomersEndpoint();
        mockMvc.perform(MockMvcRequestBuilders.multipart(URI.create(url))
                        .file(photo)
                        .param(NAME, name)
                        .param(SURNAME, surname)
                        .with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn();
    }

    @Test
    void createWithPhoto() throws Exception {
        MockMultipartFile photo = new MockMultipartFile(PHOTO, PHOTO_NAME, MediaType.IMAGE_JPEG_VALUE,
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(PHOTO_NAME)));
        String name = "createWithPhoto";
        String surname = "createWithPhoto";
        String url = properties.getCustomersEndpoint();
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.multipart(URI.create(url))
                                .file(photo)
                                .param(NAME, name)
                                .param(SURNAME, surname)
                                .with(httpBasic(USER, PASSWORD)))
                .andExpect(status().isCreated()).andReturn();
        CustomerDTO customer = objectMapper.readValue(
                result.getResponse().getContentAsString(), CustomerDTO.class);
        assertThat(customer).isNotNull()
                .matches(c -> name.equals(c.getName()), "name")
                .matches(c -> surname.equals(c.getSurname()), "surname")
                .matches(c -> USER.equals(c.getUserRef()), "userRef")
                .matches(c -> c.getPhotoUrl() != null, "photo");
        log.info("Customer created successfully");
        log.info("{}", GSON.toJson(customer));
    }

    @Test
    void updateWithoutAuthentication() throws Exception {
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(UPDATED_CUSTOMER));
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void updateUnauthorized() throws Exception {
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(UPDATED_CUSTOMER));
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON).with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void updatePartial() throws Exception {
        int customerId = UPDATED_CUSTOMER;
        String name = "updatePartial";
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(customerId));
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.multipart(HttpMethod.PATCH, URI.create(url))
                                .param(NAME, name)
                                .with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().isOk()).andReturn();
        CustomerDTO customer = objectMapper.readValue(
                result.getResponse().getContentAsString(), CustomerDTO.class);
        assertThat(customer)
                .matches(c -> customerId == c.getId(), "customerId")
                .matches(c -> name.equals(c.getName()), "name")
                .matches(c -> c.getSurname() != null, "surname")
                .matches(c -> ADMIN.equals(c.getUserRef()), "userRef");
        log.info("Customer updated successfully");
        log.info("{}", GSON.toJson(customer));
    }

    @Test
    void updateFullButJSONFile() throws Exception {
        MockMultipartFile photo = new MockMultipartFile(PHOTO, JSON_NAME, MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(JSON_NAME)));
        String name = "updateFullButJSONFile";
        String surname = "updateFullButJSONFile";
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(UPDATED_CUSTOMER));
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH, URI.create(url))
                        .file(photo)
                        .param(NAME, name)
                        .param(SURNAME, surname)
                        .with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn();
    }

    @Test
    void updateFull() throws Exception {
        int customerId = UPDATED_CUSTOMER;
        MockMultipartFile photo = new MockMultipartFile(PHOTO, PHOTO_NAME, MediaType.IMAGE_JPEG_VALUE,
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(PHOTO_NAME)));
        String name = "updateFull";
        String surname = "updateFull";
        String user = ADMIN;
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(customerId));
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.multipart(HttpMethod.PATCH, URI.create(url))
                                .file(photo)
                                .param(NAME, name)
                                .param(SURNAME, surname)
                                .with(httpBasic(user, PASSWORD)))
                .andExpect(status().isOk()).andReturn();
        CustomerDTO customer = objectMapper.readValue(
                result.getResponse().getContentAsString(), CustomerDTO.class);
        assertThat(customer)
                .matches(c -> customerId == c.getId(), "customerId")
                .matches(c -> name.equals(c.getName()), "name")
                .matches(c -> c.getSurname() != null, "surname")
                .matches(c -> user.equals(c.getUserRef()), "userRef");
        log.info("Customer updated successfully");
        log.info("{}", GSON.toJson(customer));
    }

    @Test
    void updateNonExisting() throws Exception {
        int customerId = generateRandomInt();
        String name = "updateNonExisting";
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(customerId));
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH, URI.create(url))
                        .param(NAME, name)
                        .with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value())).andReturn();
    }

    @Test
    void deleteWithoutAuthentication() throws Exception {
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(DELETED_CUSTOMER));
        mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void deleteUnauthorized() throws Exception {
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(DELETED_CUSTOMER));
        mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON)
                        .with(httpBasic(FOO, PASSWORD)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void deleteOk() throws Exception {
        int customerId = DELETED_CUSTOMER;
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(customerId));
        MvcResult result = mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON)
                        .with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().isOk()).andReturn();
        CustomerDTO customer = objectMapper.readValue(
                result.getResponse().getContentAsString(), CustomerDTO.class);
        assertThat(customer).isNotNull()
                .matches(c -> customerId == c.getId(), "customerId");
        log.info("Customer delete successfully");
        log.info("{}", GSON.toJson(customer));
    }

    @Test
    void deleteNonExisting() throws Exception {
        int customerId = generateRandomInt();
        String url = properties.getCustomersEndpoint().concat("/").concat(String.valueOf(customerId));
        mockMvc.perform(delete(url).accept(MediaType.APPLICATION_JSON)
                        .with(httpBasic(ADMIN, PASSWORD)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value())).andReturn();
    }

}
