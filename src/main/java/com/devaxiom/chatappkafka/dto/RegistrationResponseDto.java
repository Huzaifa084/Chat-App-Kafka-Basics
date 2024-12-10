package com.devaxiom.chatappkafka.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationResponseDto {
    private Long id;
    private String email;
    private String name;
    private String role;
}
