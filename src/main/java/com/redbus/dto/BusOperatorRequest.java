package com.redbus.dto;

import com.redbus.entity.BusOperator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusOperatorRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Email should be valid")
    private String contactEmail;
    
    private String contactPhone;
    
    @NotBlank(message = "License number is required")
    private String licenseNumber;
    
    public BusOperator toBusOperator() {
        return BusOperator.builder()
                .name(name)
                .contactEmail(contactEmail)
                .contactPhone(contactPhone)
                .licenseNumber(licenseNumber)
                .build();
    }
}
