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

    @Autowired
    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert project and return the generated project ID
    public int insertProject(String name, String description, Date startDate, Date endDate) {
        String sql = "INSERT INTO PROJECTS (name, description, start_date, end_date) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDate(3, new java.sql.Date(startDate.getTime()));
            ps.setDate(4, new java.sql.Date(endDate.getTime()));
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().intValue();
        } else {
            return -1; // TODO: Check if there is a better way to handle this
        }
    }

    // Insert into PROJECT_EMPLOYEE
    public void insertProjectEmployee(int projectId, int employeeId) {
        String sql = "INSERT INTO PROJECT_EMPLOYEE (project_id, employee_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, projectId, employeeId);
    }

    // Select project details
    public Project getProjectDetails(int projectId) {
        String query = "SELECT * FROM PROJECTS WHERE project_id = ?";
        System.out.println("Executing query for project ID: " + projectId); // Log the ID being passed
        try {
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Project.class), projectId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No project found for ID: " + projectId); // Log if project is not found
            return null;
        }
    }


}
