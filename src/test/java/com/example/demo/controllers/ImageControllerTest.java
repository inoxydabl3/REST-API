package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.configurations.AppProperties;
import com.example.demo.controllers.image.ImageController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("test")
class ImageControllerTest {

	private static final String PHOTO_NAME = "photo.jpg";
	private static final String FOO_NAME = "foo.jpg";

	private final ImageController controller;

	private final MockMvc mockMvc;

	private final AppProperties properties;

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
	void imageNotExists() throws Exception {
		String url = properties.getImagesEndpoint().concat("/").concat(FOO_NAME);
		mockMvc.perform(get(url)).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	void loadImageOk() throws Exception {
		Files.createDirectories(properties.getImagesPath());
		Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(PHOTO_NAME)),
				properties.getImagesPath().resolve(PHOTO_NAME));
		String url = properties.getImagesEndpoint().concat("/").concat(PHOTO_NAME);
		mockMvc.perform(get(url)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_JPEG));
		log.info("Customer's photo loaded successfully");
	}

}
