package com.fruit.warehouse.service;

import com.fruit.warehouse.entity.Category;
import com.fruit.warehouse.repository.CategoryRepository;
import com.fruit.warehouse.repository.FruitRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    
    private final CategoryRepository categoryRepository;
    private final FruitRepository fruitRepository;

    public List<Category> findAll() {
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        logger.info("创建/更新分类: {}", category.getName());
        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        logger.info("删除分类: {}", id);
        // 检查是否存在关联的水果
        if (fruitRepository.existsByCategoryId(id)) {
            throw new IllegalArgumentException("删除失败：该分类下存在水果，无法删除。如需删除，请先删除或转移该分类下的水果");
        }
        categoryRepository.deleteById(id);
    }

    public long count() {
        return categoryRepository.count();
    }
}
