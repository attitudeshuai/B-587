package com.fruit.warehouse.controller;

import com.fruit.warehouse.dto.ApiResponse;
import com.fruit.warehouse.entity.Fruit;
import com.fruit.warehouse.service.CategoryService;
import com.fruit.warehouse.service.FruitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fruits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FruitController {
    
    private final FruitService fruitService;
    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<Page<Fruit>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Fruit> fruits = fruitService.searchFruits(name, categoryId, status, pageRequest);
        return ApiResponse.success(fruits);
    }

    @GetMapping("/all")
    public ApiResponse<List<Fruit>> findAllList() {
        return ApiResponse.success(fruitService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Fruit> findById(@PathVariable Long id) {
        return fruitService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "水果不存在"));
    }

    @GetMapping("/low-stock")
    public ApiResponse<List<Fruit>> findLowStock() {
        return ApiResponse.success(fruitService.findLowStockFruits());
    }

    @PostMapping
    public ApiResponse<Fruit> create(@Valid @RequestBody Fruit fruit) {
        return ApiResponse.success("创建成功", fruitService.save(fruit));
    }

    @PutMapping("/{id}")
    public ApiResponse<Fruit> update(@PathVariable Long id, @Valid @RequestBody Fruit fruit) {
        return fruitService.findById(id)
                .map(existing -> {
                    fruit.setId(id);
                    return ApiResponse.success("更新成功", fruitService.save(fruit));
                })
                .orElse(ApiResponse.error(404, "水果不存在"));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        fruitService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
