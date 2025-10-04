package com.redbus.dto;

import com.redbus.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    
    private Long id;
    private String bookingReference;
    private LocalDate bookingDate;
    private Integer totalSeats;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private String busNumber;
    private String operatorName;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private String trackingLink;
    private List<PassengerInfo> passengers;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerInfo {
        private Integer seatNumber;
        private String passengerName;
        private Integer passengerAge;
        private String passengerGender;
    }
    
    public static BookingResponse fromBooking(Booking booking) {
        // Generate tracking link using booking reference for secure, personalized tracking
        String trackingLink = String.format("http://localhost:9090/api/tracking/booking/%s", 
            booking.getBookingReference());
        
        return BookingResponse.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .bookingDate(booking.getBookingDate())
                .totalSeats(booking.getTotalSeats())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .busNumber(booking.getSchedule().getBus().getBusNumber())
                .operatorName(booking.getSchedule().getBus().getOperator().getName())
                .origin(booking.getSchedule().getRoute().getOrigin())
                .destination(booking.getSchedule().getRoute().getDestination())
                .departureTime(booking.getSchedule().getDepartureTime().toString())
                .arrivalTime(booking.getSchedule().getArrivalTime().toString())
                .trackingLink(trackingLink)
                .passengers(booking.getSeatBookings() != null ? booking.getSeatBookings().stream()
                    .map(sb -> PassengerInfo.builder()
                        .seatNumber(sb.getSeatNumber())
                        .passengerName(sb.getPassengerName())
                        .passengerAge(sb.getPassengerAge())
                        .passengerGender(sb.getPassengerGender().name())
                        .build())
                    .toList() : new ArrayList<>())
                .build();
    }
}
