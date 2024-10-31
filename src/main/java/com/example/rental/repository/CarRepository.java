package com.example.rental.repository;

import com.example.rental.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> { }
