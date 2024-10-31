package com.example.rental.exception;

public class IllegalArgumentException extends RuntimeException {
  public static final String EMPTY_NAME = "Bilnamn får inte vara tomt.";
  public static final String INVALID_PRICE = "Pris per dag måste vara större än noll.";
  public static final String INVALID_AGE = "Föraren måste vara minst 1 år gammal.";
  public static final String INVALID_CAR_ID = "Ogiltigt bil-ID.";


  public IllegalArgumentException(String message) {
    super(message);
  }
}

