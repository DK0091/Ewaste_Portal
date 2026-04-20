package com.ewaste.service;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.User;
import com.ewaste.repository.ItemJdbcRepository;
import com.ewaste.repository.PickupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PickupService {

    private final PickupRepository pickupRepository;
    private final ItemJdbcRepository itemJdbcRepository;

    public PickupService(PickupRepository pickupRepository, ItemJdbcRepository itemJdbcRepository) {
        this.pickupRepository = pickupRepository;
        this.itemJdbcRepository = itemJdbcRepository;
    }

    @Transactional
    public Pickup schedulePickup(User citizen, String address, LocalDate date, String itemType, Double weight) {
        Pickup pickup = new Pickup();
        pickup.setCitizen(citizen);
        pickup.setAddress(address);
        pickup.setPickupDate(date);
        pickup.setStatus("REQUESTED");

        pickup = pickupRepository.save(pickup);

        List<Map<String, Object>> itemsList = new ArrayList<>();
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("itemType", itemType);
        itemMap.put("weight", weight);
        itemsList.add(itemMap);

        itemJdbcRepository.batchInsertEwasteItems(pickup.getId(), itemsList);
        
        return pickup;
    }

    @Transactional(readOnly = true)
    public List<Pickup> getCitizenPickups(User citizen) {
        return pickupRepository.findByCitizen(citizen);
    }

    @Transactional(readOnly = true)
    public List<Pickup> getRecyclerPickups(User recycler) {
        return pickupRepository.findByRecycler(recycler);
    }

    @Transactional(readOnly = true)
    public List<Pickup> getAllPickups() {
        return pickupRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Pickup> getRequestedPickups() {
        return pickupRepository.findByStatus("REQUESTED");
    }

    @Transactional(readOnly = true)
    public Pickup getPickupById(Long id) {
        return pickupRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateStatus(Long pickupId, String status) {
        Pickup pickup = getPickupById(pickupId);
        if (pickup != null) {
            pickup.setStatus(status);
            pickupRepository.save(pickup);
        }
    }
    
    @Transactional
    public void assignRecycler(Long pickupId, User recycler) {
        Pickup pickup = getPickupById(pickupId);
        if (pickup != null) {
            pickup.setRecycler(recycler);
            pickup.setStatus("ASSIGNED");
            pickupRepository.save(pickup);
        }
    }
}
