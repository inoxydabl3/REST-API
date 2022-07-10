package com.example.demo.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerDTO implements Serializable {

    private Integer id;
    private String name;
    private String surname;
    private String photoUrl;
    private String userRef;

}
