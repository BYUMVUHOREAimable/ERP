package com.erp.employee.controllers;

import com.erp.employee.dtos.response.ApiResponseDTO;
import com.erp.employee.interfaces.IEmployeeService;
import com.erp.employee.models.Employee;
import com.erp.employee.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/Employees")
@RequiredArgsConstructor

public class EmployeeController {
    private final IEmployeeService EmployeeService;

    @Operation(summary = "get loggedIn Employee ", description = "get loggedIn Employee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " get loggedIn Employee fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO> getCurrentEmployee() {
        return ResponseEntity.ok(ApiResponseDTO.success("employee",EmployeeService.getLoggedInEmployee()));
    }


    @Operation(summary = "get all Employee ", description = "get all Employee ", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " get all Employee  fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponseDTO> getAllEmployees(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = Pageable.ofSize(limit).withPage(page);
        return ResponseEntity.ok(ApiResponseDTO.success("all employees fetched", EmployeeService.getAllEmployees(pageable)));
    }
}
