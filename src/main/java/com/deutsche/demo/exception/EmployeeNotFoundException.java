package com.deutsche.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Task: EmployeeNotFoundException completeness
// This annotation makes Spring automatically return a 404 Not Found when this exception is thrown.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Integer id) {
        super("Employee not found with ID: " + id);
    }

    // Task: Constructor for findEmployeeByName (by name)
    public EmployeeNotFoundException(String name) {
        super("Employee not found with Name: " + name);
    }
}