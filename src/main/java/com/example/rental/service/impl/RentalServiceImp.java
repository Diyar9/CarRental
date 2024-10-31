package com.example.rental.service.impl;

import com.example.rental.dto.RentalDto;
import com.example.rental.exception.*;
import com.example.rental.exception.IllegalArgumentException;
import com.example.rental.repository.RentalRepository;
import com.example.rental.service.RentalService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalServiceImp implements RentalService {

    private final JdbcTemplate jdbcTemplate; // JDBC-template för databasåtkomst
    private final RentalRepository rentalRepository; // Repository för hyror
    private final CarServiceImp carServiceImp; // Service för bilar

    public RentalServiceImp(JdbcTemplate jdbcTemplate, RentalRepository rentalRepository, CarServiceImp carServiceImp) {
        this.jdbcTemplate = jdbcTemplate;
        this.rentalRepository = rentalRepository;
        this.carServiceImp = carServiceImp;
    }

    // Lägg till hyra
    @Override
    public RentalDto createRental(RentalDto createRental) {
        if (createRental.getDriverAge() <= 0) {
            throw new IllegalArgumentException(IllegalArgumentException.INVALID_AGE); // Ogiltig ålder
        }

        if (createRental.getCarId() <= 0) {
            throw new IllegalArgumentException(IllegalArgumentException.INVALID_CAR_ID); // Ogiltigt bil-ID
        }

        if (!isCarAvailable(createRental.getCarId(), createRental.getPickUpDate(), createRental.getReturnDate())) {
            throw new CarNotAvailableException(); // Bilen är inte tillgänglig
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
            return createRental; // Returnera den skapade hyran
        } catch (Exception e) {
            throw new RentalCreationException(e); // Fel vid skapande av hyra
        }
    }

    // Hämta hyra med ID
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
                throw new ResourceNotFoundException("Hyra", id); // Hyran finns inte
            }
            return rental; // Returnera hyran
        } catch (Exception e) {
            throw new ResourceNotFoundException("Hyra", id, e); // Fel vid hämtning av hyra
        }
    }

    // Hämta alla hyror
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
            ); // Returnera alla hyror
        } catch (Exception e) {
            throw new DatabaseAccessException(e); // Fel vid hämtning av hyror
        }
    }

    // Uppdatera hyra
    @Override
    public RentalDto updateRental(int id, RentalDto updatedRental) {
        String sql = "UPDATE rentals SET driver_name = ?, driver_age = ?, pick_up_date = ?, return_date = ? WHERE id = ?";
        try {
            RentalDto existingRental = getRentalById(id); // Kontrollera om hyran finns

            if (!isCarAvailable(updatedRental.getCarId(), updatedRental.getPickUpDate(), updatedRental.getReturnDate())) {
                throw new CarNotAvailableException(); // Bilen är inte tillgänglig
            }

            jdbcTemplate.update(sql, updatedRental.getDriverName(), updatedRental.getDriverAge(),
                    java.sql.Date.valueOf(updatedRental.getPickUpDate()),
                    java.sql.Date.valueOf(updatedRental.getReturnDate()), id); // Uppdatera hyran

            return new RentalDto(id, updatedRental.getCarId(), updatedRental.getDriverName(),
                    updatedRental.getDriverAge(), updatedRental.getPickUpDate(),
                    updatedRental.getReturnDate()); // Returnera den uppdaterade hyran
        } catch (ResourceNotFoundException e) {
            throw e; // Om hyran inte finns
        } catch (Exception e) {
            throw new DatabaseAccessException(e); // Fel vid uppdatering av hyra
        }
    }

    // Ta bort hyra
    @Override
    public void deleteRental(int id) {
        String sql = "DELETE FROM rentals WHERE id = ?";
        try {
            RentalDto existingRental = getRentalById(id); // Kontrollera om hyran finns

            int rowsAffected = jdbcTemplate.update(sql, id);
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Hyra", id); // Hyran finns inte
            }
        } catch (ResourceNotFoundException e) {
            throw e; // Om hyran inte finns
        } catch (Exception e) {
            throw new DatabaseAccessException(e); // Fel vid borttagning av hyra
        }
    }

    // Kontrollera om bilen är tillgänglig
    @Override
    public boolean isCarAvailable(int carId, LocalDate pickUpDate, LocalDate returnDate) {
        String sql = "SELECT COUNT(*) FROM rentals WHERE car_id = ? AND (pick_up_date < ? AND return_date > ?)";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, new Object[]{
                    carId,
                    returnDate,
                    pickUpDate
            }, Integer.class);

            return count == 0; // Returnera om bilen är tillgänglig
        } catch (Exception e) {
            throw new DatabaseAccessException(e); // Fel vid kontroll av bilens tillgänglighet
        }
    }
}
