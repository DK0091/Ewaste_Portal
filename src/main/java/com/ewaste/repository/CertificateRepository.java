package com.ewaste.repository;

import com.ewaste.entity.Certificate;
import com.ewaste.entity.Pickup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByPickup(Pickup pickup);
}
