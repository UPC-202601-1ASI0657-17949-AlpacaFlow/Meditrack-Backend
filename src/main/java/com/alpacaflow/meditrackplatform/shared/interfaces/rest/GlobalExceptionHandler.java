package com.alpacaflow.meditrackplatform.shared.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.exceptions.OrganizationDuplicateNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleException(MethodArgumentNotValidException ex) {
        String message = ex.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage() == null ? "" : fieldError.getDefaultMessage()).reduce("", String::concat);
        return ErrorResponse.create(
                ex,
                HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()),
                message
        );
    }

    @ExceptionHandler(OrganizationDuplicateNameException.class)
    public ResponseEntity<String> handleOrganizationDuplicateName(OrganizationDuplicateNameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleException(IllegalArgumentException ex) {
        return ErrorResponse.create(ex, HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage());
    }

}
