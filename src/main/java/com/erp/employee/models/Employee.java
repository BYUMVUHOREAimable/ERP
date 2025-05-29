package com.erp.employee.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.erp.employee.audits.InitiatorAudit;
import com.erp.employee.enums.EAccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "employees")

public class Employee extends InitiatorAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "mobile", unique = true, nullable = false)
    private String mobile;

    @Column(name = "nationalid", unique = true, nullable = false)
    private String nationalId;

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EAccountStatus status = EAccountStatus.ACTIVE;

    @JoinColumn(name = "profile_image_id")
    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private File profileImage;

    @Column(name = "activation_code")
    private String activationCode;

    @JsonIgnore
    @Column(name = "activation_code_expires_at")
    private LocalDateTime activationCodeExpiresAt;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PaySlip> payslips = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employment employment;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
