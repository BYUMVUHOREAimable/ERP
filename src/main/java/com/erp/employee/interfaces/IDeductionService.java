package com.erp.employee.interfaces;

import com.erp.employee.dtos.request.CreateDeductionDTO;
import com.erp.employee.dtos.request.UpdateDeductionDTO;
import com.erp.employee.dtos.response.DeductionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDeductionService {
    String initializeDeduction();
    DeductionResponseDTO createDeduction(CreateDeductionDTO dto);
    DeductionResponseDTO getDeductionById(Long id);
    Page<DeductionResponseDTO> getAllDeductions(Pageable pageable);
    DeductionResponseDTO updateDeduction(Long id, UpdateDeductionDTO dto);
    void deleteDeduction(Long id);
}