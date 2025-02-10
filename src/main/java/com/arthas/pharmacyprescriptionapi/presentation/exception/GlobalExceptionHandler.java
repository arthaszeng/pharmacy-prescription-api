package com.arthas.pharmacyprescriptionapi.presentation.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles generic exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRepresentation> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred.",
                request.getRequestURI()
        );
    }

    /**
     * Handles validation errors for request payloads.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRepresentation> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation error: {}", ex.getMessage());

        List<String> details = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error instanceof FieldError fieldError
                        ? fieldError.getField() + " " + fieldError.getDefaultMessage()
                        : error.getDefaultMessage())
                .collect(Collectors.toList());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), details);
    }

    /**
     * Handles parameter type mismatches.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRepresentation> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.warn("Type mismatch: {}", ex.getMessage());
        String message = String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    /**
     * Handles illegal argument exceptions, typically business validation errors.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRepresentation> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Business validation error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handles database integrity constraint violations.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRepresentation> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Database integrity violation: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Data integrity error", request.getRequestURI(), List.of(ex.getMessage()));
    }

    /**
     * Handles unexpected runtime exceptions.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorRepresentation> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("Unexpected runtime error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request.getRequestURI());
    }

    /**
     * Builds the error response entity.
     */
    private ResponseEntity<ErrorRepresentation> buildErrorResponse(HttpStatus status, String message, String path) {
        return buildErrorResponse(status, message, path, null);
    }

    /**
     * Builds the error response entity with optional details.
     */
    private ResponseEntity<ErrorRepresentation> buildErrorResponse(HttpStatus status, String message, String path, List<String> details) {
        ErrorRepresentation.ErrorRepresentationBuilder responseBuilder = ErrorRepresentation.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now());

        if (details != null && !details.isEmpty()) {
            responseBuilder.details(details);
        }

        return ResponseEntity.status(status).body(responseBuilder.build());
    }
}