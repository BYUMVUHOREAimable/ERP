package com.erp.employee.models;

import com.erp.employee.audits.InitiatorAudit;
import com.erp.employee.enums.PaySlipStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "payslips")
public class PaySlip extends InitiatorAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "house-amount")
    private BigDecimal houseAmount;

    @Column(name = "transport_amount")
    private BigDecimal transportAmount;

    @Column(name = "employee_tax_amount")
    private BigDecimal employeeTaxAmount;

    @Column(name = "pension_amount")
    private BigDecimal pensionAmount;

    @Column(name = "medical_insurance_amount")
    private BigDecimal medicalInsuranceAmount;

    @Column(name = "other_tax_amount")
    private BigDecimal otherTaxAmount;

    @Column(name = "gross_salary_amount")
    private BigDecimal grossSalary;

    @Column(name = "net_salary")
    private BigDecimal netSalary;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaySlipStatus status = PaySlipStatus.PENDING;


}
