package com.redbus.dto;

import com.redbus.entity.BusTracking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusTrackingResponse {
    
    private Long id;
    private Long busId;
    private String busNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal speedKmh;
    private BigDecimal directionDegrees;
    private LocalDateTime timestamp;
    
    public static BusTrackingResponse fromBusTracking(BusTracking tracking) {
        return BusTrackingResponse.builder()
                .id(tracking.getId())
                .busId(tracking.getBus().getId())
                .busNumber(tracking.getBus().getBusNumber())
                .latitude(tracking.getLatitude())
                .longitude(tracking.getLongitude())
                .speedKmh(tracking.getSpeedKmh())
                .directionDegrees(tracking.getDirectionDegrees())
                .timestamp(tracking.getTimestamp())
                .build();
    }
}
