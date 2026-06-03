package com.shoes.identity.dto;

import com.shoes.identity.entity.AuthProvider;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String email;

    private String fullName;

    private String phone;

    private String address;

    private String avatar;

    private Boolean status;

//    private Boolean emailVerified;

    private AuthProvider provider;

    private Set<String> roles;
}