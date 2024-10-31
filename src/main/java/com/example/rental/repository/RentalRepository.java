package com.example.rental.repository;

import com.example.rental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> { }
