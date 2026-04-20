package com.ewaste.service;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Async
    public void sendPickupAssignedNotification(User citizen, Pickup pickup) {
        logger.info("[ASYNC NOTIFICATION] Starting async notification task...");
        try {
            Thread.sleep(2000);
            logger.info("[ASYNC NOTIFICATION] sent update for Pickup ID: " + pickup.getId() + " to Citizen: " + citizen.getEmail());
        } catch (InterruptedException e) {
            logger.error("Async notification failed", e);
            Thread.currentThread().interrupt();
        }
    }
}
