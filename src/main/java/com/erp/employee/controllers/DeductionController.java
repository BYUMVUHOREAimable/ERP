package com.erp.employee.controllers;

import com.erp.employee.dtos.request.CreateDeductionDTO;
import com.erp.employee.dtos.request.UpdateDeductionDTO;
import com.erp.employee.dtos.response.ApiResponseDTO;
import com.erp.employee.interfaces.IDeductionService;
import com.erp.employee.interfaces.IEmployeeService;

import com.erp.employee.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deductions")
@RequiredArgsConstructor
public class DeductionController {
    private final IDeductionService service;
    private final IEmployeeService EmployeeService;

    @Operation(summary = "initialize deduction", description = "initialize deduction", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " initialize deduction fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @PostMapping("/initialize")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO> getCurrentEmployee() {
        return ResponseEntity.ok(ApiResponseDTO.success(service.initializeDeduction(), "Deduction initialized successfully"));
    }


    @Operation(summary = "Create new deduction", description = "Create new deduction", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO> createDeduction(@Valid @RequestBody CreateDeductionDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.success("Deduction created successfully", service.createDeduction(dto)));
    }

    @Operation(summary = "Get deduction by ID", description = "Get deduction by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO> getDeductionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.success("Deduction fetched successfully", service.getDeductionById(id)));
    }

    @Operation(summary = "Get all deductions", description = "Get all deductions", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deductions fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO> getAllDeductions(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = Pageable.ofSize(limit).withPage(page);
        return ResponseEntity.ok(ApiResponseDTO.success("Deductions fetched successfully", service.getAllDeductions(pageable)));
    }

    @Operation(summary = "Update deduction", description = "Update deduction", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO> updateDeduction(@PathVariable Long id, @Valid @RequestBody UpdateDeductionDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.success("Deduction updated successfully", service.updateDeduction(id, dto)));
    }

    @Operation(summary = "Delete deduction", description = "Delete deduction", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO> deleteDeduction(@PathVariable Long id) {
        service.deleteDeduction(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Deduction deleted successfully"));
    }

}
