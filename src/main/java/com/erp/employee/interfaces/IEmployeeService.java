package com.erp.employee.interfaces;

import com.erp.employee.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEmployeeService {
    Employee getLoggedInEmployee();

    Page<Employee> getAllEmployees(Pageable pageable);
}
