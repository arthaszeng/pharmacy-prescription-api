package com.arthas.pharmacyprescriptionapi.presentation.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
        when(mockRequest.getRequestURI()).thenReturn("/test-endpoint");
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<ErrorRepresentation> response = exceptionHandler.handleGenericException(ex, mockRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred.");
        assertThat(response.getBody().getPath()).isEqualTo("/test-endpoint");
    }

    @Test
    void shouldHandleTypeMismatchException() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException("123", Integer.class, "param", null, new RuntimeException("Type mismatch"));

        ResponseEntity<ErrorRepresentation> response = exceptionHandler.handleTypeMismatchException(ex, mockRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Invalid value '123' for parameter 'param'");
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input provided");

        ResponseEntity<ErrorRepresentation> response = exceptionHandler.handleIllegalArgumentException(ex, mockRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid input provided");
    }

    @Test
    void shouldHandleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate entry");

        ResponseEntity<ErrorRepresentation> response = exceptionHandler.handleDataIntegrityViolation(ex, mockRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Data integrity error");
        assertThat(response.getBody().getDetails()).contains("Duplicate entry");
    }

    @Test
    void shouldHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Unexpected runtime error");

        ResponseEntity<ErrorRepresentation> response = exceptionHandler.handleRuntimeException(ex, mockRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
    }

    @Test
    void shouldHandleOptimisticLockException() {
        IllegalStateException ex = new IllegalStateException("Optimistic lock conflict");

        ResponseEntity<ErrorRepresentation> response = exceptionHandler.handleOptimisticLockException(ex, mockRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Stock update conflict, please try again");
    }
}