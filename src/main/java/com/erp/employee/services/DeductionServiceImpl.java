package com.erp.employee.services;

import com.erp.employee.dtos.request.CreateDeductionDTO;
import com.erp.employee.dtos.request.UpdateDeductionDTO;
import com.erp.employee.dtos.response.DeductionResponseDTO;
import com.erp.employee.exceptions.ResourceNotFoundException;
import com.erp.employee.interfaces.IDeductionService;
import com.erp.employee.models.Deduction;
import com.erp.employee.repositories.IDeductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeductionServiceImpl implements IDeductionService {
    private final IDeductionRepository deductionRepo;


    @Override
    public String initializeDeduction() {
        List<Deduction> defaultDeductions = Arrays.asList(
                new Deduction("DTX001", "EmployeeTax", new BigDecimal("30")),
                new Deduction("DPN001", "Pension", new BigDecimal("6")),
                new Deduction("DMI001", "MedicalInsurance", new BigDecimal("5")),
                new Deduction("DOT001", "Others", new BigDecimal("5")),
                new Deduction("DHO001", "Housing", new BigDecimal("14")),
                new Deduction("DTR001", "Transport", new BigDecimal("14"))
        );

        for (Deduction deduction : defaultDeductions) {
            if (!deductionRepo.existsByCode(deduction.getCode())) {
                deductionRepo.save(deduction);
            }
        }
        return "Deduction initialized successfully";
    }
    @Override
    public DeductionResponseDTO createDeduction(CreateDeductionDTO dto) {
        Deduction deduction = new Deduction();
        deduction.setCode(dto.getCode());
        deduction.setName(dto.getName());
        deduction.setPercentage(dto.getPercentage());
        return convertToDTO(deductionRepo.save(deduction));
    }
    @Override
    public DeductionResponseDTO getDeductionById(Long id) {
        return convertToDTO(deductionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction", id.toString(), "id")));
    }

    @Override
    public Page<DeductionResponseDTO> getAllDeductions(Pageable pageable) {
        return deductionRepo.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public DeductionResponseDTO updateDeduction(Long id, UpdateDeductionDTO dto) {
        Deduction deduction = deductionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction", id.toString(), "id"));

        deduction.setName(dto.getName());
        deduction.setCode(dto.getCode());
        deduction.setPercentage(dto.getPercentage());

        return convertToDTO(deductionRepo.save(deduction));
    }

    @Override
    public void deleteDeduction(Long id) {
        Deduction deduction = deductionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction", id.toString(), "id"));
        deductionRepo.delete(deduction);
    }

    private DeductionResponseDTO convertToDTO(Deduction deduction) {
        DeductionResponseDTO dto = new DeductionResponseDTO();
        dto.setId(deduction.getId());
        dto.setCode(deduction.getCode());
        dto.setName(deduction.getName());
        dto.setPercentage(deduction.getPercentage());
        return dto;
    }

}

