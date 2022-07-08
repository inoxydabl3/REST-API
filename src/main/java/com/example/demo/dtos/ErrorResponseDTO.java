package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorResponseDTO<T extends Serializable> implements Serializable {

    private final String errMessage;
    private final T dto;

}
