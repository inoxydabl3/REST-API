package com.example.demo.exceptions;

import com.example.demo.dtos.customers.CustomerDTO;
import lombok.Getter;

public class MissingCustomerFieldException extends RuntimeException {

    @Getter
    private final CustomerDTO customer;

    public MissingCustomerFieldException(String message, CustomerDTO customer) {
        super(message);
        this.customer = customer;
    }

}
