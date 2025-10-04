package com.redbus.service;

import com.redbus.entity.BusOperator;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusOperatorServiceTest {

    @Mock
    private BusOperatorRepository operatorRepository;

    @InjectMocks
    private BusOperatorService operatorService;

    private BusOperator testOperator;

    @BeforeEach
    void setUp() {
        testOperator = BusOperator.builder()
                .id(1L)
                .name("Test Travels")
                .contactEmail("test@travels.com")
                .contactPhone("1234567890")
                .licenseNumber("LIC123")
                .isActive(true)
                .build();
    }

    @Test
    void getAllOperators_Success() {
        // Given
        List<BusOperator> operators = Arrays.asList(testOperator);
        when(operatorRepository.findByIsActiveTrue()).thenReturn(operators);

        // When
        List<BusOperator> result = operatorService.getAllOperators();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Travels", result.get(0).getName());
        verify(operatorRepository).findByIsActiveTrue();
    }

    @Test
    void getOperatorById_Success() {
        // Given
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(testOperator));

        // When
        Optional<BusOperator> result = operatorService.getOperatorById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Travels", result.get().getName());
        verify(operatorRepository).findById(1L);
    }

    @Test
    void getOperatorById_NotFound() {
        // Given
        when(operatorRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<BusOperator> result = operatorService.getOperatorById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(operatorRepository).findById(999L);
    }

    @Test
    void createOperator_Success() {
        // Given
        when(operatorRepository.existsByLicenseNumber(anyString())).thenReturn(false);
        when(operatorRepository.save(any(BusOperator.class))).thenReturn(testOperator);

        // When
        BusOperator result = operatorService.createOperator(testOperator);

        // Then
        assertNotNull(result);
        assertEquals("Test Travels", result.getName());
        assertEquals("LIC123", result.getLicenseNumber());
        verify(operatorRepository).existsByLicenseNumber("LIC123");
        verify(operatorRepository).save(testOperator);
    }

    @Test
    void createOperator_DuplicateLicense_ThrowsException() {
        // Given
        when(operatorRepository.existsByLicenseNumber(anyString())).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> operatorService.createOperator(testOperator));
        verify(operatorRepository).existsByLicenseNumber("LIC123");
        verify(operatorRepository, never()).save(any(BusOperator.class));
    }

    @Test
    void updateOperator_Success() {
        // Given
        BusOperator updatedOperator = BusOperator.builder()
                .name("Updated Travels")
                .contactEmail("updated@travels.com")
                .contactPhone("9876543210")
                .build();

        when(operatorRepository.findById(1L)).thenReturn(Optional.of(testOperator));
        when(operatorRepository.save(any(BusOperator.class))).thenReturn(testOperator);

        // When
        BusOperator result = operatorService.updateOperator(1L, updatedOperator);

        // Then
        assertNotNull(result);
        verify(operatorRepository).findById(1L);
        verify(operatorRepository).save(testOperator);
    }

    @Test
    void updateOperator_NotFound_ThrowsException() {
        // Given
        when(operatorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> operatorService.updateOperator(999L, testOperator));
        verify(operatorRepository).findById(999L);
        verify(operatorRepository, never()).save(any(BusOperator.class));
    }

    @Test
    void deleteOperator_Success() {
        // Given
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(testOperator));

        // When
        operatorService.deleteOperator(1L);

        // Then
        assertFalse(testOperator.getIsActive());
        verify(operatorRepository).findById(1L);
        verify(operatorRepository).save(testOperator);
    }

    @Test
    void deleteOperator_NotFound_ThrowsException() {
        // Given
        when(operatorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> operatorService.deleteOperator(999L));
        verify(operatorRepository).findById(999L);
        verify(operatorRepository, never()).save(any(BusOperator.class));
    }
}

