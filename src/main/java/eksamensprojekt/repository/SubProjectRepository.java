package eksamensprojekt.repository;

import eksamensprojekt.model.Project;
import eksamensprojekt.model.SubProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SubProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert new subproject into the database
    public int insertSubProject(int projectId, SubProject subProject) {
        String query = "INSERT INTO SUBPROJECTS (project_id, name, description, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(query, projectId, subProject.getName(),
                subProject.getDescription(), subProject.getStartDate(), subProject.getEndDate());
    }

    // Select subproject details
    public SubProject getSubProjectDetails(int subProjectId) {
        String query = "SELECT * FROM SUBPROJECTS WHERE sub_project_id = ?";
        System.out.println("Executing query for subproject ID: " + subProjectId); // Log the ID being passed;
        try {
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(SubProject.class), subProjectId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No subproject found for subproject ID: " + subProjectId);
            return null;
        }
    }
}

