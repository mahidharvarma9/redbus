package com.redbus.repository.jpa;

import com.redbus.entity.BusOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusOperatorRepository extends JpaRepository<BusOperator, Long> {
    
    Optional<BusOperator> findByLicenseNumber(String licenseNumber);
    
    boolean existsByLicenseNumber(String licenseNumber);
    
    List<BusOperator> findByIsActiveTrue();
}
