package com.example.demo.mappers;

import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "role", source = "role.role")
    UserDTO toDto(UserEntity source);

}
