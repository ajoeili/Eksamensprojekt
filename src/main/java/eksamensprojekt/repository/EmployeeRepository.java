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

    @Autowired
    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> getAllEmployees() {
        String query = "SELECT * FROM EMPLOYEES";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Employee.class));
    }

    public Employee findByEmail(String email) {
        String query = "SELECT * FROM EMPLOYEES WHERE email = ?";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        try {
            return jdbcTemplate.queryForObject(query, rowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no employee is found
        }
    }

    public List<Project> getProjectsForEmployee(int employeeId) {
        String query = "SELECT * FROM PROJECTS WHERE project_id IN (SELECT project_id FROM PROJECT_EMPLOYEE WHERE employee_id = ?)";
        RowMapper<Project> rowMapper = new BeanPropertyRowMapper<>(Project.class);

        // Add logging to track the employeeId+
        System.out.println("DEBUG: Querying for employeeId: " + employeeId);

        // Log the query being executed
        System.out.println("DEBUG: Executing query: " + query);

        // Execute the query and return the result
        List<Project> projects = jdbcTemplate.query(query, rowMapper, employeeId);

        // Log the result size and project details
        System.out.println("DEBUG: Fetched " + projects.size() + " projects for employeeId: " + employeeId);
        for (Project project : projects) {
            System.out.println("DEBUG: Project ID: " + project.getProjectId() + ", Name: " + project.getName());
        }

        return projects;
    }

    // Get employee by ID
    public Employee getEmployeeById(int employeeId) {
        String query = "SELECT * FROM EMPLOYEES WHERE employee_id = ?";
        try {
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Employee.class), employeeId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
