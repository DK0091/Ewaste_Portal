package com.ewaste.repository;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickupRepository extends JpaRepository<Pickup, Long> {
    List<Pickup> findByCitizen(User citizen);
    List<Pickup> findByRecycler(User recycler);
    List<Pickup> findByStatus(String status);
    
    @Query("SELECT COALESCE(SUM(i.weight), 0) FROM Pickup p JOIN p.items i WHERE p.status = 'CERTIFIED'")
    Double getTotalEwasteCollectedWeight();
}
