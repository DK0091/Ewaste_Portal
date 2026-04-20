package com.ewaste.repository;

import com.ewaste.entity.EwasteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<EwasteItem, Long> {
}
