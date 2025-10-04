package com.redbus.service;

import com.redbus.entity.Bus;
import com.redbus.entity.BusOperator;
import com.redbus.repository.jpa.BusRepository;
import com.redbus.repository.jpa.BusOperatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusServiceTest {

    @Mock
    private BusRepository busRepository;

    @Mock
    private BusOperatorRepository busOperatorRepository;

    @InjectMocks
    private BusService busService;

    private Bus testBus;
    private BusOperator testOperator;

    @BeforeEach
    void setUp() {
        testOperator = BusOperator.builder()
                .id(1L)
                .name("Test Operator")
                .licenseNumber("LIC123")
                .build();

        testBus = Bus.builder()
                .id(1L)
                .busNumber("BUS001")
                .busType(Bus.BusType.AC)
                .totalSeats(50)
                .operator(testOperator)
                .build();
    }

    @Test
    void createBus_Success() {
        // Given
        when(busRepository.existsByBusNumber(anyString())).thenReturn(false);
        when(busOperatorRepository.findById(anyLong())).thenReturn(Optional.of(testOperator));
        when(busRepository.save(any(Bus.class))).thenReturn(testBus);

        // When
        Bus result = busService.createBus(testBus);

        // Then
        assertNotNull(result);
        assertEquals("BUS001", result.getBusNumber());
        verify(busRepository).save(any(Bus.class));
    }

    @Test
    void createBus_BusNumberExists_ThrowsException() {
        // Given
        when(busRepository.existsByBusNumber(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> busService.createBus(testBus));
        assertEquals("Bus number already exists: BUS001", exception.getMessage());
        verify(busRepository, never()).save(any(Bus.class));
    }

    @Test
    void createBus_OperatorNotFound_ThrowsException() {
        // Given
        when(busRepository.existsByBusNumber(anyString())).thenReturn(false);
        when(busOperatorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> busService.createBus(testBus));
        assertEquals("Bus operator not found with id: 1", exception.getMessage());
        verify(busRepository, never()).save(any(Bus.class));
    }

    @Test
    void getBusesByOperator_Success() {
        // Given
        List<Bus> buses = Arrays.asList(testBus);
        when(busRepository.findActiveBusesByOperator(1L)).thenReturn(buses);

        // When
        List<Bus> result = busService.getBusesByOperator(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BUS001", result.get(0).getBusNumber());
    }

    @Test
    void updateBus_Success() {
        // Given
        Bus updatedBus = Bus.builder()
                .busNumber("BUS002")
                .busType(Bus.BusType.NON_AC)
                .totalSeats(40)
                .operator(testOperator)
                .build();

        when(busRepository.findById(1L)).thenReturn(Optional.of(testBus));
        when(busRepository.existsByBusNumber("BUS002")).thenReturn(false);
        when(busRepository.save(any(Bus.class))).thenReturn(updatedBus);

        // When
        Bus result = busService.updateBus(1L, updatedBus);

        // Then
        assertNotNull(result);
        assertEquals("BUS002", result.getBusNumber());
        verify(busRepository).save(any(Bus.class));
    }

    @Test
    void updateBus_BusNotFound_ThrowsException() {
        // Given
        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> busService.updateBus(1L, testBus));
        assertEquals("Bus not found with id: 1", exception.getMessage());
        verify(busRepository, never()).save(any(Bus.class));
    }

    @Test
    void deleteBus_Success() {
        // Given
        when(busRepository.findById(1L)).thenReturn(Optional.of(testBus));
        when(busRepository.save(any(Bus.class))).thenReturn(testBus);

        // When
        busService.deleteBus(1L);

        // Then
        assertFalse(testBus.getIsActive());
        verify(busRepository).save(testBus);
    }
}
