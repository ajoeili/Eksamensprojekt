package eksamensprojekt.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ProjectRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(String name, String description, Date startDate, Date endDate) {
        String query = "INSERT INTO PROJECT (name, description, startDate, endDate)" +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, name, description, startDate, endDate);
    }

}
