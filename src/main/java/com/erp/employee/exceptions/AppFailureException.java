package com.erp.employee.exceptions;

import com.erp.employee.dtos.response.ApiResponseDTO;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class AppFailureException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO> handleAnyError(RuntimeException exception) {
        return ResponseEntity.badRequest().body(ApiResponseDTO.error(exception.getMessage(), exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO> handleValidations(MethodArgumentNotValidException exception) {
        FieldError error = Objects.requireNonNull(exception.getFieldError());
        String message = error.getField() + ": " + error.getDefaultMessage();
        return ResponseEntity.badRequest().body(ApiResponseDTO.error(message, error));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO> handleSqlExceptions(ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(ApiResponseDTO.error(exception.getMessage() + " - " + exception.getSQL() + " - " + exception.getSQLState(), exception.getSQLException()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDTO> handleResourceAlreadyExists(ResourceAlreadyExistsException exception) {
        return ResponseEntity.status(409).body(ApiResponseDTO.error(exception.getMessage(), null));
    }

}