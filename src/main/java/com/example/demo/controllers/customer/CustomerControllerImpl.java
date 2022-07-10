package com.example.demo.controllers.customer;

import com.example.demo.dtos.CustomerCreationDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.ImageStorageException;
import com.example.demo.exceptions.responsestatus.CustomerNotFoundException;
import com.example.demo.exceptions.responsestatus.ImageContentTypeException;
import com.example.demo.services.customer.CustomerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public CustomerDTO createCustomer(String name, String surname, MultipartFile photo, HttpServletRequest request)
            throws ImageStorageException {
        validatePhoto(photo);
        CustomerCreationDTO customerWithUserRef = CustomerCreationDTO.builder()
                .name(name).surname(surname).photo(photo)
                .userRef(request.getUserPrincipal().getName()).build();
        return customerService.createCustomer(customerWithUserRef);
    }

    @Override
    public CustomerDTO updateCustomer(int customerId, String name, String surname, MultipartFile photo,
            HttpServletRequest request) throws ImageStorageException {
        validatePhoto(photo);
        CustomerCreationDTO customerWithUserRef = CustomerCreationDTO.builder()
                .name(name).surname(surname).photo(photo)
                .userRef(request.getUserPrincipal().getName()).build();
        return this.customerService.updateCustomer(customerId, customerWithUserRef).orElseThrow(
                () -> new CustomerNotFoundException(customerId));
    }

    @Override
    public CustomerDTO deleteCustomer(int customerId) {
        return customerService.deleteCustomer(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    private void validatePhoto(MultipartFile photo) {
        if (photo != null) {
            String contentType = photo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ImageContentTypeException(String.format("File '%s' is NOT an image: ",
                        photo.getOriginalFilename()));
            }
        }
    }

}
