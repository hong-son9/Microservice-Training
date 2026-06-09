package com.shoes.order.exception;

import com.shoes.order.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for all product-service exceptions
 * Provides unified error response format via ApiResponse wrapper
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException (404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApiResponse.notFound(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handle ConflictException (409)
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(
            ConflictException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApiResponse.conflict(ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    /**
     * Handle ValidationException (422)
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            ValidationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (ex.getField() != null) {
            errors.put(ex.getField(), ex.getReason());
        }
        return new ResponseEntity<>(
                ApiResponse.validationError(errors),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    /**
     * Handle BusinessException (400)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApiResponse.badRequest(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle Jakarta Bean Validation errors (422)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(
                ApiResponse.validationError(errors),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    /**
     * Handle constraint violation exceptions (422)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
        return new ResponseEntity<>(
                ApiResponse.validationError(errors),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    /**
     * Handle generic RuntimeException (500)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApiResponse.internalError("An unexpected error occurred: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Handle all other exceptions (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ApiResponse.internalError("An unexpected error occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
