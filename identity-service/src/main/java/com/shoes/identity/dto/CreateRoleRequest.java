package com.shoes.identity.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class CreateRoleRequest {
    private String roleName;
    private String roleDescription;
}
