package com.example.rental.service.impl;

import com.example.rental.dto.CarDto;
import com.example.rental.dto.RentalDto;
import com.example.rental.exception.CarNotAvailableException;
import com.example.rental.exception.ResourceNotFoundException;
import com.example.rental.repository.RentalRepository;
import com.example.rental.service.RentalService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalServiceImp implements RentalService {

    private final JdbcTemplate jdbcTemplate;
    private final RentalRepository rentalRepository;
    private final CarServiceImp carServiceImp;

    public RentalServiceImp(JdbcTemplate jdbcTemplate, RentalRepository rentalRepository, CarServiceImp carServiceImp) {
        this.jdbcTemplate = jdbcTemplate;
        this.rentalRepository = rentalRepository;
        this.carServiceImp = carServiceImp;
    }

    @Override
    public RentalDto createRental(RentalDto createRental) {
        boolean isAvailable = isCarAvailable(createRental.getCarId(), createRental.getPickUpDate(), createRental.getReturnDate());

        if (!isAvailable) {
            throw new CarNotAvailableException("Bilen är redan uthyrd under valt datumintervall.");
        }

        String sql = "INSERT INTO rentals (car_id, driver_name, driver_age, pick_up_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    createRental.getCarId(),
                    createRental.getDriverName(),
                    createRental.getDriverAge(),
                    java.sql.Date.valueOf(createRental.getPickUpDate()),
                    java.sql.Date.valueOf(createRental.getReturnDate())
            );
            return createRental;
        } catch (Exception e) {
            System.err.println("Error while creating rental: " + e.getMessage());
            throw new RuntimeException("Failed to create rental: " + e.getMessage());
        }
    }


    @Override
    public RentalDto getRentalById(int id) {
        String sql = "SELECT * FROM rentals WHERE id = ?";
        try {
            RentalDto rental = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                    new RentalDto(
                            rs.getInt("id"),
                            rs.getInt("car_id"),
                            rs.getString("driver_name"),
                            rs.getInt("driver_age"),
                            rs.getObject("pick_up_date", LocalDate.class),
                            rs.getObject("return_date", LocalDate.class)
                    )
            );

            if (rental == null) {
                throw new ResourceNotFoundException("Rental not found with given id: " + id);
            }

            return rental;
        } catch (Exception e) {
            System.err.println("Error while fetching rental by id: " + e.getMessage());
            throw new RuntimeException("Failed to fetch rental: " + e.getMessage());
        }
    }

    @Override
    public List<RentalDto> getAllRentals() {
        String sql = "SELECT r.*, c.name AS car_name FROM rentals r JOIN cars c ON r.car_id = c.id";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    new RentalDto(
                            rs.getInt("id"),
                            rs.getInt("car_id"),
                            rs.getString("car_name"),
                            rs.getString("driver_name"),
                            rs.getInt("driver_age"),
                            rs.getObject("pick_up_date", LocalDate.class),
                            rs.getObject("return_date", LocalDate.class)
                    )
            );
        } catch (Exception e) {
            System.err.println("Error while fetching all rentals: " + e.getMessage());
            throw new RuntimeException("Failed to fetch rentals: " + e.getMessage());
        }
    }


    @Override
    public RentalDto updateRental(int id, RentalDto updatedRental) {
        String sql = "UPDATE rentals SET driver_name = ?, driver_age = ?, pick_up_date = ?, return_date = ? WHERE id = ?";
        try {
            RentalDto existingRental = getRentalById(id);

            jdbcTemplate.update(sql, updatedRental.getDriverName(), updatedRental.getDriverAge(), updatedRental.getPickUpDate(), updatedRental.getReturnDate(), id);

            return new RentalDto(id, updatedRental.getCarId(), updatedRental.getDriverName(), updatedRental.getDriverAge(), updatedRental.getPickUpDate(), updatedRental.getReturnDate());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error while updating rental: " + e.getMessage());
            throw new RuntimeException("Failed to update rental: " + e.getMessage());
        }
    }

    @Override
    public void deleteRental(int id) {
        String sql = "DELETE FROM rentals WHERE id = ?";
        try {
            RentalDto existingRental = getRentalById(id);

            int rowsAffected = jdbcTemplate.update(sql, id);
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Rental not found with id: " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error while deleting Rental: " + e.getMessage());
            throw new RuntimeException("Failed to delete Rental: " + e.getMessage());
        }
    }

    @Override
    public boolean isCarAvailable(int carId, LocalDate pickUpDate, LocalDate returnDate) {
        String sql = "SELECT COUNT(*) FROM rentals WHERE car_id = ? AND (pick_up_date < ? AND return_date > ?)";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {
                carId,
                returnDate,
                pickUpDate
        }, Integer.class);

        return count == 0;
    }

}