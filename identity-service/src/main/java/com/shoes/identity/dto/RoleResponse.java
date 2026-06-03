package com.shoes.identity.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
public class RoleResponse {
    private Long roleId;
    private String roleName;
    private String roleDescription;
}
