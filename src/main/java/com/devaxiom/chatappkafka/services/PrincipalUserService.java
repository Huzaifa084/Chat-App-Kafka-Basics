package com.devaxiom.chatappkafka.services;

import com.devaxiom.chatappkafka.enums.Role;
import com.devaxiom.chatappkafka.exceptions.ResourceNotFoundException;
import com.devaxiom.chatappkafka.model.Users;
import com.devaxiom.chatappkafka.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalUserService {
    private final UsersRepository userEntityRepository;

    public String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public Users getLoggedInUser() {
        String email = this.getLoggedInUserEmail();
        return userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public Long getLoggedInUserId() {
        return this.getLoggedInUser().getId();
    }

    public String getLoggedInUserName() {
        return this.getLoggedInUser().getName();
    }

    public boolean isAdmin() {
        return this.getLoggedInUser().getRole() == Role.ADMIN;
    }
}
