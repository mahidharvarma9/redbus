package com.redbus.service;

import com.redbus.entity.Route;
import com.redbus.repository.jpa.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService {
    
    private final RouteRepository routeRepository;
    
    @Transactional(readOnly = true)
    public List<Route> getAllRoutes() {
        return routeRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Route> getRouteByOriginAndDestination(String origin, String destination) {
        return routeRepository.findByOriginAndDestination(origin, destination);
    }
    
    @Transactional(readOnly = true)
    public List<Route> searchRoutes(String origin, String destination) {
        return routeRepository.findByOriginOrDestinationContaining(origin, destination);
    }
    
    @Transactional
    public Route createRoute(Route route) {
        if (routeRepository.findByOriginAndDestination(route.getOrigin(), route.getDestination()).isPresent()) {
            throw new IllegalArgumentException("Route already exists between " + route.getOrigin() + " and " + route.getDestination());
        }
        return routeRepository.save(route);
    }
    
    @Transactional
    public Route updateRoute(Long id, Route route) {
        Route existingRoute = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with id: " + id));
        
        if (!existingRoute.getOrigin().equals(route.getOrigin()) || 
            !existingRoute.getDestination().equals(route.getDestination())) {
            if (routeRepository.findByOriginAndDestination(route.getOrigin(), route.getDestination()).isPresent()) {
                throw new IllegalArgumentException("Route already exists between " + route.getOrigin() + " and " + route.getDestination());
            }
        }
        
        existingRoute.setOrigin(route.getOrigin());
        existingRoute.setDestination(route.getDestination());
        existingRoute.setDistanceKm(route.getDistanceKm());
        existingRoute.setEstimatedDurationHours(route.getEstimatedDurationHours());
        
        return routeRepository.save(existingRoute);
    }
    
    @Transactional
    public void deleteRoute(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with id: " + id));
        
        route.setIsActive(false);
        routeRepository.save(route);
    }
}
