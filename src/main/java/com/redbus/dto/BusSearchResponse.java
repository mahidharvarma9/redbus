package com.redbus.dto;

import com.redbus.entity.Bus;
import com.redbus.entity.Schedule;
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
public class BusSearchResponse {
    
    private Long scheduleId;
    private Long busId;
    private String busNumber;
    private String busType;
    private String operatorName;
    private String origin;
    private String destination;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private BigDecimal price;
    private Integer totalSeats;
    private Integer availableSeats;
    private List<String> amenities;
    private String duration;
    
    public static BusSearchResponse fromSchedule(Schedule schedule, Integer availableSeats) {
        Bus bus = schedule.getBus();
        return BusSearchResponse.builder()
                .scheduleId(schedule.getId())
                .busId(bus.getId())
                .busNumber(bus.getBusNumber())
                .busType(bus.getBusType().name())
                .operatorName(bus.getOperator().getName())
                .origin(schedule.getRoute().getOrigin())
                .destination(schedule.getRoute().getDestination())
                .departureTime(schedule.getDepartureTime())
                .arrivalTime(schedule.getArrivalTime())
                .price(schedule.getPrice())
                .totalSeats(bus.getTotalSeats())
                .availableSeats(availableSeats)
                .amenities(bus.getAmenities())
                .duration(calculateDuration(schedule.getDepartureTime(), schedule.getArrivalTime()))
                .build();
    }
    
    private static String calculateDuration(LocalTime departure, LocalTime arrival) {
        int departureMinutes = departure.getHour() * 60 + departure.getMinute();
        int arrivalMinutes = arrival.getHour() * 60 + arrival.getMinute();
        
        if (arrivalMinutes < departureMinutes) {
            arrivalMinutes += 24 * 60; // Next day
        }
        
        int durationMinutes = arrivalMinutes - departureMinutes;
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        
        return String.format("%dh %dm", hours, minutes);
    }
}
