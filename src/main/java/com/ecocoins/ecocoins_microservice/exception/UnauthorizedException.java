package com.ecocoins.ecocoins_microservice.exception;

public class UnauthorizedException extends RuntimeException {

    private String username;
    private String reason;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String username, String reason) {
        super(String.format("Acceso no autorizado para usuario '%s': %s", username, reason));
        this.username = username;
        this.reason = reason;
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    // Getters
    public String getUsername() { return username; }
    public String getReason() { return reason; }
}