package com.redbus.dto;

import com.redbus.entity.Schedule;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {
    
    @NotNull(message = "Bus ID is required")
    private Long busId;
    
    @NotNull(message = "Route ID is required")
    private Long routeId;
    
    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;
    
    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    private Boolean isRecurring = true;
    
    private List<Integer> daysOfWeek;
    
    public Schedule toSchedule() {
        return Schedule.builder()
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .price(price)
                .isRecurring(isRecurring)
                .daysOfWeek(daysOfWeek)
                .build();
    }
}
