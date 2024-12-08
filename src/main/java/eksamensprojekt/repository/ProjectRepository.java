package eksamensprojekt.repository;

import eksamensprojekt.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Date;

@Repository
public class ProjectRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertProject(String name, String description, Date startDate, Date endDate) {
        String query = "INSERT INTO PROJECTS (name, description, startDate, endDate)" +
                    "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[] {"project_id"});
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDate(3, new java.sql.Date(startDate.getTime()));
            ps.setDate(4, new java.sql.Date(endDate.getTime()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void insertProjectEmployee(int projectId, int employeeId) {
        String query = "INSERT INTO PROJECT_EMPLOYEE (projectId, employeeId)" +
                        "VALUES(?, ?)";
        jdbcTemplate.update(query, projectId, employeeId);
    }

}
