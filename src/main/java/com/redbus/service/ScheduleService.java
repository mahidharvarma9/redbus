package com.redbus.service;

import com.redbus.entity.Schedule;
import com.redbus.entity.Bus;
import com.redbus.entity.Route;
import com.redbus.repository.jpa.ScheduleRepository;
import com.redbus.repository.jpa.BusRepository;
import com.redbus.repository.jpa.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    
    private final ScheduleRepository scheduleRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final ElasticsearchService elasticsearchService;
    
    @Transactional(readOnly = true)
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByRoute(String origin, String destination) {
        return scheduleRepository.findSchedulesByRoute(origin, destination);
    }
    
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByRouteAndTime(String origin, String destination, LocalTime departureTime) {
        return scheduleRepository.findSchedulesByRouteAndTime(origin, destination, departureTime);
    }
    
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByBusType(String busType) {
        return scheduleRepository.findSchedulesByBusType(busType);
    }
    
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByRouteAndPriceRange(String origin, String destination, Double minPrice, Double maxPrice) {
        return scheduleRepository.findSchedulesByRouteAndPriceRange(origin, destination, minPrice, maxPrice);
    }
    
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesWithSorting(String origin, String destination, String sortBy) {
        return scheduleRepository.findSchedulesWithSorting(origin, destination, sortBy);
    }
    
    @Transactional
    public Schedule createSchedule(Schedule schedule) {
        Bus bus = busRepository.findById(schedule.getBus().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with id: " + schedule.getBus().getId()));
        
        Route route = routeRepository.findById(schedule.getRoute().getId())
                .orElseThrow(() -> new IllegalArgumentException("Route not found with id: " + schedule.getRoute().getId()));
        
        schedule.setBus(bus);
        schedule.setRoute(route);
        
        Schedule savedSchedule = scheduleRepository.save(schedule);
        
        // Index in Elasticsearch
        elasticsearchService.indexSchedule(savedSchedule);
        
        return savedSchedule;
    }
    
    @Transactional
    public Schedule updateSchedule(Long id, Schedule schedule) {
        Schedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found with id: " + id));
        
        existingSchedule.setDepartureTime(schedule.getDepartureTime());
        existingSchedule.setArrivalTime(schedule.getArrivalTime());
        existingSchedule.setPrice(schedule.getPrice());
        existingSchedule.setIsRecurring(schedule.getIsRecurring());
        existingSchedule.setDaysOfWeek(schedule.getDaysOfWeek());
        
        Schedule updatedSchedule = scheduleRepository.save(existingSchedule);
        
        // Update in Elasticsearch
        elasticsearchService.indexSchedule(updatedSchedule);
        
        return updatedSchedule;
    }
    
    @Transactional
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found with id: " + id));
        
        schedule.setIsActive(false);
        scheduleRepository.save(schedule);
        
        // Remove from Elasticsearch
        elasticsearchService.deleteSchedule(id);
    }
}
