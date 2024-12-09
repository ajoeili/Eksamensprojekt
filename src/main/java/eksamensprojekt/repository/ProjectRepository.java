package eksamensprojekt.repository;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
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
        String sql = "INSERT INTO PROJECTS (NAME, DESCRIPTION, START_DATE, END_DATE) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDate(3, new java.sql.Date(startDate.getTime()));
            ps.setDate(4, new java.sql.Date(endDate.getTime()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // Insert into PROJECT_EMPLOYEE
    public void insertProjectEmployee(int projectId, int employeeId) {
        String sql = "INSERT INTO PROJECT_EMPLOYEE (PROJECT_ID, EMPLOYEE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, projectId, employeeId);
    }
}
