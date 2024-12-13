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

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllEmployees();
    }

    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public List<Project> getProjectsForEmployee(int employeeId) {
        return employeeRepository.getProjectsForEmployee(employeeId);
    }

}
