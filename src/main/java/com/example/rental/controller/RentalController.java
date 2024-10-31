package com.example.rental.controller;

import com.example.rental.dto.RentalDto;
import com.example.rental.service.RentalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    //Build Add Rental REST API
    @PostMapping
    public ResponseEntity<RentalDto> createRental(@RequestBody RentalDto rentalDto) {
        RentalDto savedRental = rentalService.createRental(rentalDto);
        return new ResponseEntity<>(savedRental, HttpStatus.CREATED);
    }

    //Build Get Rental REST API
    @GetMapping("{id}")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable("id") int Id) {
        RentalDto rental = rentalService.getRentalById(Id);
        return ResponseEntity.ok(rental);
    }

    //Build Get All Rentals REST API
    @GetMapping
    public ResponseEntity<List<RentalDto>> getAllRentals() {
        List<RentalDto> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    // Build Update Rental REST API
    @PutMapping("{id}")
    public ResponseEntity<RentalDto> updateRental(@PathVariable("id") int id, @RequestBody RentalDto updatedRental) {
        RentalDto existingRental = rentalService.updateRental(id, updatedRental);
        return ResponseEntity.ok(existingRental);
    }

    // Build Delete Rental REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRental(@PathVariable("id") int Id) {
        rentalService.deleteRental(Id);
        return ResponseEntity.ok("Rental deleted successfully!.");
    }

    @GetMapping("/cars/{carId}/availability")
    public ResponseEntity<Boolean> checkCarAvailability(@PathVariable int carId,
                                                        @RequestParam LocalDate pickUpDate,
                                                        @RequestParam LocalDate returnDate) {
        boolean isAvailable = rentalService.isCarAvailable(carId, pickUpDate, returnDate);
        return ResponseEntity.ok(isAvailable);
    }
}