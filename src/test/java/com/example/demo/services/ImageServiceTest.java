package com.example.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.exceptions.ImageStorageException;
import com.example.demo.services.image.ImageStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("test")
class ImageServiceTest {

	private static final String PHOTO_NAME = "photo.jpg";
	private static final String CONTENT_TYPE = "image/jpeg";

	private final ImageStorageService service;

	@AfterAll
	static void remove(@Value("${app.imagesPath}") Path image) {
		FileSystemUtils.deleteRecursively(image.toFile());
	}

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(service);
		log.info("Context have loaded successfully");
	}

	@Test
	void storeImage() throws IOException, ImageStorageException {
		MultipartFile photo = new MockMultipartFile(PHOTO_NAME, PHOTO_NAME, CONTENT_TYPE,
				Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(PHOTO_NAME)));
		String photoUrl = service.storeImage(photo);
		assertThat(photoUrl).isNotNull().isNotEmpty();
		log.info("Customer's photo uploaded successfully, url: {}", photoUrl);
	}

	@Test
	void loadImageAndDelete() throws IOException {
		Files.createDirectories(service.getImagesPath());
		Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(PHOTO_NAME)),
				service.getImagesPath().resolve(PHOTO_NAME));
		Optional<Resource> maybeResource = service.loadImageAsResource(PHOTO_NAME);
		assertThat(maybeResource).isPresent();
		log.info("Customer's photo loaded successfully");
		assertThat(service.deleteImage(PHOTO_NAME)).isTrue();
		log.info("Customer's photo deleted successfully");
	}

}
