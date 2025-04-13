package com.apica.userManagementService.kafka;

import com.apica.userManagementService.model.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String TOPIC = "user-events";  // Kafka topic name

    public void sendUserEvent(User user,String eventType) {
        String eventMessage = "User Event: " + user.getUsername() + " Event " +eventType+ " Triggered";
        kafkaTemplate.send(TOPIC, eventMessage);
    }
}
