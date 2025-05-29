package com.erp.employee.repositories;

import com.erp.employee.enums.EmploymentStatus;
import com.erp.employee.models.Employment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IEmploymentRepository extends JpaRepository<Employment, Long> {

    Optional<Employment> findByEmployeeIdAndStatus(UUID id, EmploymentStatus employmentStatus);
}
