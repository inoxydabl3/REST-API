package com.example.demo.services;

import com.example.demo.dtos.CustomerDTO;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.utils.CustomerMapper;
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
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<CustomerDTO> getCustomer(int customerId) {
        return customerRepository.findById(customerId).map(mapper::toDto);
    }

}
