package com.example.EmployeePayrollApp.service;

import com.example.EmployeePayrollApp.Interface.EmployeeService;
import com.example.EmployeePayrollApp.dto.EmployeeDTO;
import com.example.EmployeePayrollApp.exception.ResourceNotFoundException;
import com.example.EmployeePayrollApp.model.Employee;
import com.example.EmployeePayrollApp.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        try {
            log.info("Fetching all employees from the database");
            return employeeRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching employees: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch employees", e);
        }
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        try {
            log.info("Fetching employee with ID: {}", id);
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Employee not found with ID: {}", id);
                        return new ResourceNotFoundException("Employee not found with ID: " + id);
                    });
            return convertToDTO(employee);
        } catch (Exception e) {
            log.error("Error fetching employee: {}", e.getMessage());
            throw e;  // Let the controller handle this
        }
    }

    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        try {
            log.info("Saving new employee: {}", employeeDTO);
            Employee employee = convertToEntity(employeeDTO);
            return convertToDTO(employeeRepository.save(employee));
        } catch (Exception e) {
            log.error("Error saving employee: {}", e.getMessage());
            throw new RuntimeException("Failed to save employee", e);
        }
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        try {
            log.info("Updating employee with ID: {}", id);
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Employee not found with ID: {}", id);
                        return new ResourceNotFoundException("Employee not found with ID: " + id);
                    });

            employee.setName(employeeDTO.getName());
            employee.setDepartment(employeeDTO.getDepartment());
            employee.setSalary(employeeDTO.getSalary());

            return convertToDTO(employeeRepository.save(employee));
        } catch (Exception e) {
            log.error("Error updating employee: {}", e.getMessage());
            throw new RuntimeException("Failed to update employee", e);
        }
    }

    @Override
    public void deleteEmployee(Long id) {
        try {
            log.info("Deleting employee with ID: {}", id);
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Employee not found with ID: {}", id);
                        return new ResourceNotFoundException("Employee not found with ID: " + id);
                    });
            employeeRepository.delete(employee);
        } catch (Exception e) {
            log.error("Error deleting employee: {}", e.getMessage());
            throw new RuntimeException("Failed to delete employee", e);
        }
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return new EmployeeDTO(employee.getName(), employee.getDepartment(), employee.getSalary());
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        return new Employee(null, employeeDTO.getName(), employeeDTO.getDepartment(), employeeDTO.getSalary());
    }
}

