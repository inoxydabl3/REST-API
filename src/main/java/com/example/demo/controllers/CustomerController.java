package com.example.demo.controllers;

import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public CustomerDTO createCustomer() {
        throw new UnsupportedOperationException();
    }

    @PatchMapping(path = "/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable int customerId) {
        throw new UnsupportedOperationException();
    }

    @DeleteMapping(path = "/{customerId}")
    public CustomerDTO deleteCustomer(@PathVariable int customerId) {
        throw new UnsupportedOperationException();
    }

}
