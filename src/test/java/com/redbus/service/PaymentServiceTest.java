package com.redbus.service;

import com.redbus.entity.*;
import com.redbus.repository.jpa.PaymentRepository;
import com.redbus.repository.jpa.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment testPayment;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testBooking = Booking.builder()
                .id(1L)
                .bookingReference("BK123")
                .totalAmount(BigDecimal.valueOf(1000))
                .status(Booking.BookingStatus.CONFIRMED)
                .build();

        testPayment = Payment.builder()
                .id(1L)
                .booking(testBooking)
                .amount(BigDecimal.valueOf(1000))
                .paymentMethod(Payment.PaymentMethod.CARD)
                .paymentStatus(Payment.PaymentStatus.SUCCESS)
                .transactionId("TXN123")
                .build();
    }

    @Test
    void getPaymentsByBookingId_Success() {
        // Given
        List<Payment> payments = Arrays.asList(testPayment);
        when(paymentRepository.findByBookingId(1L)).thenReturn(payments);

        // When
        List<Payment> result = paymentService.getPaymentsByBookingId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TXN123", result.get(0).getTransactionId());
        verify(paymentRepository).findByBookingId(1L);
    }

    @Test
    void getPaymentById_Success() {
        // Given
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

        // When
        Optional<Payment> result = paymentService.getPaymentById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("TXN123", result.get().getTransactionId());
        verify(paymentRepository).findById(1L);
    }

    @Test
    void getPaymentById_NotFound() {
        // Given
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Payment> result = paymentService.getPaymentById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(paymentRepository).findById(999L);
    }


    @Test
    void updatePaymentStatus_Success() {
        // Given
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // When
        var result = paymentService.updatePaymentStatus(1L, Payment.PaymentStatus.SUCCESS);

        // Then
        assertNotNull(result);
        verify(paymentRepository).findById(1L);
        verify(paymentRepository).save(testPayment);
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void updatePaymentStatus_NotFound_ThrowsException() {
        // Given
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> paymentService.updatePaymentStatus(999L, Payment.PaymentStatus.REFUNDED));
        verify(paymentRepository).findById(999L);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void refundPayment_Success() {
        // Given
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // When
        var result = paymentService.refundPayment(1L);

        // Then
        assertNotNull(result);
        verify(paymentRepository).findById(1L);
        verify(paymentRepository).save(testPayment);
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void refundPayment_NotSuccessful_ThrowsException() {
        // Given
        testPayment.setPaymentStatus(Payment.PaymentStatus.FAILED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> paymentService.refundPayment(1L));
        verify(paymentRepository).findById(1L);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void getPaymentByTransactionId_Success() {
        // Given
        when(paymentRepository.findByTransactionId("TXN123"))
                .thenReturn(Optional.of(testPayment));

        // When
        Optional<Payment> result = paymentService.getPaymentByTransactionId("TXN123");

        // Then
        assertTrue(result.isPresent());
        assertEquals("TXN123", result.get().getTransactionId());
        verify(paymentRepository).findByTransactionId("TXN123");
    }

    @Test
    void getPaymentByTransactionId_NotFound() {
        // Given
        when(paymentRepository.findByTransactionId("INVALID"))
                .thenReturn(Optional.empty());

        // When
        Optional<Payment> result = paymentService.getPaymentByTransactionId("INVALID");

        // Then
        assertFalse(result.isPresent());
        verify(paymentRepository).findByTransactionId("INVALID");
    }
}

