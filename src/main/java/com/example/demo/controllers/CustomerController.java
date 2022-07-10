package com.example.demo.controllers;

import com.example.demo.dtos.CustomerCreationDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.ImageStorageException;
import com.example.demo.exceptions.responsestatus.CustomerNotFoundException;
import com.example.demo.exceptions.responsestatus.ImageContentTypeException;
import com.example.demo.exceptions.responsestatus.MissingFieldException;
import com.example.demo.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/customers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomersSummary();
    }

    @GetMapping(path = "/{customerId}")
    public CustomerDTO getCustomer(@PathVariable int customerId) {
        return customerService.getCustomer(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(CustomerCreationDTO customer, HttpServletRequest request)
            throws ImageStorageException {
        validations(customer);
        CustomerCreationDTO customerWithUserRef = customer.toBuilder()
                .userRef(request.getUserPrincipal().getName()).build();
        return new ResponseEntity<>(customerService.createCustomer(customerWithUserRef), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable int customerId, CustomerCreationDTO customer,
            HttpServletRequest request) throws ImageStorageException {
        CustomerCreationDTO customerWithUserRef = customer.toBuilder()
                .userRef(request.getUserPrincipal().getName()).build();
        return this.customerService.updateCustomer(customerId, customerWithUserRef).orElseThrow(
                () -> new CustomerNotFoundException(customerId));
    }

    @DeleteMapping(path = "/{customerId}")
    public CustomerDTO deleteCustomer(@PathVariable int customerId) {
        return customerService.deleteCustomer(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    private void validations(CustomerCreationDTO customer) {
        if (customer.getName() == null || customer.getSurname() == null) {
            throw new MissingFieldException("Request params 'name' and 'surname' are required.");
        }
        if (customer.getPhoto() != null) {
            String contentType = customer.getPhoto().getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ImageContentTypeException(String.format("File '%s' is NOT an image: ",
                        customer.getPhoto().getOriginalFilename()));
            }
        }
    }

}
