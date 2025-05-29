package com.erp.employee.repositories;

import com.erp.employee.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByActivationCode(String verificationCode);

    @Query("SELECT u FROM Employee u" +
            " WHERE (lower(u.firstName)  LIKE ('%' || lower(:searchKey) || '%')) " +
            " OR (lower(u.lastName) LIKE ('%' || lower(:searchKey) || '%')) " +
            " OR (lower(u.email) LIKE ('%' || lower(:searchKey) || '%'))")
    Page<Employee> search(Pageable pageable, String searchKey);

}
