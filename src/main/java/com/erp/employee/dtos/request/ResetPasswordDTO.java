package com.erp.employee.dtos.request;

import com.erp.employee.validators.ValidPassword;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;

@Getter
public class ResetPasswordDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String passwordResetCode;

    @ValidPassword
    private String newPassword;
}
