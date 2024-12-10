package com.devaxiom.chatappkafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatsDto {
    private Long chatId;
    private String chatName;
    private String chatImage;
    private boolean isGroupChat;
    private String lastMessage;
    private Long createdBy;
}