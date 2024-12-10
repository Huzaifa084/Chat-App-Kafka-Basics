package com.devaxiom.chatappkafka.dto;

import com.devaxiom.chatappkafka.enums.Role;
import lombok.*;


@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponseDto {
    private Long id;
    private String email;
    private String name;
    private String jwtToken;
    private String refreshToken;
    private boolean isVerified;
    private Role role;
}
