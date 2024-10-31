package com.example.rental.mapper;

import com.example.rental.dto.CarDto;
import com.example.rental.dto.RentalDto;
import com.example.rental.entity.Car;
import com.example.rental.entity.Rental;

import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {

    public static CarDto mapToCarDto(Car car) {
        List<RentalDto> rentalDtos = car.getRentals().stream()
                .map(RentalMapper::mapToRentalDto)
                .collect(Collectors.toList());

        return new CarDto(
                car.getId(),
                car.getName(),
                car.getPricePerDay(),
                rentalDtos
        );
    }

    public static Car mapToCar(CarDto carDto) {
        List<Rental> rentals = carDto.getRentals().stream()
                .map(RentalMapper::mapToRental)
                .collect(Collectors.toList());

        return new Car(
                carDto.getId(),
                carDto.getName(),
                carDto.getPricePerDay(),
                rentals
        );
    }
}