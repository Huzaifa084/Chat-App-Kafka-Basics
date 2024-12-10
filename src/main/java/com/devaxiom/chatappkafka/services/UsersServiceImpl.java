package com.devaxiom.chatappkafka.services;

import com.devaxiom.chatappkafka.dto.UpdateUserRequestDto;
import com.devaxiom.chatappkafka.dto.UserDto;
import com.devaxiom.chatappkafka.exceptions.*;
import com.devaxiom.chatappkafka.model.Users;
import com.devaxiom.chatappkafka.repositories.UsersRepository;
import com.devaxiom.chatappkafka.security.JwtService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UsersServiceImpl {
    private final UsersRepository usersRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    public Users findUserByEmail(String email) {
        Optional<Users> user = usersRepository.findByEmail(email);
        return user.orElse(null);
    }

    public Optional<Users> findUserOptionalByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Users findUserById(Long id) throws UserException {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isPresent()) return user.get();
        throw new UserException("User not found with id: " + id);
    }

    public Optional<Users> findUserByName(String userName) {
        return usersRepository.findByName(userName);
    }

    public Users findUserProfile(String jwt) throws UserException {
        String email = jwtService.extractUserNameFromJwt(jwt);
        if (email == null) throw new AccessDeniedException("Invalid JWT");
        Users user =  this.findUserByEmail(email);
        if (user == null) throw new UserException("User not found with email: " + email);
        return user;
    }

    public Users updateUser(Long id, UpdateUserRequestDto updateReq) throws UserException {
        Users user = findUserById(id);
        if (updateReq.getName() != null)
            user.setName(updateReq.getName());
        if (updateReq.getProfilePicture() != null)
            user.setProfilePicUrl(updateReq.getProfilePicture());
        return usersRepository.save(user);
    }

    public List<UserDto> searchUser(String query) {
        List<Users> users = usersRepository.searchUser(query);
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }
}
