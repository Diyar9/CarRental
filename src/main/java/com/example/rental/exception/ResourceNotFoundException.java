package com.example.rental.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, int id) {
        super(resourceType + " kunde inte hittas med ID: " + id);
    }

    public ResourceNotFoundException(String resourceType, int id, Throwable cause) {
        super(resourceType + " kunde inte hittas med ID: " + id, cause);
    }
}
