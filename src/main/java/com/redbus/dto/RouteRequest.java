package com.redbus.dto;

import com.redbus.entity.Route;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequest {
    
    @NotBlank(message = "Origin is required")
    private String origin;
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @Positive(message = "Distance must be positive")
    private BigDecimal distanceKm;
    
    @Positive(message = "Duration must be positive")
    private BigDecimal estimatedDurationHours;
    
    public Route toRoute() {
        return Route.builder()
                .origin(origin)
                .destination(destination)
                .distanceKm(distanceKm)
                .estimatedDurationHours(estimatedDurationHours)
                .build();
    }
}
