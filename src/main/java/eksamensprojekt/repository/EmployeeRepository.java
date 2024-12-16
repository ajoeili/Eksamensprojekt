package eksamensprojekt.repository;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    // Dependency injection in constructor
    @Autowired
    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Get all employees from the employees table
    public List<Employee> getAllEmployees() {
        String query = "SELECT * FROM EMPLOYEES";

        // Maps rows to properties of employee objects and returns the objects
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Employee.class));
    }

    // Find employee by their email
    public Employee findByEmail(String email) {
        String query = "SELECT * FROM EMPLOYEES WHERE email = ?";

        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        try {
            // Maps rows to properties of employee object and returns object
            return jdbcTemplate.queryForObject(query, rowMapper, email);

        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no employee is found
        }
    }

    // Get all projects an employee is assigned to
    public List<Project> getProjectsForEmployee(int employeeId) {
        String query = "SELECT * FROM PROJECTS WHERE project_id IN (SELECT project_id FROM PROJECT_EMPLOYEE WHERE employee_id = ?)";

        // Maps rows to properties of project objects and returns the objects
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Project.class), employeeId);

    }

    // Get employee by id
    public Employee getEmployeeById(int employeeId) {
        String query = "SELECT * FROM EMPLOYEES WHERE employee_id = ?";
        try {
            // Maps rows to employee object and returns object
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Employee.class), employeeId);

        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no employee is found
        }
    }

    // Get all employees assigned to specific project
    public List<Employee> getEmployeesForProject(int projectId) {
        String query = "SELECT * FROM EMPLOYEES WHERE project_id IN (SELECT employee_id FROM PROJECT_EMPLOYEE WHERE project_id = ?)";

        // Maps rows to employee objects and returns objects
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Employee.class), projectId);
    }

}
