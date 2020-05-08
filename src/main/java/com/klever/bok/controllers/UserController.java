package com.klever.bok.controllers;

import com.klever.bok.models.entity.User;
import com.klever.bok.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

}
