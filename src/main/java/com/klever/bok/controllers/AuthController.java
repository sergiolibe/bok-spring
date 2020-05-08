package com.klever.bok.controllers;

import com.klever.bok.exceptions.BadRequestException;
import com.klever.bok.models.entity.ERole;
import com.klever.bok.models.entity.Role;
import com.klever.bok.models.entity.User;
import com.klever.bok.payload.request.LoginRequest;
import com.klever.bok.payload.request.SignupRequest;
import com.klever.bok.payload.response.JwtResponse;
import com.klever.bok.payload.response.MessageResponse;
import com.klever.bok.repositories.RoleRepository;
import com.klever.bok.repositories.UserRepository;
import com.klever.bok.security.jwt.JwtUtils;
import com.klever.bok.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userPrincipal.getName(),
                userPrincipal.getLastname(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody final SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername()))
            throw new BadRequestException("Error: Username is already taken!");

        if (userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new BadRequestException("Error: Email is already in use!");

        // Create new user's account
        userRepository.save(buildUser(signUpRequest));
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private User buildUser(SignupRequest signUpRequest) {
        User user = instantiateUser(signUpRequest);
        user.setRoles(getRoles(signUpRequest));
        return user;
    }

    private User instantiateUser(SignupRequest signUpRequest) {
        return new User(
                signUpRequest.getName(),
                signUpRequest.getLastname(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));
    }

    private Set<Role> getRoles(SignupRequest signUpRequest) {
        Set<String> signUpRequestRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (signUpRequestRoles == null)
            roles.add(getRole(ERole.defaultRole()));
        else
            for (String role : signUpRequestRoles)
                roles.add(getRole(ERole.byName(role)));

        return roles;
    }

    private Role getRole(ERole roleUser) {
        return roleRepository.findByName(roleUser)
                .orElseThrow(() -> new RuntimeException("Error: Role < " + roleUser.toString() + " > is not found."));
    }
}
