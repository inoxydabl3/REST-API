package com.example.demo.mappers;

import com.example.demo.dtos.UserCreationDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.RoleEntity;
import com.example.demo.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", source = "role.role")
    UserDTO toDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", source = "role")
    UserEntity toEntity(UserCreationDTO entity, RoleEntity role);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", source = "role")
    UserEntity updateUser(@MappingTarget UserEntity entity, UserCreationDTO dto, RoleEntity role);

}
