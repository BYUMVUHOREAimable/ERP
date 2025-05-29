// CreatePaySlipDTO.java
package com.erp.employee.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.UUID;

@Data
public class CreatePaySlipDTO {
    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotNull(message = "Month is required")
    @Min(1) @Max(12)
    private Integer month;

    @NotNull(message = "Year is required")
    @Min(2024)
    private Integer year;
}