package com.redbus.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Document(indexName = "buses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusSearchDocument {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Long)
    private Long scheduleId;
    
    @Field(type = FieldType.Long)
    private Long busId;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String busNumber;
    
    @Field(type = FieldType.Keyword)
    private String busType;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String operatorName;
    
    @Field(type = FieldType.Long)
    private Long operatorId;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String origin;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String destination;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String route; // "Mumbai to Pune"
    
    @Field(type = FieldType.Text)
    private String departureTime;
    
    @Field(type = FieldType.Text)
    private String arrivalTime;
    
    @Field(type = FieldType.Double)
    private BigDecimal price;
    
    @Field(type = FieldType.Integer)
    private Integer totalSeats;
    
    @Field(type = FieldType.Integer)
    private Integer availableSeats;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private List<String> amenities;
    
    @Field(type = FieldType.Double)
    private BigDecimal distanceKm;
    
    @Field(type = FieldType.Double)
    private BigDecimal estimatedDurationHours;
    
    @Field(type = FieldType.Integer)
    private Integer durationMinutes;
    
    @Field(type = FieldType.Boolean)
    private Boolean isActive;
    
    @Field(type = FieldType.Boolean)
    private Boolean isRecurring;
    
    @Field(type = FieldType.Integer)
    private List<Integer> daysOfWeek;
    
    @Field(type = FieldType.Date)
    private String createdAt;
    
    @Field(type = FieldType.Date)
    private String updatedAt;
}
