package com.example.rental.dto;

import com.example.rental.entity.Rental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private int id;
    private String name;
    private double pricePerDay;
    private List<RentalDto> rentals;

    public CarDto(int id, String name, double pricePerDay) {
        this.id = id;
        this.name = name;
        this.pricePerDay = pricePerDay;
    }
}
