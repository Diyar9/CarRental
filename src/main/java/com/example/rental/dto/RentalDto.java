package com.example.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalDto {
    private int id;
    private int carId;
    private String carName;
    private String driverName;
    private int driverAge;
    private LocalDate pickUpDate;
    private LocalDate returnDate;


    public RentalDto(int id, int carId, String driverName, int driverAge, LocalDate pickUpDate, LocalDate returnDate) {
        this.id = id;
        this.carId = carId;
        this.driverName = driverName;
        this.driverAge = driverAge;
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
    }
}