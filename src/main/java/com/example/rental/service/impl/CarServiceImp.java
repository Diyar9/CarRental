package com.example.rental.service.impl;

import com.example.rental.dto.CarDto;
import com.example.rental.exception.ResourceNotFoundException;
import com.example.rental.repository.CarRepository;
import com.example.rental.service.CarService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImp implements CarService {

    private final JdbcTemplate jdbcTemplate;
    private final CarRepository carRepository;

    public CarServiceImp(JdbcTemplate jdbcTemplate, CarRepository carRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.carRepository = carRepository;
    }

    @Override
    public CarDto createCar(CarDto carDto) {
        String sql = "INSERT INTO cars (name, price_per_day) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, carDto.getName(), carDto.getPricePerDay());
            return carDto;
        } catch (Exception e) {
            System.err.println("Error while creating car: " + e.getMessage());
            throw new RuntimeException("Failed to create car: " + e.getMessage());
        }
    }

    @Override
    public CarDto getCarById(int id) {
        String sql = "SELECT * FROM cars WHERE id = ?";
        try {
            CarDto car = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                    new CarDto(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("price_per_day")
                    )
            );

            if (car == null) {
                throw new ResourceNotFoundException("Car not found with given id: " + id);
            }

            return car;
        } catch (Exception e) {
            System.err.println("Error while fetching car by id: " + e.getMessage());
            throw new RuntimeException("Failed to fetch car: " + e.getMessage());
        }
    }

    @Override
    public List<CarDto> getAllCars() {
        String sql = "SELECT * FROM cars";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    new CarDto(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("price_per_day")));
        } catch (Exception e) {
            System.err.println("Error while fetching all cars: " + e.getMessage());
            throw new RuntimeException("Failed to fetch cars: " + e.getMessage());
        }
    }

    @Override
    public CarDto updateCar(int id, CarDto updatedCar) {
        String sql = "UPDATE cars SET name = ?, price_per_day = ? WHERE id = ?";
        try {
            CarDto existingCar = getCarById(id);

            jdbcTemplate.update(sql, updatedCar.getName(), updatedCar.getPricePerDay(), id);

            return new CarDto(id, updatedCar.getName(), updatedCar.getPricePerDay());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error while updating car: " + e.getMessage());
            throw new RuntimeException("Failed to update car: " + e.getMessage());
        }
    }

    @Override
    public void deleteCar(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try {
            CarDto existingCar = getCarById(id);

            int rowsAffected = jdbcTemplate.update(sql, id);
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Car not found with id: " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error while deleting car: " + e.getMessage());
            throw new RuntimeException("Failed to delete car: " + e.getMessage());
        }
    }
}