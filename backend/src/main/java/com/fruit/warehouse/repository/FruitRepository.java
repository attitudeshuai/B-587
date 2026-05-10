package com.fruit.warehouse.repository;

import com.fruit.warehouse.entity.Fruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FruitRepository extends JpaRepository<Fruit, Long> {
    
    Page<Fruit> findByStatus(Integer status, Pageable pageable);
    
    Page<Fruit> findByCategoryId(Long categoryId, Pageable pageable);
    
    Page<Fruit> findByNameContaining(String name, Pageable pageable);
    
    @Query("SELECT f FROM Fruit f WHERE f.stockQuantity <= f.minStock AND f.status = 1")
    List<Fruit> findLowStockFruits();
    
    @Query("SELECT f FROM Fruit f WHERE (:name IS NULL OR f.name LIKE %:name%) " +
           "AND (:categoryId IS NULL OR f.category.id = :categoryId) " +
           "AND (:status IS NULL OR f.status = :status)")
    Page<Fruit> searchFruits(@Param("name") String name, 
                              @Param("categoryId") Long categoryId, 
                              @Param("status") Integer status, 
                              Pageable pageable);
    
    long countByStatus(Integer status);
    
    // 检查分类是否有关联水果
    boolean existsByCategoryId(Long categoryId);
}
