package com.redbus.dto;

import com.redbus.entity.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @NotNull(message = "Booking ID is required")
    private Long bookingId;
    
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    
    @NotNull(message = "Payment method is required")
    private Payment.PaymentMethod paymentMethod;
    
    private String transactionId;
    private String paymentGateway;
    
    public Payment toPayment() {
        return Payment.builder()
                .amount(amount)
                .paymentMethod(paymentMethod)
                .transactionId(transactionId)
                .paymentGateway(paymentGateway)
                .build();
    }
}
