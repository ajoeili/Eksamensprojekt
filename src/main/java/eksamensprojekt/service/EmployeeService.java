package eksamensprojekt.service;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Dependency injection in constructor
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    // Find employee by their email
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    // Get all projects for an employee
    public List<Project> getProjectsForEmployee(int employeeId) {
        return employeeRepository.getProjectsForEmployee(employeeId);
    }

}
