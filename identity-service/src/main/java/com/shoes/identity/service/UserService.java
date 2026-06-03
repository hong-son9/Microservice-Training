package com.shoes.identity.service;

import com.shoes.identity.dto.CreateUserRequest;
import com.shoes.identity.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(CreateUserRequest request);

    UserResponse getById(Long id);

    List<UserResponse> getAll();

//    UserResponse update(Long id, UpdateUserRequest request);

    void delete(Long id);
}
