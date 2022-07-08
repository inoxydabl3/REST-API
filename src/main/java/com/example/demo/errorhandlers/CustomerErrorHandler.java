package com.example.demo.errorhandlers;

import com.example.demo.dtos.ErrorResponseDTO;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.exceptions.MissingCustomerFieldException;
import com.example.demo.exceptions.MissingUserFieldException;
import com.example.demo.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerErrorHandler {

    @ResponseBody
    @ExceptionHandler({CustomerNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(Exception e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MissingCustomerFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDTO<?> missingFieldHandler(MissingCustomerFieldException e) {
        return new ErrorResponseDTO<>(e.getMessage(), e.getCustomer());
    }

    @ResponseBody
    @ExceptionHandler(MissingUserFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDTO<?> missingFieldHandler(MissingUserFieldException e) {
        return new ErrorResponseDTO<>(e.getMessage(), e.getUser());
    }

}
