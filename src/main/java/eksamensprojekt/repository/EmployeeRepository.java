package eksamensprojekt.repository;

import eksamensprojekt.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findAllEmployees() {
        String query = "SELECT employee_id, first_name, last_name, email, password, role, is_project_manager FROM EMPLOYEES";
        return jdbcTemplate.query(query, (rs, rowNum) -> {

            Employee employee = new Employee();

            employee.setEmployeeId(rs.getInt("employee_id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));
            employee.setEmail(rs.getString("email"));
            employee.setPassword(rs.getString("password"));
            employee.setRole(rs.getString("role"));
            employee.setProjectManager(rs.getBoolean("is_project_manager"));

            return employee;
        });
    }

}
