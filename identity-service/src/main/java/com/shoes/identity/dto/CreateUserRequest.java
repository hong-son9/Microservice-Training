package com.shoes.identity.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
public class CreateUserRequest {
    private String email;

    private String password;

    private String fullName;

    private String phone;

    private String address;

    private String avatar;

    private Set<Long> roleIds;
}
