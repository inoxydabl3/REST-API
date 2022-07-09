package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDTO;
import com.example.demo.entities.CustomerEntity;
import com.example.demo.projections.CustomerSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    CustomerEntity toEntity(CustomerDTO source);

    CustomerDTO toDto(CustomerEntity source);

    CustomerDTO toDto(CustomerSummary source);

    @Mapping(target = "id", ignore = true)
    CustomerEntity update(@MappingTarget CustomerEntity entity, CustomerDTO dto);

}
