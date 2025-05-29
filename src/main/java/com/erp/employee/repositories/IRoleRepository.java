package com.erp.employee.repositories;

import com.erp.employee.enums.ERole;
import com.erp.employee.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(ERole role);

    boolean existsByName(ERole role);
}
