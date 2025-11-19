package com.ecocoins.ecocoins_microservice.exception;

public class BadRequestException extends RuntimeException {

    private String field;
    private Object value;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String field, Object value, String message) {
        super(message);
        this.field = field;
        this.value = value;
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    // Getters
    public String getField() { return field; }
    public Object getValue() { return value; }
}