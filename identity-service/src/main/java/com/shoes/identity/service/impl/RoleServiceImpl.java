package com.shoes.identity.service.impl;

import com.shoes.identity.dto.CreateRoleRequest;
import com.shoes.identity.dto.RoleResponse;
import com.shoes.identity.entity.Role;
import com.shoes.identity.repository.RoleRepository;
import com.shoes.identity.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public RoleResponse create(CreateRoleRequest request) {
        if (roleRepository.existsRoleByName(request.getRoleName())) {
            throw new RuntimeException("Role with name " + request.getRoleName() + " already exists");
        }
        Role role = Role.builder()
                .name(request.getRoleName())
                .description(request.getRoleDescription())
                .build();
        return toRoleResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse getById(Long id) {
        return null;
    }

    @Override
    public List<RoleResponse> getAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
    private RoleResponse toRoleResponse(Role role) {

        return RoleResponse.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .roleDescription(role.getDescription())
                .build();
    }

}
