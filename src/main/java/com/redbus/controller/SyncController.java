package com.redbus.controller;

import com.redbus.service.ElasticsearchService;
import com.redbus.service.ScheduleService;
import com.redbus.service.ScheduledSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor
@Slf4j
public class SyncController {
    
    private final ScheduleService scheduleService;
    private final ElasticsearchService elasticsearchService;
    private final ScheduledSyncService scheduledSyncService;
    
    @PostMapping("/elasticsearch")
    public ResponseEntity<Map<String, Object>> syncToElasticsearch() {
        try {
            log.info("Starting manual sync to Elasticsearch...");
            
            // Get all schedules from database
            var schedules = scheduleService.getAllSchedules();
            log.info("Found {} schedules in database", schedules.size());
            
                   int syncedCount = 0;
                   int updatedCount = 0;
                   for (var schedule : schedules) {
                       try {
                           // Validate schedule has required relationships
                           if (schedule.getBus() != null && schedule.getRoute() != null && 
                               schedule.getBus().getOperator() != null) {
                               boolean wasIndexed = elasticsearchService.indexSchedule(schedule);
                               if (wasIndexed) {
                                   updatedCount++;
                                   log.info("Successfully indexed schedule ID: {}", schedule.getId());
                               } else {
                                   log.info("Schedule ID: {} already up-to-date in Elasticsearch", schedule.getId());
                               }
                               syncedCount++;
                           } else {
                               log.warn("Skipping schedule ID: {} - Missing required relationships", schedule.getId());
                           }
                       } catch (Exception e) {
                           log.error("Failed to index schedule ID: {} - Error: {}", schedule.getId(), e.getMessage());
                       }
                   }
                   
                   Map<String, Object> response = new HashMap<>();
                   response.put("success", true);
                   response.put("totalSchedules", schedules.size());
                   response.put("syncedCount", syncedCount);
                   response.put("updatedCount", updatedCount);
                   response.put("alreadySyncedCount", syncedCount - updatedCount);
                   response.put("message", "Sync completed successfully");
            
                   log.info("Manual sync completed. Updated: {}, Already synced: {}, Total: {}", 
                           updatedCount, syncedCount - updatedCount, schedules.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Manual sync failed", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSyncStatus() {
        try {
            // Get count from database
            var schedules = scheduleService.getAllSchedules();
            int dbCount = schedules.size();
            
            // Get count from Elasticsearch
            int esCount = elasticsearchService.getDocumentCount();
            
            Map<String, Object> response = new HashMap<>();
            response.put("databaseCount", dbCount);
            response.put("elasticsearchCount", esCount);
            response.put("inSync", dbCount == esCount);
            response.put("message", "Sync status retrieved successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get sync status", e);
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PostMapping("/trigger")
    public ResponseEntity<Map<String, Object>> triggerScheduledSync() {
        try {
            log.info("Triggering scheduled sync...");
            
            // Trigger the scheduled sync manually
            scheduledSyncService.syncToElasticsearch();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Scheduled sync triggered successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to trigger scheduled sync", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
