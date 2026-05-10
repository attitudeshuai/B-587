package com.fruit.warehouse.controller;

import com.fruit.warehouse.dto.ApiResponse;
import com.fruit.warehouse.dto.DashboardStats;
import com.fruit.warehouse.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {
    
    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<DashboardStats> getStats() {
        return ApiResponse.success(dashboardService.getStats());
    }
}
