package com.example.rental.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_per_day", nullable = false)
    private double pricePerDay;

    @OneToMany(mappedBy = "car")
    private List<Rental> rentals; // Lista med uthyrningar kopplade till bilen

}
