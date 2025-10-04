package com.redbus.controller;

import com.redbus.document.BusSearchDocument;
import com.redbus.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class ElasticsearchSearchController {
    
    private final ElasticsearchService elasticsearchService;
    
    @GetMapping("/text")
    public ResponseEntity<List<BusSearchDocument>> searchByText(@RequestParam String query) {
        List<BusSearchDocument> results = elasticsearchService.searchByText(query);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/advanced")
    public ResponseEntity<List<BusSearchDocument>> advancedSearch(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String busType,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        List<BusSearchDocument> results;
        
        if (origin != null && destination != null) {
            if (busType != null && minPrice != null && maxPrice != null) {
                results = elasticsearchService.searchBusesWithFilters(
                    origin, destination, busType, 
                    java.math.BigDecimal.valueOf(Double.parseDouble(minPrice)),
                    java.math.BigDecimal.valueOf(Double.parseDouble(maxPrice))
                );
            } else if (busType != null) {
                results = elasticsearchService.searchBusesByBusType(origin, destination, busType);
            } else if (minPrice != null && maxPrice != null) {
                results = elasticsearchService.searchBusesByPriceRange(
                    origin, destination,
                    java.math.BigDecimal.valueOf(Double.parseDouble(minPrice)),
                    java.math.BigDecimal.valueOf(Double.parseDouble(maxPrice))
                );
            } else {
                results = elasticsearchService.searchBuses(origin, destination);
            }
            
            if (sortBy != null) {
                results = elasticsearchService.searchAndSort(origin, destination, sortBy, sortOrder);
            }
        } else {
            results = List.of();
        }
        
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<BusSearchDocument>> searchAvailableBuses(
            @RequestParam String origin,
            @RequestParam String destination) {
        List<BusSearchDocument> results = elasticsearchService.searchAvailableBuses(origin, destination);
        return ResponseEntity.ok(results);
    }
}
