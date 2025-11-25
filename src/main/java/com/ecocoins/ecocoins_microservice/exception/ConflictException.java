package com.ecocoins.ecocoins_microservice.exception;

public class ConflictException extends RuntimeException {

    private String resource;
    private String conflictField;
    private Object conflictValue;

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String resource, String conflictField, Object conflictValue) {
        super(String.format("Ya existe un %s con %s: '%s'", resource, conflictField, conflictValue));
        this.resource = resource;
        this.conflictField = conflictField;
        this.conflictValue = conflictValue;
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    // Getters
    public String getResource() { return resource; }
    public String getConflictField() { return conflictField; }
    public Object getConflictValue() { return conflictValue; }
}