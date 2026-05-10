package com.fruit.warehouse.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code;
    private String errorCode;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> error(int code, String errorCode, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setErrorCode(errorCode);
        response.setMessage(message);
        return response;
    }
}
