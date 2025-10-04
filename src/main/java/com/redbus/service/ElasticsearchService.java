package com.redbus.service;

import com.redbus.document.BusSearchDocument;
import com.redbus.entity.Schedule;
import com.redbus.repository.elasticsearch.BusSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {
    
    private final BusSearchRepository busSearchRepository;
    
    public boolean indexSchedule(Schedule schedule) {
        String documentId = schedule.getId().toString();
        
        // Check if document already exists
        if (busSearchRepository.existsById(documentId)) {
            // Check if the schedule has been updated since last sync
            var existingDoc = busSearchRepository.findById(documentId);
            if (existingDoc.isPresent()) {
                String existingUpdatedAt = existingDoc.get().getUpdatedAt();
                String currentUpdatedAt = schedule.getUpdatedAt().toString();
                
                // Only update if the schedule has been modified
                if (existingUpdatedAt.equals(currentUpdatedAt)) {
                    return false; // No update needed
                }
            }
        }
        
        BusSearchDocument document = BusSearchDocument.builder()
                .id(documentId)
                .scheduleId(schedule.getId())
                .busId(schedule.getBus().getId())
                .busNumber(schedule.getBus().getBusNumber())
                .busType(schedule.getBus().getBusType().name())
                .operatorName(schedule.getBus().getOperator().getName())
                .operatorId(schedule.getBus().getOperator().getId())
                .origin(schedule.getRoute().getOrigin())
                .destination(schedule.getRoute().getDestination())
                .route(schedule.getRoute().getOrigin() + " to " + schedule.getRoute().getDestination())
                .departureTime(schedule.getDepartureTime().format(DateTimeFormatter.ISO_LOCAL_TIME))
                .arrivalTime(schedule.getArrivalTime().format(DateTimeFormatter.ISO_LOCAL_TIME))
                .price(schedule.getPrice())
                .totalSeats(schedule.getBus().getTotalSeats())
                .availableSeats(schedule.getBus().getTotalSeats()) // Will be updated based on bookings
                .amenities(schedule.getBus().getAmenities())
                .distanceKm(schedule.getRoute().getDistanceKm())
                .estimatedDurationHours(schedule.getRoute().getEstimatedDurationHours())
                .durationMinutes(calculateDurationMinutes(schedule.getDepartureTime(), schedule.getArrivalTime()))
                .isActive(schedule.getIsActive())
                .isRecurring(schedule.getIsRecurring())
                .daysOfWeek(schedule.getDaysOfWeek())
                .createdAt(schedule.getCreatedAt().toString())
                .updatedAt(schedule.getUpdatedAt().toString())
                .build();
        
        busSearchRepository.save(document);
        return true; // Document was indexed/updated
    }
    
    @Transactional
    public void updateAvailableSeats(Long scheduleId, Integer availableSeats) {
        busSearchRepository.findById(scheduleId.toString())
                .ifPresent(document -> {
                    document.setAvailableSeats(availableSeats);
                    busSearchRepository.save(document);
                });
    }
    
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        busSearchRepository.deleteById(scheduleId.toString());
    }
    
    public List<BusSearchDocument> searchBuses(String origin, String destination) {
        return busSearchRepository.findByOriginAndDestination(origin, destination);
    }
    
    public List<BusSearchDocument> searchBusesByBusType(String origin, String destination, String busType) {
        return busSearchRepository.findByOriginAndDestinationAndBusType(origin, destination, busType);
    }
    
    public List<BusSearchDocument> searchBusesByPriceRange(String origin, String destination, 
                                                          BigDecimal minPrice, BigDecimal maxPrice) {
        return busSearchRepository.findByOriginAndDestinationAndPriceBetween(origin, destination, minPrice, maxPrice);
    }
    
    public List<BusSearchDocument> searchBusesWithFilters(String origin, String destination, String busType, 
                                                         BigDecimal minPrice, BigDecimal maxPrice) {
        if (busType != null && minPrice != null && maxPrice != null) {
            return busSearchRepository.findByOriginAndDestinationAndBusTypeAndPriceBetween(
                origin, destination, busType, minPrice, maxPrice);
        } else if (busType != null) {
            return busSearchRepository.findByOriginAndDestinationAndBusType(origin, destination, busType);
        } else if (minPrice != null && maxPrice != null) {
            return busSearchRepository.findByOriginAndDestinationAndPriceBetween(origin, destination, minPrice, maxPrice);
        } else {
            return busSearchRepository.findByOriginAndDestination(origin, destination);
        }
    }
    
    public List<BusSearchDocument> searchAvailableBuses(String origin, String destination) {
        return busSearchRepository.searchAvailableBusesByRoute(origin, destination);
    }
    
    public List<BusSearchDocument> searchAndSort(String origin, String destination, String sortField, String sortOrder) {
        return busSearchRepository.searchAndSortByRoute(origin, destination, sortField, sortOrder);
    }
    
    public List<BusSearchDocument> searchByText(String searchText) {
        return busSearchRepository.searchByText(searchText);
    }
    
    public int getDocumentCount() {
        return (int) busSearchRepository.count();
    }
    
    private int calculateDurationMinutes(LocalTime departure, LocalTime arrival) {
        int departureMinutes = departure.getHour() * 60 + departure.getMinute();
        int arrivalMinutes = arrival.getHour() * 60 + arrival.getMinute();
        
        if (arrivalMinutes < departureMinutes) {
            arrivalMinutes += 24 * 60; // Next day
        }
        
        return arrivalMinutes - departureMinutes;
    }
}
