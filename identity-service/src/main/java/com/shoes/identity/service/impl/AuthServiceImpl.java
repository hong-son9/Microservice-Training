package com.shoes.identity.service.impl;

import com.shoes.identity.dto.LoginRequest;
import com.shoes.identity.dto.LoginResponse;
import com.shoes.identity.entity.User;
import com.shoes.identity.repository.UserRepository;
import com.shoes.identity.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getStatus()) {
            throw new RuntimeException("User is not active");
        }
        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!matches) {
            throw new RuntimeException("Incorrect password");
        }
        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }
}
