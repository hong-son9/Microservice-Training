package com.shoes.identity.repository;

import com.shoes.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(String name);
    public Optional<Role> findById(long id);
    public boolean existsRoleByName(String name);
}
