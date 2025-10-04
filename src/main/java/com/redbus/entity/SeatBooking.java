package com.redbus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "seat_bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatBooking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;
    
    @Column(name = "passenger_name", nullable = false, length = 100)
    private String passengerName;
    
    @Column(name = "passenger_age")
    private Integer passengerAge;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "passenger_gender", length = 10)
    private Gender passengerGender;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
