package com.shoes.cart.exception;

/**
 * Exception thrown when business logic validation fails
 */
public class ValidationException extends RuntimeException {
    private final String field;
    private final String reason;

    public ValidationException(String message) {
        super(message);
        this.field = null;
        this.reason = message;
    }

    public ValidationException(String field, String reason) {
        super(String.format("Validation failed for field '%s': %s", field, reason));
        this.field = field;
        this.reason = reason;
    }

    public String getField() {
        return field;
    }

    public String getReason() {
        return reason;
    }
}

