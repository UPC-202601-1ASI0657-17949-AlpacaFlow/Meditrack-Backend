package com.alpacaflow.meditrackplatform.organization.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.exceptions.CaregiverNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.exceptions.DoctorNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.exceptions.SeniorCitizenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for REST controllers.
 * This handler catches exceptions that occur during request processing, including deserialization errors.
 */
@ControllerAdvice(basePackages = "com.alpacaflow.meditrackplatform.organization.interfaces.rest")
public class GlobalExceptionHandler {

    /**
     * Handles HTTP message not readable exceptions (e.g., JSON deserialization failures).
     * This helps diagnose issues with request body parsing.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        System.out.println("❌ [GlobalExceptionHandler] Failed to deserialize request body: " + ex.getMessage());
        System.out.println("❌ [GlobalExceptionHandler] Root cause: " + (ex.getRootCause() != null ? ex.getRootCause().getMessage() : "N/A"));
        if (ex.getRootCause() != null) {
            System.out.println("❌ [GlobalExceptionHandler] Root cause class: " + ex.getRootCause().getClass().getName());
            ex.getRootCause().printStackTrace();
        }
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Failed to parse request body: " + (ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage()));
    }

    /**
     * Handles IllegalArgumentException that might be thrown during record construction or validation.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        System.out.println("❌ [GlobalExceptionHandler] IllegalArgumentException: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid request: " + ex.getMessage());
    }

    /**
     * Handles IllegalStateException that might be thrown during business logic validation.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        System.out.println("❌ [GlobalExceptionHandler] IllegalStateException: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid state: " + ex.getMessage());
    }

    /**
     * Handles RuntimeException that might wrap other exceptions.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        System.out.println("❌ [GlobalExceptionHandler] RuntimeException: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error: " + ex.getMessage());
    }

    /**
     * Handles DoctorNotFoundException.
     */
    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<String> handleDoctorNotFoundException(DoctorNotFoundException ex) {
        System.out.println("❌ [GlobalExceptionHandler] DoctorNotFoundException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Handles CaregiverNotFoundException.
     */
    @ExceptionHandler(CaregiverNotFoundException.class)
    public ResponseEntity<String> handleCaregiverNotFoundException(CaregiverNotFoundException ex) {
        System.out.println("❌ [GlobalExceptionHandler] CaregiverNotFoundException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Handles SeniorCitizenNotFoundException.
     */
    @ExceptionHandler(SeniorCitizenNotFoundException.class)
    public ResponseEntity<String> handleSeniorCitizenNotFoundException(SeniorCitizenNotFoundException ex) {
        System.out.println("❌ [GlobalExceptionHandler] SeniorCitizenNotFoundException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}

