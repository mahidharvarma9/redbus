package com.redbus.service;

import com.redbus.dto.BusTrackingRequest;
import com.redbus.dto.BusTrackingResponse;
import com.redbus.entity.Bus;
import com.redbus.entity.BusTracking;
import com.redbus.repository.jpa.BusTrackingRepository;
import com.redbus.repository.jpa.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusTrackingService {
    
    private final BusTrackingRepository busTrackingRepository;
    private final BusRepository busRepository;
    
    @Transactional(readOnly = true)
    public List<BusTrackingResponse> getLatestTrackingByBusId(Long busId) {
        Optional<BusTracking> trackingData = busTrackingRepository.findLatestTrackingByBusId(busId);
        return trackingData.map(BusTrackingResponse::fromBusTracking)
                .map(List::of)
                .orElse(List.of());
    }
    
    @Transactional(readOnly = true)
    public Optional<BusTrackingResponse> getCurrentLocation(Long busId) {
        return busTrackingRepository.findLatestTrackingByBusId(busId)
                .stream()
                .findFirst()
                .map(BusTrackingResponse::fromBusTracking);
    }
    
    @Transactional(readOnly = true)
    public List<BusTrackingResponse> getTrackingHistory(Long busId, LocalDateTime fromTime) {
        List<BusTracking> trackingData = busTrackingRepository.findTrackingByBusIdAndTimeRange(busId, fromTime);
        return trackingData.stream()
                .map(BusTrackingResponse::fromBusTracking)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BusTrackingResponse> getTrackingHistory(Long busId, LocalDateTime startTime, LocalDateTime endTime) {
        List<BusTracking> trackingData = busTrackingRepository.findTrackingByBusIdAndTimeWindow(busId, startTime, endTime);
        return trackingData.stream()
                .map(BusTrackingResponse::fromBusTracking)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public BusTrackingResponse updateLocation(BusTrackingRequest request) {
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with id: " + request.getBusId()));
        
        BusTracking tracking = BusTracking.builder()
                .bus(bus)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .speedKmh(request.getSpeedKmh())
                .directionDegrees(request.getDirectionDegrees())
                .timestamp(LocalDateTime.now())
                .build();
        
        tracking = busTrackingRepository.save(tracking);
        return BusTrackingResponse.fromBusTracking(tracking);
    }
    
    @Async
    @Transactional
    public void updateLocationAsync(BusTrackingRequest request) {
        updateLocation(request);
    }
    
    @Transactional(readOnly = true)
    public List<BusTrackingResponse> getAllActiveBusLocations() {
        // Get all active buses and their latest locations
        List<Bus> activeBuses = busRepository.findAll().stream()
                .filter(bus -> bus.getIsActive())
                .collect(Collectors.toList());
        
        return activeBuses.stream()
                .map(bus -> getCurrentLocation(bus.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
