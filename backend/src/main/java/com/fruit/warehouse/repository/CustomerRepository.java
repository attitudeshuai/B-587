package com.fruit.warehouse.repository;

import com.fruit.warehouse.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByLevel(String level);
    List<Customer> findByNameContaining(String name);
}
