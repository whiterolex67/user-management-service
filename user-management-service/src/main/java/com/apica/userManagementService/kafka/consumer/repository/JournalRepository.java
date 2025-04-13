package com.apica.userManagementService.kafka.consumer.repository;



import com.apica.userManagementService.kafka.consumer.model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUsername(String username);
}
