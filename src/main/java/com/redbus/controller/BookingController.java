package com.redbus.controller;

import com.redbus.dto.BookingRequest;
import com.redbus.dto.BookingResponse;
import com.redbus.entity.Booking;
import com.redbus.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request, 
                                                      Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        BookingResponse booking = bookingService.createBooking(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }
    
    private Long getUserIdFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof com.redbus.entity.User) {
            // User entity already implements UserDetails, so this should work
            return ((com.redbus.entity.User) principal).getId();
        }
        // If we get here, something is wrong with authentication
        throw new IllegalStateException("Unable to extract user ID from authentication. Principal type: " + 
                                       (principal != null ? principal.getClass().getName() : "null"));
    }
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingResponse>> getUserBookings(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<Booking> bookings = bookingService.getUserBookings(userId);
        List<BookingResponse> responses = bookings.stream()
                .map(BookingResponse::fromBooking)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingResponse>> getUserBookingsAlternate(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<Booking> bookings = bookingService.getUserBookings(userId);
        List<BookingResponse> responses = bookings.stream()
                .map(BookingResponse::fromBooking)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id, 
                                                      Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return bookingService.getBookingById(id)
                .map(booking -> {
                    if (!booking.getUser().getId().equals(userId)) {
                        throw new IllegalArgumentException("You can only view your own bookings");
                    }
                    return ResponseEntity.ok(BookingResponse.fromBooking(booking));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/reference/{bookingReference}")
    public ResponseEntity<BookingResponse> getBookingByReference(@PathVariable String bookingReference) {
        return bookingService.getBookingByReference(bookingReference)
                .map(booking -> ResponseEntity.ok(BookingResponse.fromBooking(booking)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, 
                                             Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        bookingService.cancelBooking(id, userId);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/schedule/{scheduleId}/date/{date}/booked-seats")
    public ResponseEntity<List<Integer>> getBookedSeats(@PathVariable Long scheduleId, 
                                                        @PathVariable String date) {
        List<Integer> bookedSeats = bookingService.getBookedSeats(scheduleId, java.time.LocalDate.parse(date));
        return ResponseEntity.ok(bookedSeats);
    }
}
