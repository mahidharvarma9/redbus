package com.redbus.controller;

import com.redbus.dto.BusTrackingRequest;
import com.redbus.dto.BusTrackingResponse;
import com.redbus.entity.Booking;
import com.redbus.service.BookingService;
import com.redbus.service.BusTrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class BusTrackingController {
    
    private final BusTrackingService busTrackingService;
    private final BookingService bookingService;
    
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<BusTrackingResponse> updateLocation(@Valid @RequestBody BusTrackingRequest request) {
        BusTrackingResponse tracking = busTrackingService.updateLocation(request);
        return ResponseEntity.ok(tracking);
    }
    
    @PostMapping("/update-async")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<String> updateLocationAsync(@Valid @RequestBody BusTrackingRequest request) {
        busTrackingService.updateLocationAsync(request);
        return ResponseEntity.ok("Location update initiated");
    }
    
    @GetMapping("/bus/{busId}/current")
    public ResponseEntity<BusTrackingResponse> getCurrentLocation(@PathVariable Long busId) {
        return busTrackingService.getCurrentLocation(busId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/bus/{busId}/history")
    public ResponseEntity<List<BusTrackingResponse>> getTrackingHistory(
            @PathVariable Long busId,
            @RequestParam(required = false) String fromTime,
            @RequestParam(required = false) String toTime) {
        
        List<BusTrackingResponse> trackingData;
        
        if (fromTime != null && toTime != null) {
            LocalDateTime startTime = LocalDateTime.parse(fromTime);
            LocalDateTime endTime = LocalDateTime.parse(toTime);
            trackingData = busTrackingService.getTrackingHistory(busId, startTime, endTime);
        } else if (fromTime != null) {
            LocalDateTime from = LocalDateTime.parse(fromTime);
            trackingData = busTrackingService.getTrackingHistory(busId, from);
        } else {
            // Get last 24 hours by default
            LocalDateTime from = LocalDateTime.now().minusHours(24);
            trackingData = busTrackingService.getTrackingHistory(busId, from);
        }
        
        return ResponseEntity.ok(trackingData);
    }
    
    @GetMapping("/bus/{busId}/latest")
    public ResponseEntity<List<BusTrackingResponse>> getLatestTracking(@PathVariable Long busId) {
        List<BusTrackingResponse> trackingData = busTrackingService.getLatestTrackingByBusId(busId);
        return ResponseEntity.ok(trackingData);
    }
    
    @GetMapping("/all-active")
    public ResponseEntity<List<BusTrackingResponse>> getAllActiveBusLocations() {
        List<BusTrackingResponse> activeBuses = busTrackingService.getAllActiveBusLocations();
        return ResponseEntity.ok(activeBuses);
    }
    
    @GetMapping("/booking/{bookingReference}")
    public ResponseEntity<Map<String, Object>> getTrackingByBookingReference(@PathVariable String bookingReference) {
        // Get booking to verify it exists and extract bus info
        Optional<Booking> bookingOpt = bookingService.getBookingByReference(bookingReference);
        
        if (bookingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Booking booking = bookingOpt.get();
        Long busId = booking.getSchedule().getBus().getId();
        
        // Get current location
        Optional<BusTrackingResponse> currentLocation = busTrackingService.getCurrentLocation(busId);
        
        // Prepare response with booking and tracking info
        Map<String, Object> response = new HashMap<>();
        response.put("bookingReference", booking.getBookingReference());
        response.put("busNumber", booking.getSchedule().getBus().getBusNumber());
        response.put("operatorName", booking.getSchedule().getBus().getOperator().getName());
        response.put("origin", booking.getSchedule().getRoute().getOrigin());
        response.put("destination", booking.getSchedule().getRoute().getDestination());
        response.put("departureTime", booking.getSchedule().getDepartureTime().toString());
        response.put("arrivalTime", booking.getSchedule().getArrivalTime().toString());
        response.put("bookingDate", booking.getBookingDate().toString());
        response.put("totalSeats", booking.getTotalSeats());
        response.put("status", booking.getStatus().name());
        
        if (currentLocation.isPresent()) {
            response.put("tracking", currentLocation.get());
            response.put("trackingAvailable", true);
        } else {
            response.put("trackingAvailable", false);
            response.put("message", "Bus tracking data not available yet. The bus will start transmitting location once the journey begins.");
        }
        
        return ResponseEntity.ok(response);
    }
}
