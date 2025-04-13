package com.apica.userManagementService.kafka.consumer.controller;

import com.apica.userManagementService.kafka.consumer.model.JournalEntry;
import com.apica.userManagementService.kafka.consumer.service.JournalEventService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apica/journal")
public class JournalEventController {

    private final JournalEventService journalEventService;

    public JournalEventController(JournalEventService journalEventService) {
        this.journalEventService = journalEventService;
    }

    @GetMapping("/events/{username}")
    public List<JournalEntry> getEventsByUsername(@PathVariable String username, Authentication authentication) {
        String jwtUsername = authentication.getName();

        if (!jwtUsername.equals(username)) {
            throw new SecurityException("Unauthorized access to journal data.");
        }

        return journalEventService.getEventsByUsername(username);
    }
}
