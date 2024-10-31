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
@RequestMapping("/api/rentals") //Bas-URI för hyror
public class RentalController {

    private final RentalService rentalService; //Service för rentalsoperationer

    //Lägg till rentals REST API
    @PostMapping //POST
    public ResponseEntity<RentalDto> createRental(@RequestBody RentalDto rentalDto) {
        RentalDto savedRental = rentalService.createRental(rentalDto); //Anropar service för att spara rentals
        return new ResponseEntity<>(savedRental, HttpStatus.CREATED); //Returnerar ett nytt objekt
    }

    //Hämta rentals REST API
    @GetMapping("{id}") //GET med hyres-ID
    public ResponseEntity<RentalDto> getRentalById(@PathVariable("id") int Id) {
        RentalDto rental = rentalService.getRentalById(Id); //Hämta rentals med ID
        return ResponseEntity.ok(rental); //Returnerar rentals
    }

    //Hämta alla rentals REST API
    @GetMapping //GET för alla hyror
    public ResponseEntity<List<RentalDto>> getAllRentals() {
        List<RentalDto> rentals = rentalService.getAllRentals(); //Hämta alla rentals
        return ResponseEntity.ok(rentals); //Returnerar listan med rentals
    }

    //Uppdatera rentals REST API
    @PutMapping("{id}") //PUT med hyres-ID
    public ResponseEntity<RentalDto> updateRental(@PathVariable("id") int id, @RequestBody RentalDto updatedRental) {
        RentalDto existingRental = rentalService.updateRental(id, updatedRental); //Uppdatera rentals
        return ResponseEntity.ok(existingRental); //Returnerar uppdaterad rentals
    }

    //Ta bort rentals REST API
    @DeleteMapping("{id}") //DELETE med hyres-ID
    public ResponseEntity<String> deleteRental(@PathVariable("id") int Id) {
        rentalService.deleteRental(Id); //Ta bort rentalsn
        return ResponseEntity.ok("Rental deleted successfully!.");
    }

    //Kontrollera bilens tillgänglighet REST API
    @GetMapping("/cars/{carId}/availability") //GET för bilens tillgänglighet
    public ResponseEntity<Boolean> checkCarAvailability(@PathVariable int carId,
                                                        @RequestParam LocalDate pickUpDate,
                                                        @RequestParam LocalDate returnDate) {
        boolean isAvailable = rentalService.isCarAvailable(carId, pickUpDate, returnDate); //Kontrollera tillgänglighet
        return ResponseEntity.ok(isAvailable); //Returnerar tillgänglighetsstatus
    }
}