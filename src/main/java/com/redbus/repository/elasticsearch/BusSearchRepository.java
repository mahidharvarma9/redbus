package com.redbus.repository.elasticsearch;

import com.redbus.document.BusSearchDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BusSearchRepository extends ElasticsearchRepository<BusSearchDocument, String> {
    
    List<BusSearchDocument> findByOriginAndDestination(String origin, String destination);
    
    List<BusSearchDocument> findByOriginAndDestinationAndBusType(String origin, String destination, String busType);
    
    List<BusSearchDocument> findByOriginAndDestinationAndPriceBetween(String origin, String destination, 
                                                                      BigDecimal minPrice, BigDecimal maxPrice);
    
    List<BusSearchDocument> findByOriginAndDestinationAndBusTypeAndPriceBetween(String origin, String destination, 
                                                                                String busType, BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("{\"bool\": {\"must\": [{\"match\": {\"origin\": \"?0\"}}, {\"match\": {\"destination\": \"?1\"}}], \"filter\": [{\"range\": {\"price\": {\"gte\": ?2, \"lte\": ?3}}}]}}")
    List<BusSearchDocument> searchByRouteAndPriceRange(String origin, String destination, 
                                                      BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("{\"bool\": {\"must\": [{\"match\": {\"origin\": \"?0\"}}, {\"match\": {\"destination\": \"?1\"}}], \"filter\": [{\"term\": {\"busType\": \"?2\"}}]}}")
    List<BusSearchDocument> searchByRouteAndBusType(String origin, String destination, String busType);
    
    @Query("{\"bool\": {\"must\": [{\"match\": {\"origin\": \"?0\"}}, {\"match\": {\"destination\": \"?1\"}}], \"filter\": [{\"range\": {\"departureTime\": {\"gte\": \"?2\"}}}]}}")
    List<BusSearchDocument> searchByRouteAndDepartureTime(String origin, String destination, String departureTime);
    
    @Query("{\"bool\": {\"must\": [{\"match\": {\"origin\": \"?0\"}}, {\"match\": {\"destination\": \"?1\"}}], \"filter\": [{\"term\": {\"busType\": \"?2\"}}, {\"range\": {\"price\": {\"gte\": ?3, \"lte\": ?4}}}]}}")
    List<BusSearchDocument> searchByRouteBusTypeAndPriceRange(String origin, String destination, String busType, 
                                                             BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("{\"bool\": {\"must\": [{\"match\": {\"origin\": \"?0\"}}, {\"match\": {\"destination\": \"?1\"}}], \"filter\": [{\"range\": {\"availableSeats\": {\"gt\": 0}}}]}}")
    List<BusSearchDocument> searchAvailableBusesByRoute(String origin, String destination);
    
    @Query("{\"bool\": {\"must\": [{\"match\": {\"origin\": \"?0\"}}, {\"match\": {\"destination\": \"?1\"}}], \"filter\": [{\"range\": {\"availableSeats\": {\"gt\": 0}}}]}, \"sort\": [{\"?2\": {\"order\": \"?3\"}}]}")
    List<BusSearchDocument> searchAndSortByRoute(String origin, String destination, String sortField, String sortOrder);
    
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"origin\", \"destination\", \"operatorName\", \"busNumber\"]}}], \"filter\": [{\"range\": {\"availableSeats\": {\"gt\": 0}}}]}}")
    List<BusSearchDocument> searchByText(String searchText);
}
