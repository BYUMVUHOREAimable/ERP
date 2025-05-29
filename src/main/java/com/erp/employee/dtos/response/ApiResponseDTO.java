package com.erp.employee.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO {
    private Boolean success;
    private String message;
    private Object data;

    public ApiResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ApiResponseDTO success(String message, Object data) {
        return new ApiResponseDTO(true, message, data);
    }

    public static ApiResponseDTO success(String message) {
        return new ApiResponseDTO(true, message);
    }

    public static ApiResponseDTO error(String message, Object data) {
        return new ApiResponseDTO(false, message, data);
    }

    public static ApiResponseDTO error(String message) {
        return new ApiResponseDTO(false, message);
    }

}