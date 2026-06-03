package com.shoes.identity.service;

import com.shoes.identity.dto.LoginRequest;
import com.shoes.identity.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
