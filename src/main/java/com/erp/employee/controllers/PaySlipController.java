package com.erp.employee.controllers;

import com.erp.employee.dtos.request.CreatePaySlipDTO;
import com.erp.employee.dtos.response.ApiResponseDTO;

import com.erp.employee.interfaces.IPaySlipService;
import com.erp.employee.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payslips")
@RequiredArgsConstructor
@Tag(name = "Payslip Management", description = "Endpoints for managing employee payslips")
public class PaySlipController {

    private final IPaySlipService paySlipService;

    @Operation(
            summary = "Generate payslip for an employee",
            description = "Generates a new payslip for a specific employee for given month and year",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payslip generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "409", description = "Payslip already exists for this period")
    })
    @PostMapping("/generate")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponseDTO> generatePaySlip(
            @Valid @RequestBody CreatePaySlipDTO dto
    ) {
        return ResponseEntity.ok(ApiResponseDTO.success("Payslip generated successfully",
                paySlipService.generatePaySlip(dto)

        ));
    }

    @Operation(
            summary = "Get all payslips",
            description = "Retrieves all payslips for a specific month and year with pagination",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payslips retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "400", description = "Invalid month or year")
    })
    @GetMapping("/all/{month}/{year}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO> getAllPaySlips(
            @Parameter(description = "Month (1-12)") @PathVariable Integer month,
            @Parameter(description = "Year (e.g., 2025)") @PathVariable Integer year,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size
    ) {
        if (month < 1 || month > 12) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Invalid month. Month should be between 1 and 12"));
        }

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDTO.success(
                "Payslips retrieved successfully",
                paySlipService.getAllPaySlips(month, year, pageable)
        ));
    }
//    @Operation(
//            summary = "Generate bulk payslips",
//            description = "Generates payslips for all active employees for given month and year",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Bulk payslips generated successfully"),
//            @ApiResponse(responseCode = "403", description = "Unauthorized access")
//    })
//    @PostMapping("/generate/bulk/{month}/{year}")
//    @PreAuthorize("hasRole('MANAGER')")
//    public ResponseEntity<ApiResponseDTO> generateBulkPaySlips(
//            @Parameter(description = "Month (1-12)") @PathVariable Integer month,
//            @Parameter(description = "Year (e.g., 2025)") @PathVariable Integer year
//    ) {
//        return ResponseEntity.ok(ApiResponseDTO.success("Bulk payslips generated successfully",
//                paySlipService.generateBulkPaySlips(month, year)
//
//        ));
//    }

    @Operation(
            summary = "Get employee's payslips",
            description = "Retrieves all payslips for the logged-in employee",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/my-payslips")
    public ResponseEntity<ApiResponseDTO> getMyPaySlips(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size
    ) {
        return ResponseEntity.ok(ApiResponseDTO.success("Payslips retrieved successfully",
                paySlipService.getMyPaySlips(page, size)

        ));
    }

    @Operation(
            summary = "Get employee's payslips",
            description = "Retrieves all payslips for the logged-in employee",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @GetMapping("/my-payslips/download")
    public ResponseEntity<byte[]> downloadMyPaySlip() {
        byte[] excelContent = paySlipService.generateMyPaySlipExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "payslip.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelContent);
    }

    @Operation(
            summary = "Approve payslip",
            description = "Approves a specific payslip by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO> approvePaySlip(
            @Parameter(description = "Payslip ID") @PathVariable UUID id
    ) {
        return ResponseEntity.ok(ApiResponseDTO.success("Payslip approved successfully",
                paySlipService.approvePaySlip(id)

        ));
    }

//    @Operation(
//            summary = "Get all payslips",
//            description = "Retrieves all payslips for a specific month and year",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @GetMapping("/all/{month}/{year}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponseDTO> getAllPaySlips(
//            @Parameter(description = "Month (1-12)") @PathVariable Integer month,
//            @Parameter(description = "Year (e.g., 2025)") @PathVariable Integer year,
//            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
//            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size
//    ) {
//        return ResponseEntity.ok(ApiResponseDTO.success("Payslips retrieved successfully",
//                paySlipService.getAllPaySlips(month, year, page, size)
//
//        ));
//    }
}