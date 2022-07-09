package com.example.demo.repositories;

import com.example.demo.entities.CustomerEntity;
import com.example.demo.projections.CustomerSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    List<CustomerSummary> findAllBy();

}
