package com.example.demo.services;

import com.example.demo.dtos.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    Optional<CustomerDTO> getCustomer(int customerId);

    CustomerDTO createCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomer(int customerId, CustomerDTO newCustomerData);

    Optional<CustomerDTO> deleteCustomer(int customerId);

}
