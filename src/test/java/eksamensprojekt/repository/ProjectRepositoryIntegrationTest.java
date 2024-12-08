package eksamensprojekt.repository;

import eksamensprojekt.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProjectRepositoryIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private int projectId;

    @BeforeEach
    public void setUp() {
        String name = "Test Project";
        String description = "Test Description";
        Date startDate = new Date();
        Date endDate = new Date();
        projectId = projectRepository.insertProject(name, description, startDate, endDate);
    }

    @Test
    public void testInsertProject() {
        assertTrue(projectId > 0, "Project ID should be greater than 0");
    }

    @Test
    public void testInsertProjectEmployee() {
        // Arrange
        int employeeId = 1;

        // Act
        projectRepository.insertProjectEmployee(projectId, employeeId);

        // Assert
        String query = "SELECT COUNT(*) FROM PROJECT_EMPLOYEE WHERE project_id = ? AND employee_id = ?";
        Integer count = jdbcTemplate.update(query, Integer.class, projectId, employeeId);

        assertNotNull(count, "Count should not be null");
        assertTrue(count > 0, "There should be a project-employee association in the database");
    }
}
