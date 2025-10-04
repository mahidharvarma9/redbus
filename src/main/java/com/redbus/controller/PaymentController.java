package com.redbus.controller;

import com.redbus.dto.PaymentRequest;
import com.redbus.dto.PaymentResponse;
import com.redbus.entity.Payment;
import com.redbus.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request,
                                                        Authentication authentication) {
        // Validate that the booking belongs to the authenticated user
        Long userId = ((com.redbus.entity.User) authentication.getPrincipal()).getId();
        // Additional validation can be added here to ensure the booking belongs to the user
        
        PaymentResponse payment = paymentService.processPayment(request);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByBooking(@PathVariable Long bookingId,
                                                                     Authentication authentication) {
        Long userId = ((com.redbus.entity.User) authentication.getPrincipal()).getId();
        // Additional validation can be added here to ensure the booking belongs to the user
        
        List<Payment> payments = paymentService.getPaymentsByBookingId(bookingId);
        List<PaymentResponse> responses = payments.stream()
                .map(PaymentResponse::fromPayment)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id,
                                                        Authentication authentication) {
        Long userId = ((com.redbus.entity.User) authentication.getPrincipal()).getId();
        // Additional validation can be added here to ensure the payment belongs to the user
        
        return paymentService.getPaymentById(id)
                .map(payment -> ResponseEntity.ok(PaymentResponse.fromPayment(payment)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(@PathVariable Long id,
                                                             @RequestParam Payment.PaymentStatus status) {
        PaymentResponse payment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(payment);
    }
    
    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long id,
                                                       Authentication authentication) {
        Long userId = ((com.redbus.entity.User) authentication.getPrincipal()).getId();
        // Additional validation can be added here to ensure the payment belongs to the user
        
        PaymentResponse payment = paymentService.refundPayment(id);
        return ResponseEntity.ok(payment);
    }
}
