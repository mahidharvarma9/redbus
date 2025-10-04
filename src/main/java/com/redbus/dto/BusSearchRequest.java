package com.redbus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusSearchRequest {
    
    @NotBlank(message = "Origin is required")
    private String origin;
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @NotNull(message = "Travel date is required")
    private LocalDate travelDate;
    
    private LocalTime departureTime;
    private String busType;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy = "departure"; // departure, price, duration
    private String sortOrder = "asc"; // asc, desc
}
