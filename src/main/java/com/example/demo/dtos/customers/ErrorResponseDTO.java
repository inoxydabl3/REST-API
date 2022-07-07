package com.example.demo.dtos.customers;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorResponseDTO implements Serializable {

    private final String errMessage;
    private final CustomerDTO customer;

}
