package com.redbus.service;

import com.redbus.entity.Schedule;
import com.redbus.repository.jpa.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledSyncService {
    
    private final ScheduleRepository scheduleRepository;
    private final ElasticsearchService elasticsearchService;
    
    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    @Transactional(readOnly = true)
    public void syncToElasticsearch() {
        try {
            log.debug("Starting scheduled sync to Elasticsearch...");
            
            List<Schedule> schedules = scheduleRepository.findAll();
            log.debug("Found {} schedules to sync", schedules.size());
            
            int syncedCount = 0;
            int skippedCount = 0;
            int updatedCount = 0;
            
            for (Schedule schedule : schedules) {
                try {
                    // Validate schedule has required relationships
                    if (schedule.getBus() != null && schedule.getRoute() != null && 
                        schedule.getBus().getOperator() != null) {
                        boolean wasIndexed = elasticsearchService.indexSchedule(schedule);
                        if (wasIndexed) {
                            syncedCount++;
                            updatedCount++;
                            log.debug("Successfully synced schedule ID: {}", schedule.getId());
                        } else {
                            syncedCount++;
                            log.debug("Schedule ID: {} already up-to-date in Elasticsearch", schedule.getId());
                        }
                    } else {
                        skippedCount++;
                        log.debug("Skipping schedule ID: {} - Missing required relationships", schedule.getId());
                    }
                } catch (Exception e) {
                    log.error("Failed to sync schedule ID: {} - Error: {}", schedule.getId(), e.getMessage());
                }
            }
            
            if (updatedCount > 0) {
                log.info("Scheduled sync completed. Updated: {}, Already synced: {}, Skipped: {}, Total: {}", 
                        updatedCount, syncedCount - updatedCount, skippedCount, schedules.size());
            } else {
                log.debug("Scheduled sync completed. No updates needed. Total: {}", schedules.size());
            }
            
        } catch (Exception e) {
            log.error("Scheduled sync failed: {}", e.getMessage());
        }
    }
    
    // Manual sync method for immediate sync
    public int manualSync() {
        try {
            log.info("Starting manual sync to Elasticsearch...");
            
            List<Schedule> schedules = scheduleRepository.findAll();
            log.info("Found {} schedules to sync", schedules.size());
            
            int syncedCount = 0;
            int updatedCount = 0;
            for (Schedule schedule : schedules) {
                try {
                    // Validate schedule has required relationships
                    if (schedule.getBus() != null && schedule.getRoute() != null && 
                        schedule.getBus().getOperator() != null) {
                        boolean wasIndexed = elasticsearchService.indexSchedule(schedule);
                        if (wasIndexed) {
                            updatedCount++;
                            log.info("Successfully synced schedule ID: {}", schedule.getId());
                        } else {
                            log.info("Schedule ID: {} already up-to-date in Elasticsearch", schedule.getId());
                        }
                        syncedCount++;
                    } else {
                        log.warn("Skipping schedule ID: {} - Missing required relationships", schedule.getId());
                    }
                } catch (Exception e) {
                    log.error("Failed to sync schedule ID: {} - Error: {}", schedule.getId(), e.getMessage());
                }
            }
            
            log.info("Manual sync completed. Updated: {}, Already synced: {}, Total: {}", 
                    updatedCount, syncedCount - updatedCount, schedules.size());
            return updatedCount;
            
        } catch (Exception e) {
            log.error("Manual sync failed: {}", e.getMessage());
            return 0;
        }
    }
}
