package com.redbus.repository.jpa;

import com.redbus.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    @Query("SELECT r FROM Route r WHERE r.origin = :origin AND r.destination = :destination")
    Optional<Route> findByOriginAndDestination(@Param("origin") String origin, @Param("destination") String destination);
    
    List<Route> findByIsActiveTrue();
    
    @Query("SELECT r FROM Route r WHERE r.origin LIKE %:origin% OR r.destination LIKE %:destination%")
    List<Route> findByOriginOrDestinationContaining(@Param("origin") String origin, @Param("destination") String destination);
}
