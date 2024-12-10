package com.devaxiom.chatappkafka.dto;

import com.devaxiom.chatappkafka.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String profilePicUrl;
}
