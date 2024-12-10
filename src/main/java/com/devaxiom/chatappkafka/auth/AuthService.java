package com.devaxiom.chatappkafka.auth;


import com.devaxiom.chatappkafka.dto.LoginRequestDto;
import com.devaxiom.chatappkafka.dto.LoginResponseDto;
import com.devaxiom.chatappkafka.dto.RegistrationRequestDto;
import com.devaxiom.chatappkafka.enums.Role;
import com.devaxiom.chatappkafka.exceptions.ConflictException;
import com.devaxiom.chatappkafka.exceptions.*;
import com.devaxiom.chatappkafka.exceptions.UnauthorizedException;
import com.devaxiom.chatappkafka.model.Users;
import com.devaxiom.chatappkafka.repositories.UsersRepository;
import com.devaxiom.chatappkafka.security.JwtService;
import com.devaxiom.chatappkafka.services.UsersServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UsersServiceImpl usersService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    public LoginResponseDto authenticateUser(LoginRequestDto loginRequest) {
        String email = loginRequest.getEmail();
        Optional<Users> userOptional = usersService.findUserOptionalByEmail(email);

        if (userOptional.isEmpty())
            throw new ResourceNotFoundException("User not found with email: " + email);

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                Users user = userOptional.get();
                String jwtToken = jwtService.generateJwtToken(user);
                log.info("User Role: {}", user.getRole().toString());

                return LoginResponseDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .jwtToken(jwtToken)
                        .role(user.getRole())
                        .build();
            }
        } catch (Exception ex) {
            throw new UnauthorizedException("Invalid email or password");
        }
        throw new UnauthorizedException("Login failed");
    }

    public LoginResponseDto registerUser(@Valid RegistrationRequestDto registrationReq) {
        if (registrationReq == null)
            throw new BadRequestException("Registration request cannot be null.");

        if (usersRepository.existsByEmail(registrationReq.getEmail()))
            throw new ConflictException("Email already registered.");

        Role role = this.matchRole(registrationReq.getRole());
        log.info("Matched Role: {}", role);
        Users user = new Users();
        user.setEmail(registrationReq.getEmail());
        user.setPassword(passwordEncoder.encode(registrationReq.getPassword()));
        user.setName(null);
        user.setRole(role);
        user.setProfilePicUrl(null);
        Users savedUser = usersRepository.save(user);
        String jwtToken = jwtService.generateJwtToken(savedUser);
        return LoginResponseDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .jwtToken(jwtToken)
                .role(role)
                .build();
    }

    private Role matchRole(String role) {
        log.info("Requested Role: {}", role);
        // Convert camelCase or other formats to UPPERCASE_WITH_UNDERSCORES
        String formattedRole = role.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
        return Arrays.stream(Role.values())
                .filter(r -> r.name().equals(formattedRole))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Invalid role: " + role));
    }
}

