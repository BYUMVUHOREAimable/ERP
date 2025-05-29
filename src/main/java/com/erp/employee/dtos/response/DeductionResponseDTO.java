package com.erp.employee.dtos.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DeductionResponseDTO {
    private UUID id;
    private String code;
    private String name;
    private BigDecimal percentage;
}