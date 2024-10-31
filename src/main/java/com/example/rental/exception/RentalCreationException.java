package com.example.rental.exception;

public class RentalCreationException extends RuntimeException {
    public RentalCreationException(Throwable cause) {
        super("Fel vid skapande av hyra.", cause);
    }
}
