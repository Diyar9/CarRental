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
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    // Build Add Car REST API
    @PostMapping
    public ResponseEntity<CarDto> createCar(@RequestBody CarDto carDto) {
        CarDto savedCar = carService.createCar(carDto);
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }

    //Build Get Car REST API
    @GetMapping("{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable("id") int Id) {
        CarDto car = carService.getCarById(Id);
        return ResponseEntity.ok(car);
    }

    //Build Get All Cars REST API
    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        List<CarDto> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    // Build Update Car REST API
    @PutMapping("{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable("id") int id, @RequestBody CarDto updatedCar) {
        CarDto existingCar = carService.updateCar(id, updatedCar);
        return ResponseEntity.ok(existingCar);
    }

    // Build Delete Car REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCar(@PathVariable("id") int Id) {
        carService.deleteCar(Id);
        return ResponseEntity.ok("Car deleted successfully!.");
    }
}