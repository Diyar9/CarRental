package com.example.rental.service;

import com.example.rental.dto.RentalDto;

import java.time.LocalDate;
import java.util.List;

//Interface/gränssnitt med CRUD-metoder för att hantera rentals.
public interface RentalService {
    RentalDto createRental(RentalDto rentalDto);

    RentalDto getRentalById(int id);

    List<RentalDto> getAllRentals();

    RentalDto updateRental(int id, RentalDto rentalDto);

    void deleteRental(int id);

    boolean isCarAvailable(int carId, LocalDate pickUpDate, LocalDate returnDate);
}