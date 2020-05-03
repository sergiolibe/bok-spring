package com.klever.bok.services;

import com.klever.bok.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserService {
    UUID obtainIdFromUserDetails(UserDetails userDetails);
    User userFromUserDetails(UserDetails userDetails);
}
