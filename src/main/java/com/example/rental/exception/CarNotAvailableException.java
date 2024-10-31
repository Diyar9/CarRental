package com.example.rental.exception;

public class CarNotAvailableException extends RuntimeException {
    public CarNotAvailableException() {
        super("Bilen är inte tillgänglig under valt datumintervall.");
    }
}