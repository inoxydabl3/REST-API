package com.example.demo.controllers;

import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.exceptions.MissingCustomerFieldException;
import com.example.demo.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/customers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping(path = "/{customerId}")
    public CustomerDTO getCustomer(@PathVariable int customerId) {
        return service.getCustomer(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customer) {
        if (customer.getName() == null || customer.getSurname() == null) {
            throw new MissingCustomerFieldException("Request params 'name' and 'surname' are required.", customer);
        }
        return new ResponseEntity<>(service.createCustomer(customer), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable int customerId, @RequestBody CustomerDTO customer) {
        return service.updateCustomer(customerId, customer).orElseThrow(
                () -> new CustomerNotFoundException(customerId));
    }

    @DeleteMapping(path = "/{customerId}")
    public CustomerDTO deleteCustomer(@PathVariable int customerId) {
        return service.deleteCustomer(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

}
