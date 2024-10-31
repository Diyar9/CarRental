package com.example.rental;

import com.example.rental.dto.CarDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/cars";

    @Test
    public void testCarCRUDOperations() throws Exception {
        // Step 1: Get all cars
        String response = mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<CarDto> carList = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, CarDto.class));

        // Step 2: Find the car named "Test Car"
        CarDto testCar = carList.stream()
                .filter(car -> "Test Car".equals(car.getName())) // Use the correct field name
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Test Car not found"));

        // Step 3: Update the car's name to "Test Completed"
        testCar.setName("Test Completed"); // Update the name

        // Step 4: Perform the update request
        mockMvc.perform(put(BASE_URL + "/{id}", testCar.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Completed")); // Verify the updated name

        // Step 5: Retrieve the updated car to verify the change
        mockMvc.perform(get(BASE_URL + "/{id}", testCar.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Completed")); // Verify the name after update

        // Step 6: Delete the car
        mockMvc.perform(delete(BASE_URL + "/{id}", testCar.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Car deleted successfully!."));
    }
}