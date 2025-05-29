package com.erp.employee.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import com.erp.employee.enums.PaySlipStatus;
import com.erp.employee.models.Employee;
import com.erp.employee.models.PaySlip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPaySlipRepository extends JpaRepository<PaySlip, UUID> {

    @Query("SELECT p FROM PaySlip p WHERE p.employee.id = :employeeId AND p.month = :month AND p.year = :year")
    Optional<PaySlip> findByEmployeeAndMonthAndYear(
            @Param("employeeId") UUID employeeId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("SELECT p FROM PaySlip p WHERE p.month = :month AND p.year = :year")
    Page<PaySlip> findAllByMonthAndYear(
            @Param("month") Integer month,
            @Param("year") Integer year,
            Pageable pageable
    );

    @Query("SELECT p FROM PaySlip p WHERE p.employee.id = :employeeId")
    Page<PaySlip> findAllByEmployee(
            @Param("employeeId") UUID employeeId,
            Pageable pageable
    );

    @Query("SELECT p FROM PaySlip p WHERE p.status = :status AND p.month = :month AND p.year = :year")
    List<PaySlip> findAllByStatusAndMonthAndYear(
            @Param("status") PaySlipStatus status,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    /**
     * Check if a payslip exists for the given employee ID, month and year
     *
     * @param employeeId the employee's ID
     * @param month      the month of the payslip
     * @param year       the year of the payslip
     * @return true if a payslip exists, false otherwise
     */
    boolean existsByEmployee_IdAndMonthAndYear(UUID employeeId, Integer month, Integer year);


    Page<PaySlip> findByMonthAndYear(Integer month, Integer year, Pageable pageable);

    Page<PaySlip> findByEmployee(Employee employee, Pageable pageable);

    @Query("SELECT p FROM PaySlip p WHERE p.employee = :employee ORDER BY p.createdAt DESC")
    Page<PaySlip> findAllByEmployee(@Param("employee") Employee employee, Pageable pageable);

    Optional<PaySlip> findFirstByEmployeeOrderByCreatedAtDesc(Employee employee);

}