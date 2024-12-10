package com.devaxiom.chatappkafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ChatDetailsAndMessagesDto {
    private Long chatId;
    private String chatName;
    private String chatImage;
    private boolean isGroupChat;
    private List<Long> memberIds;
    private List<MessageDto> messages;
    private List<Long> adminIds;
    private Long createdBy;
}