package com.example.demo.controllers.errorhandlers;

import com.example.demo.dtos.customers.ErrorResponseDTO;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.exceptions.MissingCustomerFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerErrorHandler {

    @ResponseBody
    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(CustomerNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MissingCustomerFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDTO todoNotValidHandler(MissingCustomerFieldException e) {
        return new ErrorResponseDTO(e.getMessage(), e.getCustomer());
    }

}
