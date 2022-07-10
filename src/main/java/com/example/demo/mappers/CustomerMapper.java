package com.example.demo.mappers;

import com.example.demo.dtos.CustomerCreationDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.entities.CustomerEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.projections.CustomerSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    CustomerDTO toDto(CustomerSummary source);

    @Mapping(target = "photo", source = "photo", qualifiedByName = "photoUrl")
    @Mapping(target = "userRef", source = "user.username")
    CustomerDTO toDto(CustomerEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", source = "photoName")
    @Mapping(target = "user", source = "userRef")
    CustomerEntity toEntity(CustomerCreationDTO dto, String photoName, UserEntity userRef);

    @Named("photoUrl")
    default String createPhotoUrlOf(String photo) {
        if (photo != null) {
            // TODO: change for constant
            return "/images/" + photo;
        }
        return null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "user", source = "userRef")
    CustomerEntity updateEntity(@MappingTarget CustomerEntity entity, CustomerCreationDTO dto, String photo,
            UserEntity userRef);

}
