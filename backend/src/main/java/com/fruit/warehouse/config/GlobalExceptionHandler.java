package com.fruit.warehouse.config;

import com.fruit.warehouse.dto.ApiResponse;
import com.fruit.warehouse.dto.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.warn("参数验证失败: {}", message);
        return ApiResponse.error(400, message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        logger.warn("数据完整性异常: {}", e.getMessage());
        String message = "操作失败：该数据存在关联记录，无法删除";
        // 根据异常信息识别具体的外键约束类型
        String rootMessage = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();
        if (rootMessage != null) {
            if (rootMessage.contains("stock_in_records") && rootMessage.contains("fruit_id")) {
                message = "删除失败：该水果存在入库记录，无法删除";
            } else if (rootMessage.contains("stock_out_records") && rootMessage.contains("fruit_id")) {
                message = "删除失败：该水果存在出库记录，无法删除";
            } else if (rootMessage.contains("stock_in_records") && rootMessage.contains("supplier_id")) {
                message = "删除失败：该供应商存在入库记录，无法删除";
            } else if (rootMessage.contains("stock_out_records") && rootMessage.contains("customer_id")) {
                message = "删除失败：该客户存在出库记录，无法删除";
            } else if (rootMessage.contains("fruits") && rootMessage.contains("category_id")) {
                message = "删除失败：该分类下存在水果，无法删除";
            }
        }
        return ApiResponse.error(400, message);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBusinessException(BusinessException e) {
        logger.warn("业务异常: [{}] {}", e.getErrorCode(), e.getMessage());
        return ApiResponse.error(400, e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("参数异常: {}", e.getMessage());
        return ApiResponse.error(400, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常: {}", e.getMessage(), e);
        return ApiResponse.error(500, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e) {
        logger.error("系统异常: {}", e.getMessage(), e);
        return ApiResponse.error(500, "系统异常，请稍后重试");
    }
}
