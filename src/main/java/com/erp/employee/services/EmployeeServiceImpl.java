package com.erp.employee.services;

import com.erp.employee.exceptions.BadRequestException;
import com.erp.employee.exceptions.ResourceNotFoundException;
import com.erp.employee.interfaces.IEmployeeService;
import com.erp.employee.models.Employee;
import com.erp.employee.repositories.IEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements IEmployeeService {
    private final IEmployeeRepository employeeRepo;

    @Override
    public Employee getLoggedInEmployee() {
        // Get the current authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if Employee is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("No authenticated Employee found");
        }

        Object principal = authentication.getPrincipal();

        // Ensure principal is of type EmployeeDetails
        if (!(principal instanceof UserDetails)) {
            throw new BadRequestException("Invalid authentication principal");
        }

        String employeeName = ((UserDetails) principal).getUsername();

        return employeeRepo.findByEmail(employeeName)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: ", employeeName, "email"));
    }

    @Override
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepo.findAll(pageable);
    }

}
