package com.example.demo.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    private Integer id;
    private String role;

}
