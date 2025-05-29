package com.erp.employee.interfaces;

import com.erp.employee.dtos.request.CreateEmployeeDTO;
import com.erp.employee.dtos.request.LoginDTO;
import com.erp.employee.dtos.response.JwtAuthenticationResponse;
import com.erp.employee.models.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface IAuthService {
    Employee registerEmployee(@Valid CreateEmployeeDTO dto);
    Employee registerEmployeeManager(@Valid CreateEmployeeDTO dto);


    JwtAuthenticationResponse login(@Valid LoginDTO dto);

    void verifyAccount(String verificationCode);

    void initiateAccountVerification(@Email String email);


    void initiateResetPassword(@NotBlank @Email String email);

    void resetPassword(@NotBlank String email, @NotBlank String passwordResetCode, String newPassword);
}
