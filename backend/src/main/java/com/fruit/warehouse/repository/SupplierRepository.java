package com.fruit.warehouse.repository;

import com.fruit.warehouse.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByStatus(Integer status);
    List<Supplier> findByNameContaining(String name);
}
