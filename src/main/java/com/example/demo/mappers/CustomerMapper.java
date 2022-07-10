package com.example.demo.mappers;

import com.example.demo.dtos.CustomerCreationDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.entities.CustomerEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.projections.CustomerSummaryView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    CustomerDTO toDto(CustomerSummaryView source);

    @Mapping(target = "photoUrl",
            expression = "java(entity.getPhoto() != null ? imageEndpoint + \"/\" + entity.getPhoto() : null)")
    @Mapping(target = "userRef", source = "entity.user.username")
    CustomerDTO toDto(CustomerEntity entity, String imageEndpoint);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", source = "photoName")
    @Mapping(target = "user", source = "userRef")
    CustomerEntity toEntity(CustomerCreationDTO dto, String photoName, UserEntity userRef);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "user", source = "userRef")
    CustomerEntity updateEntity(@MappingTarget CustomerEntity entity, CustomerCreationDTO dto, String photo,
            UserEntity userRef);

}
