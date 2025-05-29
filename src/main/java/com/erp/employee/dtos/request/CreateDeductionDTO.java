package com.erp.employee.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateDeductionDTO {
    @NotBlank(message = "Code is required")
    @Pattern(regexp = "^D[A-Z]{2}\\d{3}$", message = "Code must be in format 'DXXNNN' where X is uppercase letter and N is number")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Percentage is required")
    @DecimalMin(value = "0.0", message = "Percentage must be greater than or equal to 0")
    private BigDecimal percentage;
}