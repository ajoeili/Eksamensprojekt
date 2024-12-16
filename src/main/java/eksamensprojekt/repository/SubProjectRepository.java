package eksamensprojekt.repository;

import eksamensprojekt.model.SubProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

@Repository
public class SubProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    // Dependency injection in constructor
    @Autowired
    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert new subproject and return the generated subproject id
    public int insertSubProject(String name, String description, Date startDate, Date endDate, int projectId) {
        String query = "INSERT INTO SUBPROJECTS (name, description, start_date, end_date, project_id) VALUES (?, ?, ?, ?, ?)";

        // Key-holder to retrieve the auto-incremented subproject id
        KeyHolder keyholder = new GeneratedKeyHolder();

        // Lambda function to create and configure prepared statement
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS); // Returns the auto-incremented project id
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDate(3, new java.sql.Date(startDate.getTime()));
            ps.setDate(4, new java.sql.Date(endDate.getTime()));
            ps.setInt(5, projectId);
            return ps;
        }, keyholder); // Key-holder holds the project id

        // Error handling in case subproject id is null
        if (keyholder.getKey() != null) {
            return keyholder.getKey().intValue(); // Return subproject id
        } else {
            return -1; // Return -1 if subproject id is not found
        }
    }

    // Get subproject details
    public SubProject getSubProjectDetails(int subProjectId) {
        String query = "SELECT * FROM SUBPROJECTS WHERE sub_project_id = ?";

        // Error handling in case project is null
        try {
            // Maps rows to a subproject object and returns object
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(SubProject.class), subProjectId);

        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if project does not exist
        }
    }

    // Get subprojects for a given project id
    public List<SubProject> getSubProjectsByProjectId(int projectId) {
        String query = "SELECT * FROM SUBPROJECTS WHERE PROJECT_ID = ?";

        // Maps the rows to subproject objects and returns the objects
        return jdbcTemplate.query(query, new Object[]{projectId}, new BeanPropertyRowMapper<>(SubProject.class));
    }

    // Get project id by subproject id
    public int getProjectIdBySubProjectId(int subProjectId) {
        String query = "SELECT PROJECT_ID FROM SUBPROJECTS WHERE SUBPROJECT_ID = ?";

        // Maps result set to an Integer value that corresponds to a project id associated with the subproject id
        Integer projectId = jdbcTemplate.queryForObject(query, new Object[]{subProjectId}, (rs, rowNum) -> {
            return rs.getInt("PROJECT_ID"); // Extract the PROJECT_ID column
        });

        // Error handling in case subproject id is not found
        if (projectId == null) {
            throw new IllegalArgumentException("Subproject ID not found: " + subProjectId);
        }
        return projectId;
    }

}


