package com.redbus.controller;

import com.redbus.dto.*;
import com.redbus.entity.*;
import com.redbus.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operator")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
public class BusManagementController {
    
    private final BusOperatorService busOperatorService;
    private final BusService busService;
    private final RouteService routeService;
    private final ScheduleService scheduleService;
    
    // Bus Operator Management
    @GetMapping("/operators")
    public ResponseEntity<List<BusOperator>> getAllOperators() {
        return ResponseEntity.ok(busOperatorService.getAllOperators());
    }
    
    @GetMapping("/operators/{id}")
    public ResponseEntity<BusOperator> getOperatorById(@PathVariable Long id) {
        return busOperatorService.getOperatorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/operators")
    public ResponseEntity<BusOperator> createOperator(@Valid @RequestBody BusOperatorRequest request) {
        BusOperator operator = busOperatorService.createOperator(request.toBusOperator());
        return ResponseEntity.status(HttpStatus.CREATED).body(operator);
    }
    
    @PutMapping("/operators/{id}")
    public ResponseEntity<BusOperator> updateOperator(@PathVariable Long id, @Valid @RequestBody BusOperatorRequest request) {
        BusOperator operator = busOperatorService.updateOperator(id, request.toBusOperator());
        return ResponseEntity.ok(operator);
    }
    
    @DeleteMapping("/operators/{id}")
    public ResponseEntity<Void> deleteOperator(@PathVariable Long id) {
        busOperatorService.deleteOperator(id);
        return ResponseEntity.noContent().build();
    }
    
    // Bus Management
    @GetMapping("/buses")
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }
    
    @GetMapping("/buses/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long id) {
        return busService.getBusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/operators/{operatorId}/buses")
    public ResponseEntity<List<Bus>> getBusesByOperator(@PathVariable Long operatorId) {
        return ResponseEntity.ok(busService.getBusesByOperator(operatorId));
    }
    
    @PostMapping("/buses")
    public ResponseEntity<Bus> createBus(@Valid @RequestBody BusRequest request) {
        Bus bus = request.toBus();
        BusOperator operator = busOperatorService.getOperatorById(request.getOperatorId())
                .orElseThrow(() -> new IllegalArgumentException("Bus operator not found"));
        bus.setOperator(operator);
        
        Bus createdBus = busService.createBus(bus);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBus);
    }
    
    @PutMapping("/buses/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @Valid @RequestBody BusRequest request) {
        Bus bus = request.toBus();
        BusOperator operator = busOperatorService.getOperatorById(request.getOperatorId())
                .orElseThrow(() -> new IllegalArgumentException("Bus operator not found"));
        bus.setOperator(operator);
        
        Bus updatedBus = busService.updateBus(id, bus);
        return ResponseEntity.ok(updatedBus);
    }
    
    @DeleteMapping("/buses/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }
    
    // Route Management
    @GetMapping("/routes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }
    
    @GetMapping("/routes/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        return routeService.getRouteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/routes")
    public ResponseEntity<Route> createRoute(@Valid @RequestBody RouteRequest request) {
        Route route = routeService.createRoute(request.toRoute());
        return ResponseEntity.status(HttpStatus.CREATED).body(route);
    }
    
    @PutMapping("/routes/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteRequest request) {
        Route route = routeService.updateRoute(id, request.toRoute());
        return ResponseEntity.ok(route);
    }
    
    @DeleteMapping("/routes/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
    
    // Schedule Management
    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }
    
    @GetMapping("/schedules/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/schedules")
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody ScheduleRequest request) {
        Schedule schedule = request.toSchedule();
        Bus bus = busService.getBusById(request.getBusId())
                .orElseThrow(() -> new IllegalArgumentException("Bus not found"));
        Route route = routeService.getRouteById(request.getRouteId())
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));
        
        schedule.setBus(bus);
        schedule.setRoute(route);
        
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
    }
    
    @PutMapping("/schedules/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequest request) {
        Schedule schedule = request.toSchedule();
        Bus bus = busService.getBusById(request.getBusId())
                .orElseThrow(() -> new IllegalArgumentException("Bus not found"));
        Route route = routeService.getRouteById(request.getRouteId())
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));
        
        schedule.setBus(bus);
        schedule.setRoute(route);
        
        Schedule updatedSchedule = scheduleService.updateSchedule(id, schedule);
        return ResponseEntity.ok(updatedSchedule);
    }
    
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
