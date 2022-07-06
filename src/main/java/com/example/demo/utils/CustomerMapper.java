package com.example.demo.utils;

import com.example.demo.dtos.CustomerDTO;
import com.example.demo.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    Customer toDto(CustomerDTO source);
    CustomerDTO toDto(Customer source);

}
