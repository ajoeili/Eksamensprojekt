package eksamensprojekt.repository;

import eksamensprojekt.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    // Dependency injection in constructor
    @Autowired
    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert project and return the generated project id
    public int insertProject(String name, String description, Date startDate, Date endDate) {
        String sql = "INSERT INTO PROJECTS (name, description, start_date, end_date) VALUES (?, ?, ?, ?)";

        // Key-holder to retrieve the auto-incremented project id
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Lambda function to create and configure prepared statement
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Returns the auto-incremented project id
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDate(3, new java.sql.Date(startDate.getTime()));
            ps.setDate(4, new java.sql.Date(endDate.getTime()));
            return ps;
        }, keyHolder); // Key-holder holds the project id

        // Error handling in case project id is null
        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().intValue(); // Return project id
        } else {
            return -1; // Return -1 if project id is not found
        }
    }

    // Insert into PROJECT_EMPLOYEE
    public void insertProjectEmployee(int projectId, int employeeId) {
        String sql = "INSERT INTO PROJECT_EMPLOYEE (project_id, employee_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, projectId, employeeId);
    }

    // Get project details
    public Project getProjectDetails(int projectId) {
        String query = "SELECT * FROM PROJECTS WHERE project_id = ?";

        // Error handling in case project is null
        try {
            // Maps rows to a project object and returns object
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Project.class), projectId);

        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if project does not exist
        }
    }


}
