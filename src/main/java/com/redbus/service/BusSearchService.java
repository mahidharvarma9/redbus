package com.redbus.service;

import com.redbus.dto.BusSearchRequest;
import com.redbus.dto.BusSearchResponse;
import com.redbus.document.BusSearchDocument;
import com.redbus.entity.Schedule;
import com.redbus.repository.jpa.ScheduleRepository;
import com.redbus.repository.jpa.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusSearchService {
    
    private final ScheduleRepository scheduleRepository;
    private final BookingRepository bookingRepository;
    private final ElasticsearchService elasticsearchService;
    
    @Transactional(readOnly = true)
    public List<BusSearchResponse> searchBuses(BusSearchRequest request) {
        List<BusSearchDocument> documents;
        
        // Use Elasticsearch for powerful search
        if (request.getBusType() != null && request.getMinPrice() != null && request.getMaxPrice() != null) {
            documents = elasticsearchService.searchBusesWithFilters(
                request.getOrigin(), 
                request.getDestination(), 
                request.getBusType(),
                request.getMinPrice() != null ? BigDecimal.valueOf(request.getMinPrice()) : null,
                request.getMaxPrice() != null ? BigDecimal.valueOf(request.getMaxPrice()) : null
            );
        } else if (request.getBusType() != null) {
            documents = elasticsearchService.searchBusesByBusType(
                request.getOrigin(), 
                request.getDestination(), 
                request.getBusType()
            );
        } else if (request.getMinPrice() != null && request.getMaxPrice() != null) {
            documents = elasticsearchService.searchBusesByPriceRange(
                request.getOrigin(), 
                request.getDestination(), 
                request.getMinPrice() != null ? BigDecimal.valueOf(request.getMinPrice()) : null,
                request.getMaxPrice() != null ? BigDecimal.valueOf(request.getMaxPrice()) : null
            );
        } else {
            documents = elasticsearchService.searchBuses(request.getOrigin(), request.getDestination());
        }
        
        // Apply sorting if specified
        if (request.getSortBy() != null) {
            documents = applyElasticsearchSorting(documents, request.getSortBy(), request.getSortOrder());
        }
        
        // Convert to response DTOs and filter by available seats
        return documents.stream()
            .filter(doc -> doc.getAvailableSeats() > 0) // Only show buses with available seats
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    private List<BusSearchDocument> applyElasticsearchSorting(List<BusSearchDocument> documents, String sortBy, String sortOrder) {
        return documents.stream()
            .sorted((d1, d2) -> {
                int comparison = 0;
                switch (sortBy.toLowerCase()) {
                    case "price":
                        comparison = d1.getPrice().compareTo(d2.getPrice());
                        break;
                    case "departure":
                        comparison = d1.getDepartureTime().compareTo(d2.getDepartureTime());
                        break;
                    case "duration":
                        comparison = Integer.compare(d1.getDurationMinutes(), d2.getDurationMinutes());
                        break;
                    default:
                        comparison = d1.getDepartureTime().compareTo(d2.getDepartureTime());
                }
                return "desc".equalsIgnoreCase(sortOrder) ? -comparison : comparison;
            })
            .collect(Collectors.toList());
    }
    
    private BusSearchResponse convertToResponse(BusSearchDocument document) {
        return BusSearchResponse.builder()
                .scheduleId(document.getScheduleId())
                .busId(document.getBusId())
                .busNumber(document.getBusNumber())
                .busType(document.getBusType())
                .operatorName(document.getOperatorName())
                .origin(document.getOrigin())
                .destination(document.getDestination())
                .departureTime(LocalTime.parse(document.getDepartureTime()))
                .arrivalTime(LocalTime.parse(document.getArrivalTime()))
                .price(document.getPrice())
                .totalSeats(document.getTotalSeats())
                .availableSeats(document.getAvailableSeats())
                .amenities(document.getAmenities())
                .duration(formatDuration(document.getDurationMinutes()))
                .build();
    }
    
    private String formatDuration(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%dh %dm", hours, mins);
    }
    
    private int calculateAvailableSeats(Long scheduleId, LocalDate travelDate) {
        Long bookedSeats = bookingRepository.countBookedSeatsByScheduleAndDate(scheduleId, travelDate);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if (schedule == null) return 0;
        
        return schedule.getBus().getTotalSeats() - bookedSeats.intValue();
    }
}
