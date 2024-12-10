package com.devaxiom.chatappkafka.broker;

import com.devaxiom.chatappkafka.model.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class Receiver {

    private final SimpMessageSendingOperations messagingTemplate;
    private final SimpUserRegistry userRegistry;

    @KafkaListener(topics = "messaging", groupId = "chat")
    public void consume(Message chatMessage) {
        log.info("Received message from Kafka: {}", chatMessage);
        for (SimpUser user : userRegistry.getUsers()) {
            for (SimpSession session : user.getSessions()) {
                if (!session.getId().equals(chatMessage.getSessionId())) {
                    messagingTemplate.convertAndSendToUser(session.getId(), "/topic/public", chatMessage);
                }
            }
        }
    }
}