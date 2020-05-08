package com.klever.bok.services;

import com.klever.bok.exceptions.ResourceNotFoundException;
import com.klever.bok.models.entity.User;
import com.klever.bok.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "uuid", userId)
        );
    }

    ;
}
