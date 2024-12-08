package eksamensprojekt.service;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class ProjectServiceUnitTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createProject_success() {
        // Arrange
        Project project = new Project(10, "New Project", "Description of the project", new Date(), new Date());
        Employee employee = new Employee(10, "Jens", "Hansen", "jeha85@email.com", "password10", "Tester", true);
        project.setEmployees(List.of(employee));

        // Act & Assert
        projectService.createProject(project);

        // Verify that the insertProject method was called once
        verify(projectRepository, times(1)).insertProject(project.getName(), project.getDescription(), project.getStartDate(), project.getEndDate());
        // Verify that the insertProjectEmployee was called once
        verify(projectRepository, times(1)).insertProjectEmployee(anyInt(), eq(employee.getEmployeeId()));
    }

    @Test
    public void createProject_emptyName() {
        // Arrange
        Project project = new Project(10, "", "Description of the project", new Date(), new Date());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project);
        });
        assertEquals("Project name cannot be empty", exception.getMessage());
    }

    @Test
    public void createProject_emptyDescription() {
        // Arrange
        Project project = new Project(10, "New Project", "", new Date(), new Date());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project);
        });
        assertEquals("Project description cannot be empty", exception.getMessage());
    }

    @Test
    public void createProject_nullStartDate() {
        // Arrange
        Project project = new Project(10, "New Project", "Description of the project", null, new Date());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project);
        });
        assertEquals("Project start date cannot be empty", exception.getMessage());
    }

    @Test
    public void createProject_nullEndDate() {
        // Arrange
        Project project = new Project(10, "New Project", "Description of the project", new Date(), null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project);
        });
        assertEquals("Project end date cannot be empty", exception.getMessage());
    }

    @Test
    public void createProject_endDateBeforeStartDate() {
        // Arrange
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() - 1000);  // End date is 1 second before the start date
        Project project = new Project(10, "New Project", "Description of the project", startDate, endDate);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project);
        });
        assertEquals("End date cannot be before start date", exception.getMessage());
    }

    @Test
    public void createProject_invalidEmployeeId() {
        // Arrange
        Employee employee = new Employee(0, "Jens", "Hansen", "jeha85@email.com", "password10", "Tester", true); // Invalid ID (<= 0)
        Project project = new Project(10, "New Project", "Description of the project", new Date(), new Date());
        project.setEmployees(List.of(employee));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project);
        });
        assertEquals("Invalid employee ID: 0", exception.getMessage());
    }


}
