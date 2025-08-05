package com.deutsche.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException; // Import for validation errors
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus; // Import for @ResponseStatus
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Void> handleEmployeeNotFoundException(EmployeeNotFoundException e) {
        LOG.error("Handling EmployeeNotFoundException: {}", e.getMessage());
        // No need to return a body for a 404, but a message in header is good.
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("message", e.getMessage())
                .build(); // Use .build() as body is null/void
    }

    // Task: Add handler for MethodArgumentNotValidException (validation errors)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Returns 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        LOG.error("Handling MethodArgumentNotValidException: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors; // Returns a JSON map of field errors
    }

    // Generic handler for any other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllOtherExceptions(Exception e) {
        LOG.error("An unexpected error occurred: {}", e.getMessage(), e); // Log full stack trace
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("message", "An unexpected server error occurred: " + e.getMessage())
                .body("An internal server error occurred. Please try again later.");
    }
}