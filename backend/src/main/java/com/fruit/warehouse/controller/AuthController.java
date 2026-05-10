package com.fruit.warehouse.controller;

import com.fruit.warehouse.dto.ApiResponse;
import com.fruit.warehouse.dto.LoginRequest;
import com.fruit.warehouse.entity.User;
import com.fruit.warehouse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<User> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request.getUsername(), request.getPassword())
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(401, "用户名或密码错误"));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        return ApiResponse.success("登出成功");
    }
}
