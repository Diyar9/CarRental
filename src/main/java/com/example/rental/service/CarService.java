package com.example.rental.service;

import com.example.rental.dto.CarDto;

import java.util.List;

public interface CarService {
    CarDto createCar(CarDto carDto);

    CarDto getCarById(int id);

    List<CarDto> getAllCars();

    CarDto updateCar(int id, CarDto carDto);

    void deleteCar(int id);
}