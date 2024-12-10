package com.devaxiom.chatappkafka.dto;

import com.devaxiom.chatappkafka.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String content;
    private String sessionId;
    private Long chatId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long senderId;
    private LocalDateTime createdAt;
    private MessageType type;
}