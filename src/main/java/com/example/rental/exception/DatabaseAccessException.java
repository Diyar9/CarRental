package com.example.rental.exception;

public class DatabaseAccessException extends RuntimeException {
    public DatabaseAccessException(Throwable cause) {
        super("Fel vid databasåtkomst.", cause);
    }
}
