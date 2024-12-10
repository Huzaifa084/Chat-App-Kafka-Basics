package com.devaxiom.chatappkafka.auth;

import com.devaxiom.chatappkafka.advices.ApiResponse;
import com.devaxiom.chatappkafka.dto.LoginRequestDto;
import com.devaxiom.chatappkafka.dto.LoginResponseDto;
import com.devaxiom.chatappkafka.dto.RegistrationRequestDto;
import com.devaxiom.chatappkafka.exceptions.UnauthorizedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequest) {
        log.info("Attempting login for email: {}", loginRequest.getEmail());
        try {
            LoginResponseDto loginResponseDto = authService.authenticateUser(loginRequest);
            ApiResponse<LoginResponseDto> response = new ApiResponse<>(loginResponseDto, "Login successful");
            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            log.error("Login failed for email: {}", loginRequest.getEmail());
            ApiResponse<LoginResponseDto> response = new ApiResponse<>(null, "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponseDto>> register(@RequestBody @Valid RegistrationRequestDto registrationReq) {
        log.info("Attempting register for email: {}", registrationReq.getEmail());
        try {
            LoginResponseDto loginResponseDto = authService.registerUser(registrationReq);
            ApiResponse<LoginResponseDto> response = new ApiResponse<>(loginResponseDto, "Registration successful");
            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            log.error("Registration failed for email: {}", registrationReq.getEmail());
            ApiResponse<LoginResponseDto> response = new ApiResponse<>(null, "Registration failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
