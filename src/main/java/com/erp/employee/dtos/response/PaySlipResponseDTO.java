// PaySlipResponseDTO.java
package com.erp.employee.dtos.response;

import com.erp.employee.enums.PaySlipStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaySlipResponseDTO {
    private String id;
    private String employeeId;
    private String employeeName;
    private BigDecimal baseSalary;
    private BigDecimal houseAmount;
    private BigDecimal transportAmount;
    private BigDecimal employeeTaxAmount;
    private BigDecimal pensionAmount;
    private BigDecimal medicalInsuranceAmount;
    private BigDecimal otherTaxAmount;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private Integer month;
    private Integer year;
    private PaySlipStatus status;
    private LocalDateTime createdAt;
}