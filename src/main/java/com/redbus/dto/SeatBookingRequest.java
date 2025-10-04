package com.redbus.dto;

import com.redbus.entity.SeatBooking;
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
public class SeatBookingRequest {
    
    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;
    
    @NotNull(message = "Travel date is required")
    private java.time.LocalDate travelDate;
    
    @NotNull(message = "Seat bookings are required")
    private List<PassengerInfo> passengers;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerInfo {
        @NotNull(message = "Seat number is required")
        @Positive(message = "Seat number must be positive")
        private Integer seatNumber;
        
        @NotBlank(message = "Passenger name is required")
        private String passengerName;
        
        @NotNull(message = "Passenger age is required")
        @Positive(message = "Passenger age must be positive")
        private Integer passengerAge;
        
        private SeatBooking.Gender passengerGender;
    }
}
