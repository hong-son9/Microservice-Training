package com.shoes.identity.service;

import com.shoes.identity.dto.CreateRoleRequest;
import com.shoes.identity.dto.CreateUserRequest;
import com.shoes.identity.dto.RoleResponse;
import com.shoes.identity.dto.UserResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(CreateRoleRequest request);

    RoleResponse getById(Long id);

    List<RoleResponse> getAll();

//    UserResponse update(Long id, UpdateUserRequest request);

    void delete(Long id);
}
