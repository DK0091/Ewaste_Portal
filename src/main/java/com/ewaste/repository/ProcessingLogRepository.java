package com.ewaste.repository;

import com.ewaste.entity.ProcessingLog;
import com.ewaste.entity.Pickup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, Long> {
    List<ProcessingLog> findByPickupOrderByLogDateDesc(Pickup pickup);
}
