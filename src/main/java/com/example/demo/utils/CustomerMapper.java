package com.example.demo.utils;

import com.example.demo.dtos.CustomerDTO;
import com.example.demo.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerDTO source);

    CustomerDTO toDto(Customer source);

}
