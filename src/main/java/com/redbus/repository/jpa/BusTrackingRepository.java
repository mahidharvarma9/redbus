package com.redbus.repository.jpa;

import com.redbus.entity.BusTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusTrackingRepository extends JpaRepository<BusTracking, Long> {
    
    @Query("SELECT bt FROM BusTracking bt WHERE bt.bus.id = :busId ORDER BY bt.timestamp DESC LIMIT 1")
    Optional<BusTracking> findLatestTrackingByBusId(@Param("busId") Long busId);
    
    @Query("SELECT bt FROM BusTracking bt WHERE bt.bus.id = :busId AND bt.timestamp >= :fromTime ORDER BY bt.timestamp DESC")
    List<BusTracking> findTrackingByBusIdAndTimeRange(@Param("busId") Long busId, 
                                                    @Param("fromTime") LocalDateTime fromTime);
    
    @Query("SELECT bt FROM BusTracking bt WHERE bt.bus.id = :busId AND bt.timestamp BETWEEN :startTime AND :endTime ORDER BY bt.timestamp ASC")
    List<BusTracking> findTrackingByBusIdAndTimeWindow(@Param("busId") Long busId,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);
}
