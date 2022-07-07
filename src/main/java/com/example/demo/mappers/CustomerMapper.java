package com.example.demo.mappers;

import com.example.demo.dtos.customers.CustomerDTO;
import com.example.demo.entities.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    CustomerEntity toEntity(CustomerDTO source);

    CustomerDTO toDto(CustomerEntity source);

}
