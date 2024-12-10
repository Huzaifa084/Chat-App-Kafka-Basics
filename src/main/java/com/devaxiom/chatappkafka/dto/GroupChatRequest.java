package com.devaxiom.chatappkafka.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatRequest {
    @NotNull
    @NotEmpty
    private List<Long> userIds;

    @NotNull
    @NotEmpty
    private String chatName;
    private String chatImage;
}
