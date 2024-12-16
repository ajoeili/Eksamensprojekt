package eksamensprojekt.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev") // Specifies the 'dev' profile for this test, ensuring that the application context loads with the dev-specific configurations
public class ProjectRepositoryIntegrationTest {

    // Autowiring the ProjectRepository to interact with the Project data
    @Autowired
    private ProjectRepository projectRepository;

    // Autowiring JdbcTemplate to directly execute SQL queries on the database
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // The project id used across multiple tests
    private int projectId;

    // This method sets up the test environment before each test method is executed
    @BeforeEach
    public void setUp() {
        // Create test project data
        String name = "Test Project";
        String description = "Test Description";
        Date startDate = new Date();  // Current date as the start date
        Date endDate = new Date();    // Current date as the end date (for simplicity in this example)

        // Insert the project into the database and retrieve its generated id
        projectId = projectRepository.insertProject(name, description, startDate, endDate);
    }

    // Test verifies that the project insertion works and the project id is properly generated
    @Test
    public void testInsertProject() {
        // Verify that the project id is greater than 0, indicating successful insertion
        assertTrue(projectId > 0, "Project ID should be greater than 0");
    }

    // Test verifies that an employee can be successfully associated with a project
    @Test
    public void testInsertProjectEmployee() {
        // Set up an employee id to associate with the project
        int employeeId = 1;  // Assuming employee with id 1 exists in the database

        // Insert the employee-project relationship into the PROJECT_EMPLOYEE table
        projectRepository.insertProjectEmployee(projectId, employeeId);

        // Verify the association was successful by querying the PROJECT_EMPLOYEE table
        String query = "SELECT COUNT(*) FROM PROJECT_EMPLOYEE WHERE project_id = ? AND employee_id = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, projectId, employeeId);

        // Verify that the count is not null and the relationship was successfully created
        assertNotNull(count, "Count should not be null");
        assertTrue(count > 0, "There should be at least one entry in the PROJECT_EMPLOYEE table for this project-employee relationship");
    }
}
