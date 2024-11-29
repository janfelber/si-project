package com.rocksolid.service;

import com.rocksolid.dto.UserResponseDto;
import com.rocksolid.module.User;
import com.rocksolid.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getEmail(),
                        user.getFirst_name(),
                        user.getLast_name()
                )).collect(Collectors.toList());
    }

    public Optional<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getFirst_name(),
                        user.getEmail(),
                        user.getLast_name()
                ));
    }

}
