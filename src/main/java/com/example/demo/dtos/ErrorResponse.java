package com.example.demo.dtos;

import lombok.Data;

@Data
public class ErrorResponse {

    private final String errMessage;
    private final CustomerDTO customer;

}
