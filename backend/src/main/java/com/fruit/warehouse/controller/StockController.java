package com.fruit.warehouse.controller;

import com.fruit.warehouse.dto.ApiResponse;
import com.fruit.warehouse.dto.StockInRequest;
import com.fruit.warehouse.dto.StockOutRequest;
import com.fruit.warehouse.entity.StockInRecord;
import com.fruit.warehouse.entity.StockOutRecord;
import com.fruit.warehouse.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {
    
    private final StockService stockService;

    @PostMapping("/in")
    public ApiResponse<StockInRecord> stockIn(@Valid @RequestBody StockInRequest request) {
        StockInRecord record = stockService.stockIn(request);
        return ApiResponse.success("入库成功", record);
    }

    @PostMapping("/out")
    public ApiResponse<StockOutRecord> stockOut(@Valid @RequestBody StockOutRequest request) {
        StockOutRecord record = stockService.stockOut(request);
        return ApiResponse.success("出库成功", record);
    }

    @GetMapping("/in/records")
    public ApiResponse<Page<StockInRecord>> getStockInRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(stockService.findAllStockIn(pageRequest));
    }

    @GetMapping("/out/records")
    public ApiResponse<Page<StockOutRecord>> getStockOutRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(stockService.findAllStockOut(pageRequest));
    }
}
