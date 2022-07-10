package com.example.demo.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerCreationDTO {

    private String name;
    private String surname;
    private MultipartFile photo;
    // Reference to the user who modified it
    private String userRef;

}
