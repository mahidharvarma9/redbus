package com.redbus.service;

import com.redbus.entity.*;
import com.redbus.repository.jpa.ScheduleRepository;
import com.redbus.repository.jpa.BusRepository;
import com.redbus.repository.jpa.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private BusRepository busRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private ElasticsearchService elasticsearchService;

    @InjectMocks
    private ScheduleService scheduleService;

    private Schedule testSchedule;
    private Bus testBus;
    private Route testRoute;
    private BusOperator testOperator;

    @BeforeEach
    void setUp() {
        testOperator = BusOperator.builder()
                .id(1L)
                .name("Test Operator")
                .contactEmail("operator@test.com")
                .build();

        testBus = Bus.builder()
                .id(1L)
                .busNumber("BUS001")
                .totalSeats(50)
                .operator(testOperator)
                .busType(Bus.BusType.AC)
                .build();

        testRoute = Route.builder()
                .id(1L)
                .origin("Mumbai")
                .destination("Pune")
                .build();

        testSchedule = Schedule.builder()
                .id(1L)
                .departureTime(LocalTime.of(10, 0))
                .arrivalTime(LocalTime.of(14, 0))
                .price(BigDecimal.valueOf(500))
                .bus(testBus)
                .route(testRoute)
                .isActive(true)
                .isRecurring(false)
                .build();
    }

    @Test
    void getAllSchedules_Success() {
        // Given
        List<Schedule> schedules = Arrays.asList(testSchedule);
        when(scheduleRepository.findAll()).thenReturn(schedules);

        // When
        List<Schedule> result = scheduleService.getAllSchedules();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scheduleRepository).findAll();
    }

    @Test
    void getScheduleById_Success() {
        // Given
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(testSchedule));

        // When
        Optional<Schedule> result = scheduleService.getScheduleById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(LocalTime.of(10, 0), result.get().getDepartureTime());
        verify(scheduleRepository).findById(1L);
    }

    @Test
    void getScheduleById_NotFound() {
        // Given
        when(scheduleRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Schedule> result = scheduleService.getScheduleById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(scheduleRepository).findById(999L);
    }

    @Test
    void getSchedulesByRoute_Success() {
        // Given
        List<Schedule> schedules = Arrays.asList(testSchedule);
        when(scheduleRepository.findSchedulesByRoute("Mumbai", "Pune"))
                .thenReturn(schedules);

        // When
        List<Schedule> result = scheduleService.getSchedulesByRoute("Mumbai", "Pune");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scheduleRepository).findSchedulesByRoute("Mumbai", "Pune");
    }

    @Test
    void getSchedulesByRouteAndTime_Success() {
        // Given
        LocalTime departureTime = LocalTime.of(10, 0);
        List<Schedule> schedules = Arrays.asList(testSchedule);
        when(scheduleRepository.findSchedulesByRouteAndTime("Mumbai", "Pune", departureTime))
                .thenReturn(schedules);

        // When
        List<Schedule> result = scheduleService.getSchedulesByRouteAndTime("Mumbai", "Pune", departureTime);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scheduleRepository).findSchedulesByRouteAndTime("Mumbai", "Pune", departureTime);
    }

    @Test
    void getSchedulesByBusType_Success() {
        // Given
        List<Schedule> schedules = Arrays.asList(testSchedule);
        when(scheduleRepository.findSchedulesByBusType("AC")).thenReturn(schedules);

        // When
        List<Schedule> result = scheduleService.getSchedulesByBusType("AC");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scheduleRepository).findSchedulesByBusType("AC");
    }

    @Test
    void createSchedule_Success() {
        // Given
        when(busRepository.findById(1L)).thenReturn(Optional.of(testBus));
        when(routeRepository.findById(1L)).thenReturn(Optional.of(testRoute));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(testSchedule);
        when(elasticsearchService.indexSchedule(any(Schedule.class))).thenReturn(true);

        // When
        Schedule result = scheduleService.createSchedule(testSchedule);

        // Then
        assertNotNull(result);
        assertEquals(testBus, result.getBus());
        assertEquals(testRoute, result.getRoute());
        verify(busRepository).findById(1L);
        verify(routeRepository).findById(1L);
        verify(scheduleRepository).save(testSchedule);
        verify(elasticsearchService).indexSchedule(testSchedule);
    }

    @Test
    void createSchedule_BusNotFound_ThrowsException() {
        // Given
        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> scheduleService.createSchedule(testSchedule));
        verify(busRepository).findById(1L);
        verify(routeRepository, never()).findById(any());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void createSchedule_RouteNotFound_ThrowsException() {
        // Given
        when(busRepository.findById(1L)).thenReturn(Optional.of(testBus));
        when(routeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> scheduleService.createSchedule(testSchedule));
        verify(busRepository).findById(1L);
        verify(routeRepository).findById(1L);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void updateSchedule_Success() {
        // Given
        Schedule updatedSchedule = Schedule.builder()
                .price(BigDecimal.valueOf(600))
                .departureTime(LocalTime.of(11, 0))
                .arrivalTime(LocalTime.of(15, 0))
                .isRecurring(true)
                .daysOfWeek(Arrays.asList(1, 2, 3, 4, 5))
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(testSchedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(testSchedule);
        when(elasticsearchService.indexSchedule(any(Schedule.class))).thenReturn(true);

        // When
        Schedule result = scheduleService.updateSchedule(1L, updatedSchedule);

        // Then
        assertNotNull(result);
        verify(scheduleRepository).findById(1L);
        verify(scheduleRepository).save(testSchedule);
        verify(elasticsearchService).indexSchedule(testSchedule);
    }

    @Test
    void updateSchedule_NotFound_ThrowsException() {
        // Given
        when(scheduleRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> scheduleService.updateSchedule(999L, testSchedule));
        verify(scheduleRepository).findById(999L);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void deleteSchedule_Success() {
        // Given
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(testSchedule));

        // When
        scheduleService.deleteSchedule(1L);

        // Then
        assertFalse(testSchedule.getIsActive());
        verify(scheduleRepository).findById(1L);
        verify(scheduleRepository).save(testSchedule);
        verify(elasticsearchService).deleteSchedule(1L);
    }

    @Test
    void deleteSchedule_NotFound_ThrowsException() {
        // Given
        when(scheduleRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> scheduleService.deleteSchedule(999L));
        verify(scheduleRepository).findById(999L);
        verify(scheduleRepository, never()).save(any());
    }
}

