package com.example.demo.services.customer;

import com.example.demo.dtos.CustomerCreationDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.ImageStorageException;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerDTO> getAllCustomersSummary();

    Optional<CustomerDTO> getCustomer(int customerId);

    CustomerDTO createCustomer(CustomerCreationDTO customerData) throws ImageStorageException;

    Optional<CustomerDTO> updateCustomer(int customerId, CustomerCreationDTO customerData) throws ImageStorageException;

    Optional<CustomerDTO> deleteCustomer(int customerId);

}
