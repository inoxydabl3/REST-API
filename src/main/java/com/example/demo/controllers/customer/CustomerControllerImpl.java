package com.example.demo.controllers.customer;

import com.example.demo.dtos.CustomerCreationDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.ImageStorageException;
import com.example.demo.exceptions.responsestatus.CustomerNotFoundException;
import com.example.demo.exceptions.responsestatus.ImageContentTypeException;
import com.example.demo.exceptions.responsestatus.MissingFieldException;
import com.example.demo.services.customer.CustomerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerControllerImpl implements CustomerController {

    private final @NonNull CustomerService customerService;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomersSummary();
    }

    @Override
    public CustomerDTO getCustomer(int customerId) {
        return customerService.getCustomer(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Override
    public CustomerDTO createCustomer(CustomerCreationDTO customer, HttpServletRequest request)
            throws ImageStorageException {
        validations(customer);
        CustomerCreationDTO customerWithUserRef = customer.toBuilder()
                .userRef(request.getUserPrincipal().getName()).build();
        return customerService.createCustomer(customerWithUserRef);
    }

    @Override
    public CustomerDTO updateCustomer(int customerId, CustomerCreationDTO customer, HttpServletRequest request)
            throws ImageStorageException {
        CustomerCreationDTO customerWithUserRef = customer.toBuilder()
                .userRef(request.getUserPrincipal().getName()).build();
        return this.customerService.updateCustomer(customerId, customerWithUserRef).orElseThrow(
                () -> new CustomerNotFoundException(customerId));
    }

    @Override
    public CustomerDTO deleteCustomer(int customerId) {
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
