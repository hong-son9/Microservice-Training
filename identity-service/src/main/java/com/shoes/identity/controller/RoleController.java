package com.shoes.identity.controller;

import com.shoes.identity.dto.CreateRoleRequest;
import com.shoes.identity.dto.CreateUserRequest;
import com.shoes.identity.dto.RoleResponse;
import com.shoes.identity.dto.UserResponse;
import com.shoes.identity.service.RoleService;
import com.shoes.identity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @PostMapping("/create")
    public RoleResponse create(@RequestBody CreateRoleRequest createRoleRequest) {
        return roleService.create(createRoleRequest);
    }
}
