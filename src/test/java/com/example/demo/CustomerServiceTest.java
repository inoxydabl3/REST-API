package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.dtos.CustomerCreationDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.ImageStorageException;
import com.example.demo.services.customer.CustomerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Slf4j
@SpringBootTest
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

	private static final int UPDATED_CUSTOMER = 1;
	private static final int DELETED_CUSTOMER = 2;

	private static final String PHOTO_NAME = "photo.jpg";
	private static final String CONTENT_TYPE = "image/jpeg";

	private static int generateRandomInt() {
		return RND.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
	}

	private final CustomerService service;

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
	void getAll() {
		List<CustomerDTO> customers = service.getAllCustomersSummary();
		assertThat(customers).isNotNull().isNotEmpty();
		log.info("Customers fetched successfully: {}", customers.size());
		customers.forEach(c -> log.info(String.valueOf(c)));
	}

	@Test
	void get() {
		int customerId = UPDATED_CUSTOMER;
		Optional<CustomerDTO> customer = service.getCustomer(customerId);
		assertThat(customer).isPresent();
		assertThat(customer.get())
				.matches(c -> customerId == c.getId(), "customerId");
		log.info("Customer fetched successfully");
		log.info("{}", GSON.toJson(customer.get()));
	}

	@Test
	void getNonExisting() {
		int customerId = generateRandomInt();
		Optional<CustomerDTO> customer = service.getCustomer(customerId);
		assertThat(customer).isEmpty();
		log.info("Customer with id '{}' is not existing", customerId);
	}

	@Test
	void createWithoutPhoto() throws ImageStorageException {
		String name = "createWithoutPhoto";
		String surname = "createWithoutPhoto";
		String userRef = USER;
		CustomerDTO customer = service.createCustomer(CustomerCreationDTO.builder()
				.name(name)
				.surname(surname)
				.userRef(userRef)
				.build());
		assertThat(customer).isNotNull()
				.matches(c -> name.equals(c.getName()), "name")
				.matches(c -> surname.equals(c.getSurname()), "surname")
				.matches(c -> userRef.equals(c.getUserRef()), "userRef")
				.matches(c -> c.getPhotoUrl() == null, "photo");
		log.info("Customer created successfully");
		log.info("{}", GSON.toJson(customer));
	}

	@Test
	void createWithPhoto() throws ImageStorageException, IOException {
		MultipartFile photo = new MockMultipartFile(PHOTO_NAME, PHOTO_NAME, CONTENT_TYPE,
				Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(PHOTO_NAME)));
		String name = "createWithPhoto";
		String surname = "createWithPhoto";
		String userRef = USER;
		CustomerDTO customer = service.createCustomer(CustomerCreationDTO.builder()
				.name(name)
				.surname(surname)
				.userRef(userRef)
				.photo(photo)
				.build());
		assertThat(customer).isNotNull()
				.matches(c -> name.equals(c.getName()), "name")
				.matches(c -> surname.equals(c.getSurname()), "surname")
				.matches(c -> userRef.equals(c.getUserRef()), "userRef")
				.matches(c -> c.getPhotoUrl() != null, "photo");
		log.info("Customer created successfully");
		log.info("{}", GSON.toJson(customer));
	}

	@Test
	void updatePartial() throws ImageStorageException {
		int customerId = UPDATED_CUSTOMER;
		String name = "updatePartial";
		String userRef = ADMIN;
		Optional<CustomerDTO> maybeCustomer = service.updateCustomer(customerId, CustomerCreationDTO.builder()
				.name(name)
				.userRef(userRef)
				.build());
		assertThat(maybeCustomer).isPresent();
		CustomerDTO customer = maybeCustomer.get();
		assertThat(customer)
				.matches(c -> customerId == c.getId(), "customerId")
				.matches(c -> name.equals(c.getName()), "name")
				.matches(c -> c.getSurname() != null, "surname")
				.matches(c -> userRef.equals(c.getUserRef()), "userRef");
		log.info("Customer updated successfully");
		log.info("{}", GSON.toJson(customer));
	}

	@Test
	void updateFull() throws IOException, ImageStorageException {
		MultipartFile photo = new MockMultipartFile(PHOTO_NAME, PHOTO_NAME, CONTENT_TYPE,
				Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(PHOTO_NAME)));
		int customerId = UPDATED_CUSTOMER;
		String name = "updateFull";
		String surname = "updateFull";
		String userRef = USER;
		Optional<CustomerDTO> maybeCustomer = service.updateCustomer(customerId, CustomerCreationDTO.builder()
				.name(name)
				.surname(surname)
				.userRef(userRef)
				.photo(photo)
				.build());
		assertThat(maybeCustomer).isPresent();
		CustomerDTO customer = maybeCustomer.get();
		assertThat(customer)
				.matches(c -> customerId == c.getId(), "customerId")
				.matches(c -> name.equals(c.getName()), "name")
				.matches(c -> surname.equals(c.getSurname()), "surname")
				.matches(c -> userRef.equals(c.getUserRef()), "userRef")
				.matches(c -> c.getPhotoUrl() != null, "photo");
		log.info("Customer updated successfully");
		log.info("{}", GSON.toJson(customer));
	}

	@Test
	void updateNonExisting() throws ImageStorageException {
		int customerId = generateRandomInt();
		Optional<CustomerDTO> customer = service.updateCustomer(customerId, CustomerCreationDTO.builder()
				.name("updateNonExisting")
				.userRef(ADMIN)
				.build());
		assertThat(customer).isEmpty();
		log.info("Customer with id '{}' is not existing", customerId);
	}

	@Test
	void delete() {
		int customerId = DELETED_CUSTOMER;
		Optional<CustomerDTO> customer = service.deleteCustomer(customerId);
		assertThat(customer).isPresent();
		assertThat(customer.get().getId()).isEqualTo(customerId);
		log.info("Customer delete successfully");
		log.info("{}", GSON.toJson(customer.get()));
	}

	@Test
	void deleteNonExisting() {
		int customerId = generateRandomInt();
		Optional<CustomerDTO> customer = service.deleteCustomer(customerId);
		assertThat(customer).isEmpty();
		log.info("Customer with id '{}' is not existing", customerId);
	}

}
