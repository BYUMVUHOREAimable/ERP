package com.erp.employee.controllers;

import com.erp.employee.dtos.request.*;

import com.erp.employee.dtos.response.ApiResponseDTO;

import com.erp.employee.interfaces.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @Operation(summary = "register Employee ", description = "register Employee Employee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " register Employee successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/register")
    ResponseEntity<ApiResponseDTO> registerUser(@Valid @RequestBody CreateEmployeeDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.success("user created successfully", authService.registerEmployee(dto)));
    }

    @Operation(summary = "register manager Employee ", description = "register manager Employee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " register manager Employee successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/manager")
    ResponseEntity<ApiResponseDTO> registerEmployeeManager(@Valid @RequestBody CreateEmployeeDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.success("user created successfully", authService.registerEmployeeManager(dto)));
    }

    //    login
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    ResponseEntity<ApiResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.success("user logged in  successfully", authService.login(dto)));
    }

    //    initiate account verification
    @Operation(summary = "Initiate account verification by sending code to email")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Verification code sent"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email format or email not found")
    })
    @PatchMapping("/initiate-account-verification")
    private ResponseEntity<ApiResponseDTO> initiateAccountVerification(@RequestBody @Valid InitiateAccountVerificationDTO dto) {
        this.authService.initiateAccountVerification(dto.getEmail());
        return ResponseEntity.ok(ApiResponseDTO.success("Verification code sent to email, expiring in 6 hours"));
    }


    //verify account
    @Operation(summary = "Verify user account using the provided verification code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account verified successfully"),
            @ApiResponse(responseCode = "404", description = "Verification code not found or expired")
    })
    @PatchMapping("/verify-account/{verificationCode}")
    private ResponseEntity<ApiResponseDTO> verifyAccount(
            @PathVariable("verificationCode") String verificationCode
    ) {
        this.authService.verifyAccount(verificationCode);
        return ResponseEntity.ok(ApiResponseDTO.success("Account verified successfully, you can now login"));
    }


    //    initiate password reset
    @Operation(summary = "Send password reset code to user's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset code sent"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    @PatchMapping(path = "/initiate-reset-password")
    public ResponseEntity<ApiResponseDTO> initiateResetPassword(@RequestBody @Valid InitiatePasswordResetDTO dto) {
        this.authService.initiateResetPassword(dto.getEmail());
        return ResponseEntity.ok(ApiResponseDTO.success("Please check your mail and activate account"));
    }

    //reset password
    @Operation(summary = "Reset password using reset code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully reset"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired reset code")
    })
    @PatchMapping(path = "/reset-password")
    public ResponseEntity<ApiResponseDTO> resetPassword(@RequestBody @Valid ResetPasswordDTO dto) {
        this.authService.resetPassword(dto.getEmail(), dto.getPasswordResetCode(), dto.getNewPassword());
        return ResponseEntity.ok(ApiResponseDTO.success("Password successfully reset"));
    }
}
