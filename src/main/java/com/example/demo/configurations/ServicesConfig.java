package com.example.demo.configurations;

import com.example.demo.exceptions.ImageStorageException;
import com.example.demo.mappers.CustomerMapper;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.customer.CustomerService;
import com.example.demo.services.customer.CustomerServiceImpl;
import com.example.demo.services.image.ImageStorageService;
import com.example.demo.services.image.ImageStorageServiceImpl;
import com.example.demo.services.user.UserService;
import com.example.demo.services.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ServicesConfig {

    private final AppProperties properties;

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    @Bean
    public CustomerService customerService(ImageStorageService imageStorageService, CustomerMapper mapper) {
        return new CustomerServiceImpl(imageStorageService, customerRepository, userRepository, mapper,
                properties.getImagesEndpoint());
    }

    @Bean
    public UserService userService(PasswordEncoder passwordEncoder, UserMapper mapper) {
        return new UserServiceImpl(passwordEncoder, userRepository, roleRepository, mapper);
    }

    @Bean
    public ImageStorageService imageStorageService() throws ImageStorageException {
        return new ImageStorageServiceImpl(properties.getImagesPath());
    }

}
