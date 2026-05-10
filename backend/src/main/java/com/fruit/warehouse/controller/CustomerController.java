package com.fruit.warehouse.controller;

import com.fruit.warehouse.dto.ApiResponse;
import com.fruit.warehouse.entity.Customer;
import com.fruit.warehouse.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerController {
    
    private final CustomerService customerService;

    @GetMapping
    public ApiResponse<List<Customer>> findAll() {
        return ApiResponse.success(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Customer> findById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "客户不存在"));
    }

    @PostMapping
    public ApiResponse<Customer> create(@Valid @RequestBody Customer customer) {
        return ApiResponse.success("创建成功", customerService.save(customer));
    }

    @PutMapping("/{id}")
    public ApiResponse<Customer> update(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        return customerService.findById(id)
                .map(existing -> {
                    customer.setId(id);
                    return ApiResponse.success("更新成功", customerService.save(customer));
                })
                .orElse(ApiResponse.error(404, "客户不存在"));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        customerService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
