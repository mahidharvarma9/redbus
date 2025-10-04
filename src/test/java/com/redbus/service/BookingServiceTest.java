package com.redbus.service;

import com.redbus.entity.*;
import com.redbus.repository.jpa.BookingRepository;
import com.redbus.repository.jpa.ScheduleRepository;
import com.redbus.repository.jpa.SeatBookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private SeatBookingRepository seatBookingRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingService bookingService;

    private User testUser;
    private Schedule testSchedule;
    private Bus testBus;
    private Route testRoute;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        testRoute = Route.builder()
                .id(1L)
                .origin("Mumbai")
                .destination("Pune")
                .build();

        BusOperator testOperator = BusOperator.builder()
                .id(1L)
                .name("Test Operator")
                .contactEmail("operator@test.com")
                .build();

        testBus = Bus.builder()
                .id(1L)
                .busNumber("BUS001")
                .totalSeats(50)
                .operator(testOperator)
                .build();

        testSchedule = Schedule.builder()
                .id(1L)
                .departureTime(LocalTime.of(10, 0))
                .arrivalTime(LocalTime.of(14, 0))
                .price(BigDecimal.valueOf(500))
                .bus(testBus)
                .route(testRoute)
                .build();
    }

    @Test
    void createBooking_Success() {
        // Given
        com.redbus.dto.BookingRequest request = com.redbus.dto.BookingRequest.builder()
                .scheduleId(1L)
                .travelDate(LocalDate.now().plusDays(1))
                .passengers(Arrays.asList(
                    com.redbus.dto.SeatBookingRequest.PassengerInfo.builder()
                        .seatNumber(1)
                        .passengerName("John Doe")
                        .passengerAge(30)
                        .passengerGender(SeatBooking.Gender.MALE)
                        .build()
                ))
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(testSchedule));
        when(bookingRepository.findBookedSeatNumbers(1L, request.getTravelDate())).thenReturn(Arrays.asList());
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(1L);
            return booking;
        });
        when(seatBookingRepository.save(any(SeatBooking.class))).thenReturn(new SeatBooking());

        // When
        com.redbus.dto.BookingResponse result = bookingService.createBooking(request, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalSeats());
        verify(bookingRepository).save(any(Booking.class));
        verify(seatBookingRepository).save(any(SeatBooking.class));
    }

    @Test
    void createBooking_ScheduleNotFound_ThrowsException() {
        // Given
        com.redbus.dto.BookingRequest request = com.redbus.dto.BookingRequest.builder()
                .scheduleId(999L)
                .travelDate(LocalDate.now().plusDays(1))
                .passengers(Arrays.asList())
                .build();

        when(scheduleRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> bookingService.createBooking(request, 1L));
        assertEquals("Schedule not found", exception.getMessage());
    }

    @Test
    void createBooking_SeatAlreadyBooked_ThrowsException() {
        // Given
        com.redbus.dto.BookingRequest request = com.redbus.dto.BookingRequest.builder()
                .scheduleId(1L)
                .travelDate(LocalDate.now().plusDays(1))
                .passengers(Arrays.asList(
                    com.redbus.dto.SeatBookingRequest.PassengerInfo.builder()
                        .seatNumber(1)
                        .passengerName("John Doe")
                        .passengerAge(30)
                        .passengerGender(SeatBooking.Gender.MALE)
                        .build()
                ))
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(testSchedule));
        when(bookingRepository.findBookedSeatNumbers(1L, request.getTravelDate())).thenReturn(Arrays.asList(1));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> bookingService.createBooking(request, 1L));
        assertEquals("Seat 1 is already booked", exception.getMessage());
    }

    @Test
    void cancelBooking_Success() {
        // Given
        Booking testBooking = Booking.builder()
                .id(1L)
                .user(testUser)
                .status(Booking.BookingStatus.PENDING)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // When
        bookingService.cancelBooking(1L, 1L);

        // Then
        assertEquals(Booking.BookingStatus.CANCELLED, testBooking.getStatus());
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void cancelBooking_NotOwnBooking_ThrowsException() {
        // Given
        User otherUser = User.builder().id(2L).build();
        Booking testBooking = Booking.builder()
                .id(1L)
                .user(otherUser)
                .status(Booking.BookingStatus.PENDING)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> bookingService.cancelBooking(1L, 1L));
        assertEquals("You can only cancel your own bookings", exception.getMessage());
    }

    @Test
    void cancelBooking_AlreadyCancelled_ThrowsException() {
        // Given
        Booking testBooking = Booking.builder()
                .id(1L)
                .user(testUser)
                .status(Booking.BookingStatus.CANCELLED)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> bookingService.cancelBooking(1L, 1L));
        assertEquals("Cannot cancel booking with status: CANCELLED", exception.getMessage());
    }
}
