package com.redbus.service;

import com.redbus.dto.BookingRequest;
import com.redbus.dto.BookingResponse;
import com.redbus.dto.SeatBookingRequest;
import com.redbus.entity.*;
import com.redbus.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;
    private final SeatBookingRepository seatBookingRepository;
    
    @Transactional(readOnly = true)
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingByReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference);
    }
    
    @Transactional
    public BookingResponse createBooking(BookingRequest request, Long userId) {
        // Validate schedule exists
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        
        // Check seat availability
        List<Integer> bookedSeats = bookingRepository.findBookedSeatNumbers(request.getScheduleId(), request.getTravelDate());
        for (SeatBookingRequest.PassengerInfo passenger : request.getPassengers()) {
            if (bookedSeats.contains(passenger.getSeatNumber())) {
                throw new IllegalArgumentException("Seat " + passenger.getSeatNumber() + " is already booked");
            }
        }
        
        // Create booking
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Booking booking = request.toBooking();
        booking.setUser(user);
        booking.setSchedule(schedule);
        booking.setBookingReference(generateBookingReference());
        booking.setTotalAmount(schedule.getPrice().multiply(BigDecimal.valueOf(request.getPassengers().size())));
        
        booking = bookingRepository.save(booking);
        
        // Create seat bookings
        for (SeatBookingRequest.PassengerInfo passengerInfo : request.getPassengers()) {
            SeatBooking seatBooking = SeatBooking.builder()
                    .booking(booking)
                    .seatNumber(passengerInfo.getSeatNumber())
                    .passengerName(passengerInfo.getPassengerName())
                    .passengerAge(passengerInfo.getPassengerAge())
                    .passengerGender(passengerInfo.getPassengerGender())
                    .build();
            seatBookingRepository.save(seatBooking);
        }
        
        return BookingResponse.fromBooking(booking);
    }
    
    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        
        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only cancel your own bookings");
        }
        
        if (booking.getStatus() != Booking.BookingStatus.PENDING && 
            booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Cannot cancel booking with status: " + booking.getStatus());
        }
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
    
    @Transactional
    public void confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }
    
    @Transactional(readOnly = true)
    public List<Integer> getBookedSeats(Long scheduleId, LocalDate travelDate) {
        return bookingRepository.findBookedSeatNumbers(scheduleId, travelDate);
    }
    
    private String generateBookingReference() {
        return "RB" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
