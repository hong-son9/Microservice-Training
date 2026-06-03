package com.shoes.identity.controller;

import com.shoes.identity.dto.CreateUserRequest;
import com.shoes.identity.dto.UserResponse;
import com.shoes.identity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public UserResponse create(@RequestBody CreateUserRequest createUserRequest) {
        return userService.create(createUserRequest);
    }

}
