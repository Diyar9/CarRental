package com.example.rental.mapper;

import com.example.rental.dto.RentalDto;
import com.example.rental.entity.Rental;

public class RentalMapper {

    public static RentalDto mapToRentalDto(Rental rental) {
        return new RentalDto(
                rental.getId(),
                rental.getCar().getId(),
                rental.getDriverName(),
                rental.getDriverAge(),
                rental.getPickUpDate(),
                rental.getReturnDate()
        );
    }

    public static Rental mapToRental(RentalDto rentalDto) {
        Rental rental = new Rental();
        rental.setId(rentalDto.getId());
        rental.setDriverName(rentalDto.getDriverName());
        rental.setDriverAge(rentalDto.getDriverAge());
        rental.setPickUpDate(rentalDto.getPickUpDate());
        rental.setReturnDate(rentalDto.getReturnDate());
        return rental;
    }
}