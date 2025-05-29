package com.erp.employee.interfaces;

import com.erp.employee.dtos.request.CreatePaySlipDTO;
import com.erp.employee.dtos.response.PaySlipResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPaySlipService {
    // For Manager
    PaySlipResponseDTO generatePaySlip(CreatePaySlipDTO dto);

    //    List<PaySlipResponseDTO> generateBulkPaySlips(Integer month, Integer year);
    Page<PaySlipResponseDTO> getAllPaySlips(Integer month, Integer year, Pageable pageable);

    // For Employee
    Page<PaySlipResponseDTO> getMyPaySlips(int page, int size);
//    PaySlipResponseDTO getMyPaySlip(String employeeId, Integer month, Integer year);

    // For Admin
    PaySlipResponseDTO approvePaySlip(UUID paySlipId);

    //    List<PaySlipResponseDTO> approveBulkPaySlips(Integer month, Integer year);
    byte[] generateMyPaySlipExcel();

}