package com.erp.employee.models;

import com.erp.employee.audits.InitiatorAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deductions")
public class Deduction extends InitiatorAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "percentage")
    private BigDecimal percentage;


    public Deduction(String dtx001, String name, BigDecimal bigDecimal) {
        this.code = dtx001;
        this.name = name;
        this.percentage = bigDecimal;
    }
}
