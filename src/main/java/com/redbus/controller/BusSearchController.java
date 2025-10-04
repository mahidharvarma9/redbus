package com.redbus.controller;

import com.redbus.dto.BusSearchRequest;
import com.redbus.dto.BusSearchResponse;
import com.redbus.service.BusSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class BusSearchController {
    
    private final BusSearchService busSearchService;
    
    @PostMapping("/search")
    public ResponseEntity<List<BusSearchResponse>> searchBuses(@Valid @RequestBody BusSearchRequest request) {
        List<BusSearchResponse> buses = busSearchService.searchBuses(request);
        return ResponseEntity.ok(buses);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<BusSearchResponse>> searchBuses(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String travelDate,
            @RequestParam(required = false) String departureTime,
            @RequestParam(required = false) String busType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "departure") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        BusSearchRequest request = BusSearchRequest.builder()
                .origin(origin)
                .destination(destination)
                .travelDate(java.time.LocalDate.parse(travelDate))
                .departureTime(departureTime != null ? java.time.LocalTime.parse(departureTime) : null)
                .busType(busType)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .sortBy(sortBy)
                .sortOrder(sortOrder)
                .build();
        
        List<BusSearchResponse> buses = busSearchService.searchBuses(request);
        return ResponseEntity.ok(buses);
    }
}
