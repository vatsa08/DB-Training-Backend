package com.deutsche.demo.controller;

import com.deutsche.demo.exception.EmployeeNotFoundException; // Ensure this import is correct
import com.deutsche.demo.model.Employee;
import com.deutsche.demo.service.EmployeeService;
import jakarta.validation.Valid; // Keep for validation
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Import Map for validation error response
import java.util.HashMap; // Import HashMap for validation error response

// Task: methods in this class should not return raw business objects, they should return ResponseEntity objects
// Task: LOG as applicable
// Task: Update methods in controller class
@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("employees") // Remember: This is prefixed by /api/v1 from application.properties
public class EmployeeController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass()); // Task: Use logging

    @Autowired
    private EmployeeService employeeService;

    // GET http://localhost:8090/api/v1/employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() { // Returns ResponseEntity
        LOG.info("Controller: Received request to get all employees.");
        List<Employee> employees = employeeService.getAllEmployees();
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "All employees fetched successfully.");
        return new ResponseEntity<>(employees, headers, HttpStatus.OK); // Returns 200 OK
    }

    // GET http://localhost:8090/api/v1/employees/101
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
        LOG.info("Controller: Received request to get employee by ID: {}.", id);
        try {
            Employee employee = employeeService.getEmployeeById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", "Employee with ID " + id + " fetched successfully.");
            return new ResponseEntity<>(employee, headers, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            // Task: Handle exceptions from service (e.g., 404 Not Found)
            LOG.warn("Controller: Employee not found for ID: {}. Error: {}", id, e.getMessage());
            // GlobalExceptionHandler will catch this and return 404
            throw e;
        } catch (Exception e) {
            LOG.error("Controller: An unexpected error occurred while fetching employee with ID {}: {}", id, e.getMessage(), e);
            // GlobalExceptionHandler will catch generic Exception if not handled more specifically
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage()); // Re-throw as RuntimeException or custom generic exception
        }
    }

    // Task: Create functionality for findEmployeeByName (New Endpoint)
    // GET http://localhost:8090/api/v1/employees/name/John
    @GetMapping("/name/{name}") // Use a distinct path segment for searching by name
    public ResponseEntity<List<Employee>> getEmployeesByName(@PathVariable String name) {
        LOG.info("Controller: Received request to get employees by name: {}.", name);
        try {
            List<Employee> employees = employeeService.findEmployeeByName(name);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", employees.size() + " employee(s) found with name: " + name + ".");
            return new ResponseEntity<>(employees, headers, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            LOG.warn("Controller: No employees found with name: {}. Error: {}", name, e.getMessage());
            throw e; // Re-throw to trigger 404 via GlobalExceptionHandler
        } catch (Exception e) {
            LOG.error("Controller: An unexpected error occurred while fetching employee by name {}: {}", name, e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    // POST http://localhost:8090/api/v1/employees
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) { // Returns ResponseEntity
        LOG.info("Controller: Received request to add new employee.");
        // GlobalExceptionHandler now handles MethodArgumentNotValidException
        Employee newEmployee = employeeService.addEmployee(employee);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "Employee " + newEmployee.getName() + " added successfully.");
        return new ResponseEntity<>(newEmployee, headers, HttpStatus.CREATED); // Returns 201 Created
    }

    // PUT http://localhost:8090/api/v1/employees
    @PutMapping
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee) { // Returns ResponseEntity
        LOG.info("Controller: Received request to update employee with ID: {}.", employee.getId());
        try {
            // Validate that ID is provided for update operations
            if (employee.getId() == null) {
                LOG.warn("Controller: Update failed - Employee ID not provided in request body.");
                HttpHeaders headers = new HttpHeaders();
                headers.add("message", "Employee ID must be provided for update operations.");
                return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST); // 400 Bad Request
            }
            Employee updatedEmployee = employeeService.updateEmployee(employee);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", "Employee with ID " + updatedEmployee.getId() + " updated successfully.");
            return new ResponseEntity<>(updatedEmployee, headers, HttpStatus.OK); // Returns 200 OK
        } catch (EmployeeNotFoundException e) {
            LOG.error("Controller: Update failed - Employee not found for ID: {}. Error: {}", employee.getId(), e.getMessage());
            throw e; // Re-throw to trigger 404 via GlobalExceptionHandler
        } catch (Exception e) {
            LOG.error("Controller: An unexpected error occurred while updating employee with ID {}: {}", employee.getId(), e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    // DELETE http://localhost:8090/api/v1/employees/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) { // Returns ResponseEntity<Void> for 204
        LOG.info("Controller: Received request to delete employee with ID: {}.", id);
        try {
            Employee deletedEmployee = employeeService.deleteEmployee(id); // Service now returns the deleted employee
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", "Employee with ID " + deletedEmployee.getId() + " deleted successfully.");
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT); // Returns 204 No Content
        } catch (EmployeeNotFoundException e) {
            LOG.error("Controller: Delete failed - Employee not found for ID: {}. Error: {}", id, e.getMessage());
            throw e; // Re-throw to trigger 404 via GlobalExceptionHandler
        } catch (Exception e) {
            LOG.error("Controller: An unexpected error occurred while deleting employee with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }
}