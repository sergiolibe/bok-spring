package com.klever.bok.services;

import com.klever.bok.models.User;
import com.klever.bok.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    public UUID obtainIdFromUserDetails(UserDetails userDetails) {
        return userFromUserDetails(userDetails).getId();
    }

    public User userFromUserDetails(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

}
