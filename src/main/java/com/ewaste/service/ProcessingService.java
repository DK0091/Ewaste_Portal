package com.ewaste.service;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.ProcessingLog;
import com.ewaste.entity.User;
import com.ewaste.repository.ProcessingLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProcessingService {

    private final ProcessingLogRepository logRepository;
    private final PickupService pickupService;

    public ProcessingService(ProcessingLogRepository logRepository, PickupService pickupService) {
        this.logRepository = logRepository;
        this.pickupService = pickupService;
    }

    @Transactional
    public void updateProcessingStage(Long pickupId, User recycler, String stage, String notes) {
        Pickup pickup = pickupService.getPickupById(pickupId);
        if (pickup != null && pickup.getRecycler().getId().equals(recycler.getId())) {
            
            ProcessingLog log = new ProcessingLog(pickup, recycler, stage, LocalDateTime.now(), notes);
            logRepository.save(log);

            pickupService.updateStatus(pickupId, stage);
        }
    }

    @Transactional(readOnly = true)
    public List<ProcessingLog> getLogsForPickup(Pickup pickup) {
        return logRepository.findByPickupOrderByLogDateDesc(pickup);
    }
}
