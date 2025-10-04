package com.redbus.repository.jpa;

import com.redbus.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    @Query("SELECT s FROM Schedule s " +
           "JOIN s.route r " +
           "WHERE r.origin = :origin AND r.destination = :destination " +
           "AND s.isActive = true AND r.isActive = true")
    List<Schedule> findSchedulesByRoute(@Param("origin") String origin, @Param("destination") String destination);
    
    @Query("SELECT s FROM Schedule s " +
           "JOIN s.route r " +
           "WHERE r.origin = :origin AND r.destination = :destination " +
           "AND s.departureTime >= :departureTime " +
           "AND s.isActive = true AND r.isActive = true " +
           "ORDER BY s.departureTime")
    List<Schedule> findSchedulesByRouteAndTime(@Param("origin") String origin, 
                                             @Param("destination") String destination,
                                             @Param("departureTime") LocalTime departureTime);
    
    @Query("SELECT s FROM Schedule s " +
           "JOIN s.bus b " +
           "WHERE b.busType = :busType " +
           "AND s.isActive = true AND b.isActive = true")
    List<Schedule> findSchedulesByBusType(@Param("busType") String busType);
    
    @Query("SELECT s FROM Schedule s " +
           "JOIN s.route r " +
           "WHERE r.origin = :origin AND r.destination = :destination " +
           "AND s.price BETWEEN :minPrice AND :maxPrice " +
           "AND s.isActive = true AND r.isActive = true " +
           "ORDER BY s.price")
    List<Schedule> findSchedulesByRouteAndPriceRange(@Param("origin") String origin,
                                                    @Param("destination") String destination,
                                                    @Param("minPrice") Double minPrice,
                                                    @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT s FROM Schedule s " +
           "JOIN s.route r " +
           "WHERE r.origin = :origin AND r.destination = :destination " +
           "AND s.isActive = true AND r.isActive = true " +
           "ORDER BY " +
           "CASE WHEN :sortBy = 'price' THEN s.price END ASC, " +
           "CASE WHEN :sortBy = 'departure' THEN s.departureTime END ASC, " +
           "CASE WHEN :sortBy = 'duration' THEN (s.arrivalTime - s.departureTime) END ASC")
    List<Schedule> findSchedulesWithSorting(@Param("origin") String origin,
                                           @Param("destination") String destination,
                                           @Param("sortBy") String sortBy);
}
