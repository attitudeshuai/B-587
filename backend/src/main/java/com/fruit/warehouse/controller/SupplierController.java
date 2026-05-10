package com.fruit.warehouse.controller;

import com.fruit.warehouse.dto.ApiResponse;
import com.fruit.warehouse.entity.Supplier;
import com.fruit.warehouse.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierController {
    
    private final SupplierService supplierService;

    @GetMapping
    public ApiResponse<List<Supplier>> findAll() {
        return ApiResponse.success(supplierService.findAll());
    }

    @GetMapping("/active")
    public ApiResponse<List<Supplier>> findActive() {
        return ApiResponse.success(supplierService.findByStatus(1));
    }

    @GetMapping("/{id}")
    public ApiResponse<Supplier> findById(@PathVariable Long id) {
        return supplierService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "供应商不存在"));
    }

    @PostMapping
    public ApiResponse<Supplier> create(@Valid @RequestBody Supplier supplier) {
        return ApiResponse.success("创建成功", supplierService.save(supplier));
    }

    @PutMapping("/{id}")
    public ApiResponse<Supplier> update(@PathVariable Long id, @Valid @RequestBody Supplier supplier) {
        return supplierService.findById(id)
                .map(existing -> {
                    supplier.setId(id);
                    return ApiResponse.success("更新成功", supplierService.save(supplier));
                })
                .orElse(ApiResponse.error(404, "供应商不存在"));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        supplierService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
