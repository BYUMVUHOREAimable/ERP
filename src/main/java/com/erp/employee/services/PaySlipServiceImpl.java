package com.erp.employee.services;

import com.erp.employee.dtos.request.CreatePaySlipDTO;
import com.erp.employee.dtos.response.PaySlipResponseDTO;
import com.erp.employee.enums.EmploymentStatus;
import com.erp.employee.enums.PaySlipStatus;

import com.erp.employee.exceptions.AppException;
import com.erp.employee.exceptions.BadRequestException;
import com.erp.employee.exceptions.ResourceAlreadyExistsException;
import com.erp.employee.exceptions.ResourceNotFoundException;

import com.erp.employee.interfaces.IEmployeeService;
import com.erp.employee.interfaces.IPaySlipService;
import com.erp.employee.models.*;
import com.erp.employee.repositories.*;
import com.erp.employee.standalone.ExcelService;
import com.erp.employee.standalone.MailService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaySlipServiceImpl implements IPaySlipService {

    private final IPaySlipRepository paySlipRepository;

    private final IEmployeeRepository employeeRepo;
    private final IEmploymentRepository employmentRepo;
    private final IDeductionRepository deductionRepo;
    private final IEmployeeService employeeService;
    private final MailService mailService;
    private final IMessageRepository messageRepo;
    private  final ExcelService excelService;

    @Override
    @Transactional
    public PaySlipResponseDTO generatePaySlip(CreatePaySlipDTO dto) {

        Employee employee = employeeRepo.findById(dto.getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("Employee not found", dto.getEmployeeId().toString(), "employeeId"));
        Employment employment = employmentRepo.findByEmployeeIdAndStatus(employee.getId(), EmploymentStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException("Active employment not found for employee", employee.getId().toString(), "employeeId"));
        checkDuplicatePayslip(dto.getEmployeeId(), dto.getMonth(), dto.getYear());
        // Get all active deductions
        List<Deduction> deductions = deductionRepo.findAll();

        // Calculate amounts
        BigDecimal baseSalary = employment.getBaseSalary();

        // Housing and Transport are benefits (added to gross)
        BigDecimal housingAmount = calculatePercentage(baseSalary, "Housing");
        BigDecimal transportAmount = calculatePercentage(baseSalary, "Transport");

        // Other deductions
        BigDecimal employeeTaxAmount = calculatePercentage(baseSalary, "EmployeeTax");
        BigDecimal pensionAmount = calculatePercentage(baseSalary, "Pension");
        BigDecimal medicalInsuranceAmount = calculatePercentage(baseSalary, "MedicalInsurance");
        BigDecimal otherTaxAmount = calculatePercentage(baseSalary, "Others");

        // Calculate gross and net salary
        BigDecimal grossSalary = baseSalary.add(housingAmount).add(transportAmount);
        BigDecimal totalDeductions = employeeTaxAmount.add(pensionAmount).add(medicalInsuranceAmount).add(otherTaxAmount);
        BigDecimal netSalary = grossSalary.subtract(totalDeductions);

        // Create and save payslip
        PaySlip paySlip = new PaySlip();
        paySlip.setEmployee(employment.getEmployee());
        paySlip.setHouseAmount(housingAmount);
        paySlip.setTransportAmount(transportAmount);
        paySlip.setEmployeeTaxAmount(employeeTaxAmount);
        paySlip.setPensionAmount(pensionAmount);
        paySlip.setMedicalInsuranceAmount(medicalInsuranceAmount);
        paySlip.setOtherTaxAmount(otherTaxAmount);
        paySlip.setGrossSalary(grossSalary);
        paySlip.setNetSalary(netSalary);
        paySlip.setMonth(dto.getMonth());
        paySlip.setYear(dto.getYear());
        paySlip.setStatus(PaySlipStatus.PENDING);

        PaySlip savedPaySlip = paySlipRepository.save(paySlip);

        // Convert to response DTO
        PaySlipResponseDTO response = new PaySlipResponseDTO();
        response.setId(savedPaySlip.getId().toString());
        response.setEmployeeId(employee.getId().toString());
        response.setEmployeeName(employee.getFullName());
        response.setBaseSalary(baseSalary);
        response.setHouseAmount(savedPaySlip.getHouseAmount());
        response.setTransportAmount(savedPaySlip.getTransportAmount());
        response.setEmployeeTaxAmount(savedPaySlip.getEmployeeTaxAmount());
        response.setPensionAmount(savedPaySlip.getPensionAmount());
        response.setMedicalInsuranceAmount(savedPaySlip.getMedicalInsuranceAmount());
        response.setOtherTaxAmount(savedPaySlip.getOtherTaxAmount());
        response.setGrossSalary(savedPaySlip.getGrossSalary());
        response.setNetSalary(savedPaySlip.getNetSalary());
        response.setMonth(dto.getMonth());
        response.setYear(dto.getYear());
        response.setStatus(savedPaySlip.getStatus());
        response.setCreatedAt(savedPaySlip.getCreatedAt());

        return response;
    }
//
//    @Override
//    public List<PaySlipResponseDTO> generateBulkPaySlips(Integer month, Integer year) {
//        return List.of();
//    }

    @Override
    public Page<PaySlipResponseDTO> getAllPaySlips(Integer month, Integer year, Pageable pageable) {
        Page<PaySlip> paySlips = paySlipRepository.findByMonthAndYear(month, year, pageable);
        return paySlips.map(this::mapToPaySlipResponseDTO);
    }


    @Override
    public Page<PaySlipResponseDTO> getMyPaySlips(int page, int size) {
        Employee employee = employeeService.getLoggedInEmployee();

        // Create a PageRequest with sorting by created date in descending order
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return paySlipRepository.findByEmployee(employee, pageRequest).map(payslip -> {
            PaySlipResponseDTO dto = new PaySlipResponseDTO();
            dto.setId(payslip.getId().toString());
            dto.setEmployeeId(payslip.getEmployee().getId().toString());
            dto.setEmployeeName(payslip.getEmployee().getFullName());
            dto.setBaseSalary(payslip.getEmployee().getEmployment().getBaseSalary());
            dto.setHouseAmount(payslip.getHouseAmount());
            dto.setTransportAmount(payslip.getTransportAmount());
            dto.setEmployeeTaxAmount(payslip.getEmployeeTaxAmount());
            dto.setPensionAmount(payslip.getPensionAmount());
            dto.setMedicalInsuranceAmount(payslip.getMedicalInsuranceAmount());
            dto.setOtherTaxAmount(payslip.getOtherTaxAmount());
            dto.setGrossSalary(payslip.getGrossSalary());
            dto.setNetSalary(payslip.getNetSalary());
            dto.setMonth(payslip.getMonth());
            dto.setYear(payslip.getYear());
            dto.setStatus(payslip.getStatus());
            dto.setCreatedAt(payslip.getCreatedAt());
            return dto;
        });
    }
    @Override
    public byte[] generateMyPaySlipExcel() {
        Employee employee = employeeService.getLoggedInEmployee();
        PaySlip payslip = paySlipRepository.findFirstByEmployeeOrderByCreatedAtDesc(employee)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found","id","id"));

        List<String> headers = Arrays.asList(
                "Employee Name", "Base Salary", "House Allowance", "Transport Allowance",
                "Tax Amount", "Pension Amount", "Medical Insurance", "Other Tax",
                "Gross Salary", "Net Salary", "Month", "Year", "Status"
        );

        List<List<String>> data = Collections.singletonList(Arrays.asList(
                payslip.getEmployee().getFullName(),
                payslip.getEmployee().getEmployment().getBaseSalary().toString(),
                payslip.getHouseAmount().toString(),
                payslip.getTransportAmount().toString(),
                payslip.getEmployeeTaxAmount().toString(),
                payslip.getPensionAmount().toString(),
                payslip.getMedicalInsuranceAmount().toString(),
                payslip.getOtherTaxAmount().toString(),
                payslip.getGrossSalary().toString(),
                payslip.getNetSalary().toString(),
                payslip.getMonth().toString(),
                payslip.getYear().toString(),
                payslip.getStatus().toString()
        ));

        try {
            return excelService.generatePaySlipExcel(headers, data);
        } catch (IOException e) {
            throw new AppException("failed to generate payslip excel", e);
        }
    }


//    @Override
//    public PaySlipResponseDTO getMyPaySlip(String employeeId, Integer month, Integer year) {
//        return null;
//    }

    public PaySlipResponseDTO approvePaySlip(UUID paySlipId) {
        PaySlip paySlip = paySlipRepository.findById(paySlipId).orElseThrow(() -> new ResourceNotFoundException("PaySlip not found", "pay_slip_id", paySlipId));

        if (paySlip.getStatus() == PaySlipStatus.PAID) {
            throw new BadRequestException("PaySlip is already approved");
        }


        paySlip.setStatus(PaySlipStatus.PAID);
        PaySlip savedPaySlip = paySlipRepository.save(paySlip);

        String message = composeSalaryCreditMessage(savedPaySlip);

        Message msg = new Message();
        msg.setEmployee(savedPaySlip.getEmployee());
        msg.setMessage(message);
        mailService.sendSalaryCreditNotification(savedPaySlip, message);
        return mapToPaySlipResponseDTO(savedPaySlip);
    }

//
//    @Override
//    public List<PaySlipResponseDTO> approveBulkPaySlips(Integer month, Integer year) {
//        return List.of();
//    }

    private BigDecimal calculatePercentage(BigDecimal amount, String deductionName) {
        Deduction deduction = deductionRepo.findByName((deductionName)).orElseThrow(() -> new ResourceNotFoundException("Deduction not found", deductionName, "deductionName"));

        return amount.multiply(deduction.getPercentage()).divide(BigDecimal.valueOf(100));
    }

    // Add before generating new payslip
    private void checkDuplicatePayslip(UUID employeeId, Integer month, Integer year) {
        boolean exists = paySlipRepository.existsByEmployee_IdAndMonthAndYear(employeeId, month, year);
        if (exists) {
            throw new ResourceAlreadyExistsException("Payslip already exists for this period");
        }
    }

    private PaySlipResponseDTO mapToPaySlipResponseDTO(PaySlip payslip) {
        PaySlipResponseDTO dto = new PaySlipResponseDTO();
        dto.setId(payslip.getId().toString());
        dto.setEmployeeId(payslip.getEmployee().getId().toString());
        dto.setEmployeeName(payslip.getEmployee().getFullName());
        dto.setBaseSalary(payslip.getEmployee().getEmployment().getBaseSalary());
        dto.setHouseAmount(payslip.getHouseAmount());
        dto.setTransportAmount(payslip.getTransportAmount());
        dto.setEmployeeTaxAmount(payslip.getEmployeeTaxAmount());
        dto.setPensionAmount(payslip.getPensionAmount());
        dto.setMedicalInsuranceAmount(payslip.getMedicalInsuranceAmount());
        dto.setOtherTaxAmount(payslip.getOtherTaxAmount());
        dto.setGrossSalary(payslip.getGrossSalary());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setMonth(payslip.getMonth());
        dto.setYear(payslip.getYear());
        dto.setStatus(payslip.getStatus());
        dto.setCreatedAt(payslip.getCreatedAt());
        return dto;
    }


    public String composeSalaryCreditMessage(PaySlip paySlip) {
        if (paySlip == null || paySlip.getEmployee() == null) {
            throw new IllegalArgumentException("PaySlip or Employee information is missing");
        }

        String monthName = getMonthName(paySlip.getMonth());
        String institution = "ERP System"; // You might want to make this configurable

        return String.format("Dear %s, your salary for %s/%d from %s amounting to %s has been credited to your account %s successfully.", paySlip.getEmployee().getFirstName(), monthName, paySlip.getYear(), institution, formatCurrency(paySlip.getNetSalary()), paySlip.getEmployee().getId());
    }

    private String getMonthName(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month number");
        }
        return java.time.Month.of(month).name();
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return String.format("%,.2f", amount);
    }
}