package com.redbus.service;

import com.redbus.entity.Route;
import com.redbus.repository.jpa.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    private Route testRoute;

    @BeforeEach
    void setUp() {
        testRoute = Route.builder()
                .id(1L)
                .origin("Mumbai")
                .destination("Pune")
                .distanceKm(BigDecimal.valueOf(150))
                .estimatedDurationHours(BigDecimal.valueOf(3.5))
                .isActive(true)
                .build();
    }

    @Test
    void getAllRoutes_Success() {
        // Given
        List<Route> routes = Arrays.asList(testRoute);
        when(routeRepository.findByIsActiveTrue()).thenReturn(routes);

        // When
        List<Route> result = routeService.getAllRoutes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mumbai", result.get(0).getOrigin());
        verify(routeRepository).findByIsActiveTrue();
    }

    @Test
    void getRouteById_Success() {
        // Given
        when(routeRepository.findById(1L)).thenReturn(Optional.of(testRoute));

        // When
        Optional<Route> result = routeService.getRouteById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Mumbai", result.get().getOrigin());
        verify(routeRepository).findById(1L);
    }

    @Test
    void getRouteById_NotFound() {
        // Given
        when(routeRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Route> result = routeService.getRouteById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(routeRepository).findById(999L);
    }

    @Test
    void getRouteByOriginAndDestination_Success() {
        // Given
        when(routeRepository.findByOriginAndDestination("Mumbai", "Pune"))
                .thenReturn(Optional.of(testRoute));

        // When
        Optional<Route> result = routeService.getRouteByOriginAndDestination("Mumbai", "Pune");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Mumbai", result.get().getOrigin());
        assertEquals("Pune", result.get().getDestination());
        verify(routeRepository).findByOriginAndDestination("Mumbai", "Pune");
    }

    @Test
    void searchRoutes_Success() {
        // Given
        List<Route> routes = Arrays.asList(testRoute);
        when(routeRepository.findByOriginOrDestinationContaining("Mumbai", "Mumbai"))
                .thenReturn(routes);

        // When
        List<Route> result = routeService.searchRoutes("Mumbai", "Mumbai");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(routeRepository).findByOriginOrDestinationContaining("Mumbai", "Mumbai");
    }

    @Test
    void createRoute_Success() {
        // Given
        when(routeRepository.findByOriginAndDestination(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(routeRepository.save(any(Route.class))).thenReturn(testRoute);

        // When
        Route result = routeService.createRoute(testRoute);

        // Then
        assertNotNull(result);
        assertEquals("Mumbai", result.getOrigin());
        assertEquals("Pune", result.getDestination());
        verify(routeRepository).findByOriginAndDestination("Mumbai", "Pune");
        verify(routeRepository).save(testRoute);
    }

    @Test
    void createRoute_AlreadyExists_ThrowsException() {
        // Given
        when(routeRepository.findByOriginAndDestination(anyString(), anyString()))
                .thenReturn(Optional.of(testRoute));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> routeService.createRoute(testRoute));
        verify(routeRepository).findByOriginAndDestination("Mumbai", "Pune");
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void updateRoute_Success() {
        // Given
        Route updatedRoute = Route.builder()
                .origin("Mumbai")
                .destination("Pune")
                .distanceKm(BigDecimal.valueOf(160))
                .estimatedDurationHours(BigDecimal.valueOf(3.0))
                .build();

        when(routeRepository.findById(1L)).thenReturn(Optional.of(testRoute));
        when(routeRepository.save(any(Route.class))).thenReturn(testRoute);

        // When
        Route result = routeService.updateRoute(1L, updatedRoute);

        // Then
        assertNotNull(result);
        verify(routeRepository).findById(1L);
        verify(routeRepository).save(testRoute);
    }

    @Test
    void updateRoute_NotFound_ThrowsException() {
        // Given
        when(routeRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> routeService.updateRoute(999L, testRoute));
        verify(routeRepository).findById(999L);
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void deleteRoute_Success() {
        // Given
        when(routeRepository.findById(1L)).thenReturn(Optional.of(testRoute));

        // When
        routeService.deleteRoute(1L);

        // Then
        assertFalse(testRoute.getIsActive());
        verify(routeRepository).findById(1L);
        verify(routeRepository).save(testRoute);
    }

    @Test
    void deleteRoute_NotFound_ThrowsException() {
        // Given
        when(routeRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> routeService.deleteRoute(999L));
        verify(routeRepository).findById(999L);
        verify(routeRepository, never()).save(any(Route.class));
    }
}

