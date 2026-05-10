package com.fruit.warehouse.controller;

import com.fruit.warehouse.dto.ApiResponse;
import com.fruit.warehouse.entity.Category;
import com.fruit.warehouse.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {
    
    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<Category>> findAll() {
        return ApiResponse.success(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Category> findById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "分类不存在"));
    }

    @PostMapping
    public ApiResponse<Category> create(@Valid @RequestBody Category category) {
        return ApiResponse.success("创建成功", categoryService.save(category));
    }

    @PutMapping("/{id}")
    public ApiResponse<Category> update(@PathVariable Long id, @Valid @RequestBody Category category) {
        return categoryService.findById(id)
                .map(existing -> {
                    category.setId(id);
                    return ApiResponse.success("更新成功", categoryService.save(category));
                })
                .orElse(ApiResponse.error(404, "分类不存在"));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
