package com.example.demo.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerDTO {

    private Integer id;
    private String name;
    private String surname;
    private String photo;

}
