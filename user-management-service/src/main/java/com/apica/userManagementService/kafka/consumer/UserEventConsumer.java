package com.apica.userManagementService.kafka.consumer;



import com.apica.userManagementService.kafka.consumer.model.JournalEntry;
import com.apica.userManagementService.kafka.consumer.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserEventConsumer {

    @Autowired
    private JournalRepository journalRepository;

    @KafkaListener(topics = "user-events", groupId = "journal-group")
    public void consumeUserEvent(String eventMessage) {
        String username = extractUsername(eventMessage);
        String eventType = extractEventType(eventMessage);
        String description = eventMessage;

        JournalEntry entry = JournalEntry.builder()
                .username(username)
                .eventType(eventType)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();

        journalRepository.save(entry);
    }

    private String extractUsername(String message) {
        return message.split(" ")[2];
    }

    private String extractEventType(String message) {
        if (message.contains("Registered")) return "REGISTER";
        if (message.contains("Updated")) return "UPDATE";
        if (message.contains("Deleted")) return "DELETE";
        if(message.contains("fetched")) return "FETCH";
        return "UNKNOWN";
    }
}
