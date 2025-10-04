package com.redbus.service;

import com.redbus.entity.BusOperator;
import com.redbus.repository.jpa.BusOperatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusOperatorService {
    
    private final BusOperatorRepository busOperatorRepository;
    
    @Transactional(readOnly = true)
    public List<BusOperator> getAllOperators() {
        return busOperatorRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public Optional<BusOperator> getOperatorById(Long id) {
        return busOperatorRepository.findById(id);
    }
    
    @Transactional
    public BusOperator createOperator(BusOperator operator) {
        if (busOperatorRepository.existsByLicenseNumber(operator.getLicenseNumber())) {
            throw new IllegalArgumentException("License number already exists: " + operator.getLicenseNumber());
        }
        return busOperatorRepository.save(operator);
    }
    
    @Transactional
    public BusOperator updateOperator(Long id, BusOperator operator) {
        BusOperator existingOperator = busOperatorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bus operator not found with id: " + id));
        
        existingOperator.setName(operator.getName());
        existingOperator.setContactEmail(operator.getContactEmail());
        existingOperator.setContactPhone(operator.getContactPhone());
        
        return busOperatorRepository.save(existingOperator);
    }
    
    @Transactional
    public void deleteOperator(Long id) {
        BusOperator operator = busOperatorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bus operator not found with id: " + id));
        
        operator.setIsActive(false);
        busOperatorRepository.save(operator);
    }
}
