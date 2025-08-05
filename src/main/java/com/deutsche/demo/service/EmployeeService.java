package com.deutsche.demo.service;

import com.deutsche.demo.exception.EmployeeNotFoundException;
import com.deutsche.demo.model.Employee;
import com.deutsche.demo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass()); // Task: Use logging

    @Autowired
    EmployeeRepository employeeRepository;

    // Task: Update methods in service class
    public List<Employee> getAllEmployees() {
        LOG.info("Service: Fetching all employees.");
        return employeeRepository.findAll();
    }

    // Task: Update methods in service class (Consistent error handling)
    public Employee getEmployeeById(Integer id) {
        LOG.info("Service: Attempting to fetch employee with ID: {}.", id);
        // Your existing logic is perfect here for throwing EmployeeNotFoundException.
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    LOG.warn("Service: Employee with ID: {} not found.", id);
                    return new EmployeeNotFoundException(id);
                });
    }

    // Task: Update methods in service class
    public Employee addEmployee(Employee employee) {
        LOG.info("Service: Adding new employee: {}.", employee.getName());
        // For a POST, the ID should ideally be null as it's auto-generated.
        // Spring Data JPA save() handles both insert and update.
        Employee savedEmployee = employeeRepository.save(employee);
        LOG.info("Service: Employee added successfully with ID: {}.", savedEmployee.getId());
        return savedEmployee;
    }

    // Task: Update methods in service class (Completed Update Logic)
    public Employee updateEmployee(Employee employee) {
        LOG.info("Service: Attempting to update employee with ID: {}.", employee.getId());

        // 1. Check if the employee exists.
        // This prevents creating a new record if the ID in the request body is for a non-existent employee.
        Employee existingEmployee = employeeRepository.findById(employee.getId())
                .orElseThrow(() -> {
                    LOG.warn("Service: Update failed - Employee with ID: {} not found.", employee.getId());
                    return new EmployeeNotFoundException(employee.getId());
                });

        // 2. Update the fields of the existing employee with the new data.
        // This handles cases where only specific fields are provided in the update request.
        if (employee.getName() != null && !employee.getName().isEmpty()) { // Also check for empty string
            existingEmployee.setName(employee.getName());
        }
        if (employee.getSalary() != null) {
            existingEmployee.setSalary(employee.getSalary());
        }

        // 3. Save the updated existing employee back to the database.
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        LOG.info("Service: Employee with ID: {} updated successfully.", updatedEmployee.getId());
        return updatedEmployee;
    }

    // Task: Update methods in service class (Consistent error handling & return deleted object)
    public Employee deleteEmployee(Integer id) {
        LOG.info("Service: Attempting to delete employee with ID: {}.", id);

        // 1. First, check if the employee exists before attempting to delete.
        Employee employeeToDelete = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    LOG.warn("Service: Delete failed - Employee with ID: {} not found.", id);
                    return new EmployeeNotFoundException(id);
                });

        // 2. If it exists, delete it.
        employeeRepository.deleteById(id);
        LOG.info("Service: Employee with ID: {} deleted successfully.", id);
        return employeeToDelete; // Return the deleted employee object as requested
    }

    // Task: Create functionality for findEmployeeByName
    public List<Employee> findEmployeeByName(String name) {
        LOG.info("Service: Searching for employees with name: {}.", name);
        List<Employee> employees = employeeRepository.findByName(name);
        if (employees.isEmpty()) {
            LOG.warn("Service: No employees found with name: {}.", name);
            // Task: Throw exception if no employees found by name
            throw new EmployeeNotFoundException(name);
        }
        LOG.info("Service: Found {} employee(s) with name: {}.", employees.size(), name);
        return employees;
    }
}