package com.example.demo.controllers.customer;

import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.ImageStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping(path = "${app.customersEndpoint}")
public interface CustomerController {

    @GetMapping
    List<CustomerDTO> getAllCustomers();

    @GetMapping(path = "/{customerId}")
    CustomerDTO getCustomer(@PathVariable int customerId);

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    CustomerDTO createCustomer(@RequestParam String name, @RequestParam String surname,
            @RequestPart(required = false) MultipartFile photo, HttpServletRequest request) throws ImageStorageException;

    @PatchMapping(path = "/{customerId}")
    CustomerDTO updateCustomer(@PathVariable int customerId, @RequestParam(required = false) String name,
            @RequestParam(required = false)  String surname, @RequestPart(required = false)  MultipartFile photo,
            HttpServletRequest request) throws ImageStorageException;

    @DeleteMapping(path = "/{customerId}")
    CustomerDTO deleteCustomer(@PathVariable int customerId);

}
