package com.apica.userManagementService.kafka.consumer.service;

import com.apica.userManagementService.kafka.consumer.model.JournalEntry;
import com.apica.userManagementService.kafka.consumer.repository.JournalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalEventService {

    private final JournalRepository repository;

    public JournalEventService(JournalRepository repository) {
        this.repository = repository;
    }

    public List<JournalEntry> getEventsByUsername(String username) {
        return repository.findByUsername(username);
    }
}