package com.erp.employee.dtos.request;

import com.erp.employee.enums.EmploymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateEmploymentDTO {

    @NotBlank(message = "Code must not be blank")
    private String code;

    @NotBlank(message = "Department must not be blank")
    private String department;

    @NotBlank(message = "Position must not be blank")
    private String position;

    @NotNull(message = "Base salary is required")
    @Positive(message = "Base salary must be a positive value")
    private BigDecimal baseSalary;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date must be in the past or present")
    private LocalDate joiningDate;

    @NotNull(message = "Employment status is required")
    private EmploymentStatus status;
}
