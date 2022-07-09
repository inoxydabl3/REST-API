package com.example.demo.services;

import com.example.demo.dtos.CustomerDTO;
import com.example.demo.entities.CustomerEntity;
import com.example.demo.mappers.CustomerMapper;
import com.example.demo.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper mapper;

    @Override
    public List<CustomerDTO> getAllCustomersSummary() {
        return customerRepository.findAllBy().stream()
                .map(mapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<CustomerDTO> getCustomer(int customerId) {
        return customerRepository.findById(customerId).map(mapper::toDto);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
        // TODO: The customer should have a reference to the user who created it.
        // TODO: Image uploads should be able to be managed.
        return mapper.toDto(customerRepository.save(mapper.toEntity(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(int customerId, CustomerDTO newCustomerData) {
        // TODO: Image uploads should be able to be managed.
        Optional<CustomerEntity> entityToUpdate = customerRepository.findById(customerId);
        if (entityToUpdate.isPresent()) {
            entityToUpdate = Optional.of(customerRepository.save(
                    mapper.update(entityToUpdate.get(), newCustomerData)));
        }
        return entityToUpdate.map(mapper::toDto);
    }

    @Override
    public Optional<CustomerDTO> deleteCustomer(int customerId) {
        Optional<CustomerEntity> entityToDelete = customerRepository.findById(customerId);
        if (entityToDelete.isPresent()) {
            customerRepository.deleteById(customerId);
        }
        return entityToDelete.map(mapper::toDto);
    }

}
