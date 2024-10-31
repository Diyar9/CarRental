package com.example.rental.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "driver_name", nullable = false)
    private String driverName;

    @Column(name = "driver_age", nullable = false)
    private int driverAge;

    @Column(name = "pick_up_date", nullable = false)
    private LocalDate pickUpDate;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;
}
