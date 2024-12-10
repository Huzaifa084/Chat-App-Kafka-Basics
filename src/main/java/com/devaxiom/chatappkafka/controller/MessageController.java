package com.devaxiom.chatappkafka.controller;

import com.devaxiom.chatappkafka.broker.Sender;
import com.devaxiom.chatappkafka.exceptions.UnauthorizedException;
import com.devaxiom.chatappkafka.model.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@AllArgsConstructor
@Slf4j
public class MessageController {

    private final Sender sender;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.send-message")
    public void sendMessage(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        if (username == null)
            throw new UnauthorizedException("User is not authenticated");
        chatMessage.setSender(username);

        chatMessage.setSessionId(headerAccessor.getSessionId());
        sender.send("messaging", chatMessage);
        log.info("Sending message to /topic/public: {}", chatMessage);
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
        log.info("Message sent to /topic/public: {}", chatMessage);
    }

    @MessageMapping("/chat.add-user")
    @SendTo("/topic/public")
    public Message addUser(
            @Payload Message chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        }

        return chatMessage;
    }
}
