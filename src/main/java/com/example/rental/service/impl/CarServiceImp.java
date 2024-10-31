package com.example.rental.service.impl;

import com.example.rental.dto.CarDto;
import com.example.rental.exception.DatabaseAccessException;
import com.example.rental.exception.IllegalArgumentException;
import com.example.rental.exception.RentalCreationException;
import com.example.rental.exception.ResourceNotFoundException;
import com.example.rental.repository.CarRepository;
import com.example.rental.service.CarService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImp implements CarService {

    private final JdbcTemplate jdbcTemplate; //JDBC-template för databasåtkomst
    private final CarRepository carRepository;

    public CarServiceImp(JdbcTemplate jdbcTemplate, CarRepository carRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.carRepository = carRepository;
    }

    //Lägg till bil
    @Override
    public CarDto createCar(CarDto carDto) {
        if (carDto.getName() == null || carDto.getName().isEmpty()) {
            throw new IllegalArgumentException(IllegalArgumentException.EMPTY_NAME);
        }

        if (carDto.getPricePerDay() <= 0) {
            throw new IllegalArgumentException(IllegalArgumentException.INVALID_PRICE);
        }

        String sql = "INSERT INTO cars (name, price_per_day) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, carDto.getName(), carDto.getPricePerDay()); //Spara bilen
            return carDto;
        } catch (Exception e) {
            throw new RentalCreationException(e); //Fel vid skapande av bil
        }
    }

    //Hämta bil med ID
    @Override
    public CarDto getCarById(int id) {
        String sql = "SELECT * FROM cars WHERE id = ?";
        try {
            CarDto car = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                    new CarDto(rs.getInt("id"), rs.getString("name"), rs.getInt("price_per_day"))
            );
            if (car == null) {
                throw new ResourceNotFoundException("Bil", id);
            }
            return car;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Bil", id, e); //Fel vid hämtning av bil
        }
    }

    //Hämta alla bilar
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
            throw new DatabaseAccessException(e); //Fel vid hämtning av bilar
        }
    }

    //Uppdatera bil
    @Override
    public CarDto updateCar(int id, CarDto updatedCar) {
        String sql = "UPDATE cars SET name = ?, price_per_day = ? WHERE id = ?";
        try {
            CarDto existingCar = getCarById(id); //Kontrollera om bilen finns

            jdbcTemplate.update(sql, updatedCar.getName(), updatedCar.getPricePerDay(), id); //Uppdatera bilen

            return new CarDto(id, updatedCar.getName(), updatedCar.getPricePerDay());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseAccessException(e); //Fel vid uppdatering av bil
        }
    }

    //Ta bort bil
    @Override
    public void deleteCar(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try {
            CarDto existingCar = getCarById(id); //Kontrollera om bilen finns

            int rowsAffected = jdbcTemplate.update(sql, id);
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Bil", id); //Bilen finns inte
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseAccessException(e); //Fel vid borttagning av bil
        }
    }
}