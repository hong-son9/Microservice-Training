package com.shoes.identity.service.Impl;

import com.shoes.identity.dto.CreateUserRequest;
import com.shoes.identity.dto.UserResponse;
import com.shoes.identity.entity.AuthProvider;
import com.shoes.identity.entity.Role;
import com.shoes.identity.entity.User;
import com.shoes.identity.repository.RoleRepository;
import com.shoes.identity.repository.UserRepository;
import com.shoes.identity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        Set<Role> roles = new HashSet<>();
        if (request.getRoleIds() != null) {
            roles = request.getRoleIds()
                    .stream()
                    .map(id -> roleRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Role not found")))
                    .collect(Collectors.toSet());
        }
        else {
            Role defaultRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));
            roles = Collections.singleton(defaultRole);
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .fullName(request.getFullName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .provider(AuthProvider.LOCAL)
                .build();
        return toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getById(Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Tài khoản chưa được xác thực");
        }

        User currentUser = (User) authentication.getPrincipal();
        if (currentUser == null) {
            throw new RuntimeException("No login information found.");
        }

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_ADMIN"));

        if (!isAdmin && !id.equals(currentUser.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("You do not have the right to view other people's information!");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
    private UserResponse toUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .provider(user.getProvider())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
