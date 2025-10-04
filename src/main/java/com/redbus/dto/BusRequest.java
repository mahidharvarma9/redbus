package com.redbus.dto;

import com.redbus.entity.Bus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusRequest {
    
    @NotNull(message = "Operator ID is required")
    private Long operatorId;
    
    @NotBlank(message = "Bus number is required")
    private String busNumber;
    
    @NotNull(message = "Bus type is required")
    private Bus.BusType busType;
    
    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;
    
    private List<String> amenities;
    
    public Bus toBus() {
        return Bus.builder()
                .busNumber(busNumber)
                .busType(busType)
                .totalSeats(totalSeats)
                .amenities(amenities)
                .build();
    }
}
