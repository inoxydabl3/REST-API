package com.example.demo.services.customer;

import com.example.demo.dtos.CustomerCreationDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.entities.CustomerEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.exceptions.ImageStorageException;
import com.example.demo.mappers.CustomerMapper;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.image.ImageStorageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final @NonNull ImageStorageService imageStorageService;

    private final @NonNull CustomerRepository customerRepository;
    private final @NonNull UserRepository userRepository;

    private final @NonNull CustomerMapper mapper;

    private final @NonNull String imageEndpoint;

    @Override
    public List<CustomerDTO> getAllCustomersSummary() {
        return customerRepository.findAllBy().stream()
                .map(mapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<CustomerDTO> getCustomer(int customerId) {
        return customerRepository.findById(customerId).map(e -> mapper.toDto(e, imageEndpoint));
    }

    @Override
    public CustomerDTO createCustomer(CustomerCreationDTO customerData) throws ImageStorageException {
        // Store photo
        String photo = null;
        if (customerData.getPhoto() != null) {
            photo = imageStorageService.storeImage(customerData.getPhoto());
        }
        // Find user ref
        UserEntity user = userRepository.findByUsername(customerData.getUserRef());
        // Create customer entity
        CustomerEntity customerEntity = mapper.toEntity(customerData, photo, user);
        return mapper.toDto(customerRepository.save(customerEntity), imageEndpoint);
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(int customerId, CustomerCreationDTO customerData)
            throws ImageStorageException {
        Optional<CustomerEntity> entityToUpdate = customerRepository.findById(customerId);
        if (entityToUpdate.isPresent()) {
            // Store photo
            String photo = null;
            if (customerData.getPhoto() != null) {
                photo = imageStorageService.storeImage(customerData.getPhoto());
            }
            // Find user ref
            UserEntity user = userRepository.findByUsername(customerData.getUserRef());
            entityToUpdate = Optional.of(customerRepository.save(mapper.updateEntity(
                    entityToUpdate.get(), customerData, photo, user)));
        }
        return entityToUpdate.map(e -> mapper.toDto(e, imageEndpoint));
    }

    @Override
    public Optional<CustomerDTO> deleteCustomer(int customerId) {
        Optional<CustomerEntity> entityToDelete = customerRepository.findById(customerId);
        if (entityToDelete.isPresent()) {
            customerRepository.deleteById(customerId);
        }
        return entityToDelete.map(e -> mapper.toDto(e, imageEndpoint));
    }

}
