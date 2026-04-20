package com.ewaste.service;

import com.ewaste.repository.PickupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportingScheduledService {

    private static final Logger logger = LoggerFactory.getLogger(ReportingScheduledService.class);
    private final PickupRepository pickupRepository;

    public ReportingScheduledService(PickupRepository pickupRepository) {
        this.pickupRepository = pickupRepository;
    }

    @Transactional(readOnly = true)
    @Scheduled(fixedRate = 600000) 
    public void generateWeeklyEwasteReport() {
        logger.info("=== [SCHEDULED TASK] Generating Weekly E-Waste Report ===");
        Double totalWeight = pickupRepository.getTotalEwasteCollectedWeight();
        logger.info(String.format("Total E-Waste Certified to date: %.2f kg", (totalWeight != null ? totalWeight : 0.0)));
        logger.info("=========================================================");
    }
}
