package com.example.rental.controller;

import com.example.rental.dto.CarDto;
import com.example.rental.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/cars") //Bas-URI
public class CarController {

    private final CarService carService;

    //Lägg till cars REST API
    @PostMapping //POST-förfrågningar
    public ResponseEntity<CarDto> createCar(@RequestBody CarDto carDto) {
        CarDto savedCar = carService.createCar(carDto); //Anropar service för att spara bilen
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED); //Returnerar ett nytt objekt
    }

    //Hämta bil cars API
    @GetMapping("{id}") //GET-förfrågningar med bil-ID
    public ResponseEntity<CarDto> getCarById(@PathVariable("id") int Id) {
        CarDto car = carService.getCarById(Id); //Hämta bil med ID
        return ResponseEntity.ok(car); //Returnerar bilen
    }

    //Hämta alla cars REST API
    @GetMapping //GET-förfrågningar för alla bilar
    public ResponseEntity<List<CarDto>> getAllCars() {
        List<CarDto> cars = carService.getAllCars(); //Hämta alla bilar
        return ResponseEntity.ok(cars); //Returnerar listan med bilar
    }

    //Uppdatera cars REST API
    @PutMapping("{id}") //PUT-förfrågningar med bil-ID
    public ResponseEntity<CarDto> updateCar(@PathVariable("id") int id, @RequestBody CarDto updatedCar) {
        CarDto existingCar = carService.updateCar(id, updatedCar); //Uppdatera bilen
        return ResponseEntity.ok(existingCar); //Returnerar den uppdaterade bilen
    }

    //Ta bort cars REST API
    @DeleteMapping("{id}") //DELETE-förfrågningar med bil-ID
    public ResponseEntity<String> deleteCar(@PathVariable("id") int Id) {
        carService.deleteCar(Id); //Ta bort bilen
        return ResponseEntity.ok("Car deleted successfully!."); //Bekräftelsemeddelande
    }
}