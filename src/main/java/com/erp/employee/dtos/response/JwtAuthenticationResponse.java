package com.erp.employee.dtos.response;

import com.erp.employee.models.Employee;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Employee Employee;

    public JwtAuthenticationResponse(String accessToken, Employee Employee) {
        this.accessToken = accessToken;
        this.Employee = Employee;
    }
}