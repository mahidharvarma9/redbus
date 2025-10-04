package com.redbus.service;

import com.redbus.dto.PaymentRequest;
import com.redbus.dto.PaymentResponse;
import com.redbus.entity.Booking;
import com.redbus.entity.Payment;
import com.redbus.repository.jpa.PaymentRepository;
import com.redbus.repository.jpa.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }
    
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        // Validate booking exists
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        
        // Validate amount matches booking amount
        if (request.getAmount().compareTo(booking.getTotalAmount()) != 0) {
            throw new IllegalArgumentException("Payment amount does not match booking amount");
        }
        
        // Create payment record
        Payment payment = request.toPayment();
        payment.setBooking(booking);
        
        if (payment.getTransactionId() == null) {
            payment.setTransactionId(generateTransactionId());
        }
        
        // Simulate payment processing (in real app, integrate with payment gateway)
        payment = processPaymentGateway(payment);
        
        payment = paymentRepository.save(payment);
        
        // Update booking status if payment is successful
        if (payment.getPaymentStatus() == Payment.PaymentStatus.SUCCESS) {
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
        }
        
        return PaymentResponse.fromPayment(payment);
    }
    
    @Transactional
    public PaymentResponse updatePaymentStatus(Long paymentId, Payment.PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        
        payment.setPaymentStatus(status);
        payment = paymentRepository.save(payment);
        
        // Update booking status if payment is successful
        if (status == Payment.PaymentStatus.SUCCESS) {
            Booking booking = payment.getBooking();
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
        }
        
        return PaymentResponse.fromPayment(payment);
    }
    
    @Transactional
    public PaymentResponse refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        
        if (payment.getPaymentStatus() != Payment.PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException("Can only refund successful payments");
        }
        
        payment.setPaymentStatus(Payment.PaymentStatus.REFUNDED);
        payment = paymentRepository.save(payment);
        
        // Update booking status
        Booking booking = payment.getBooking();
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        
        return PaymentResponse.fromPayment(payment);
    }
    
    private Payment processPaymentGateway(Payment payment) {
        // Simulate payment gateway processing
        // In a real application, this would integrate with actual payment gateways like Razorpay, Stripe, etc.
        
        try {
            // Simulate network delay
            Thread.sleep(1000);
            
            // Simulate payment success (90% success rate for demo)
            if (Math.random() > 0.1) {
                payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
            } else {
                payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
        }
        
        return payment;
    }
    
    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
