package com.erp.employee.dtos.request;

import lombok.Getter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
public class InitiatePasswordResetDTO {

    @NotBlank
    @Email
    private String email;
}
