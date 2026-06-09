package com.shoes.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Unified API Response Wrapper for all order-service endpoints
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Builder.Default
    private Boolean success = false;

    private Integer status;

    private String message;

    private T data;

    private Map<String, String> errors;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(200)
                .message(message != null ? message : "Operation successful")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation successful");
    }

    public static ApiResponse<Void> success() {
        return ApiResponse.<Void>builder()
                .success(true)
                .status(200)
                .message("Operation successful")
                .build();
    }

    public static ApiResponse<Void> success(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .status(200)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(201)
                .message(message != null ? message : "Resource created successfully")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return created(data, "Resource created successfully");
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return error(message, 400);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return error(message, 404);
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return error(message, 409);
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return error(message, 500);
    }

    public static <T> ApiResponse<T> validationError(Map<String, String> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(422)
                .message("Validation failed")
                .errors(errors)
                .build();
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return error(message, 403);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(message, 401);
    }
}

