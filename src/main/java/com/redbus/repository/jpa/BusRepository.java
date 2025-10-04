package com.redbus.repository.jpa;

import com.redbus.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    
    Optional<Bus> findByBusNumber(String busNumber);
    
    boolean existsByBusNumber(String busNumber);
    
    List<Bus> findByOperatorIdAndIsActiveTrue(Long operatorId);
    
    @Query("SELECT b FROM Bus b WHERE b.operator.id = :operatorId AND b.isActive = true")
    List<Bus> findActiveBusesByOperator(@Param("operatorId") Long operatorId);
    
    @Query("SELECT b FROM Bus b WHERE b.busType = :busType AND b.isActive = true")
    List<Bus> findActiveBusesByType(@Param("busType") Bus.BusType busType);
}
