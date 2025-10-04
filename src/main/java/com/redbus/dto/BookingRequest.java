package com.redbus.dto;

import com.redbus.entity.Booking;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    
    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;
    
    @NotNull(message = "Travel date is required")
    private java.time.LocalDate travelDate;
    
    @Valid
    @NotNull(message = "Seat bookings are required")
    private List<SeatBookingRequest.PassengerInfo> passengers;
    
    public Booking toBooking() {
        return Booking.builder()
                .bookingDate(travelDate)
                .totalSeats(passengers.size())
                .status(Booking.BookingStatus.PENDING)
                .build();
    }
}
