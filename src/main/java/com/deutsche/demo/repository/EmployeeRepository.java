package com.deutsche.demo.repository;

import com.deutsche.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Import List if not already there

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // Task: Create functionality for findEmployeeByName
    // Spring Data JPA automatically provides implementation for this method name
    List<Employee> findByName(String name);
}