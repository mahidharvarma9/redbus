package com.redbus.service;

import com.redbus.entity.Bus;
import com.redbus.entity.BusOperator;
import com.redbus.repository.jpa.BusRepository;
import com.redbus.repository.jpa.BusOperatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusService {
    
    private final BusRepository busRepository;
    private final BusOperatorRepository busOperatorRepository;
    
    @Transactional(readOnly = true)
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Bus> getBusById(Long id) {
        return busRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Bus> getBusesByOperator(Long operatorId) {
        return busRepository.findActiveBusesByOperator(operatorId);
    }
    
    @Transactional
    public Bus createBus(Bus bus) {
        if (busRepository.existsByBusNumber(bus.getBusNumber())) {
            throw new IllegalArgumentException("Bus number already exists: " + bus.getBusNumber());
        }
        
        BusOperator operator = busOperatorRepository.findById(bus.getOperator().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bus operator not found with id: " + bus.getOperator().getId()));
        
        bus.setOperator(operator);
        return busRepository.save(bus);
    }
    
    @Transactional
    public Bus updateBus(Long id, Bus bus) {
        Bus existingBus = busRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with id: " + id));
        
        if (!existingBus.getBusNumber().equals(bus.getBusNumber()) && 
            busRepository.existsByBusNumber(bus.getBusNumber())) {
            throw new IllegalArgumentException("Bus number already exists: " + bus.getBusNumber());
        }
        
        existingBus.setBusNumber(bus.getBusNumber());
        existingBus.setBusType(bus.getBusType());
        existingBus.setTotalSeats(bus.getTotalSeats());
        existingBus.setAmenities(bus.getAmenities());
        
        return busRepository.save(existingBus);
    }
    
    @Transactional
    public void deleteBus(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with id: " + id));
        
        bus.setIsActive(false);
        busRepository.save(bus);
    }
}
