package eksamensprojekt.service;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectServiceUnitTest {

    // Mocking the ProjectRepository
    @Mock
    private ProjectRepository projectRepository;

    // Injecting the mocked ProjectRepository into the ProjectService
    @InjectMocks
    private ProjectService projectService;

    // This method sets up the test environment before each test method is executed
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes mock objects
    }

    // Test ensures that when a project is created successfully, the returned project id is correct
    @Test
    public void testCreateProject_Success() {
        // Create a new project and a list of employee id's
        Project project = new Project(10, "New Project", "Description of the project", new Date(), new Date());
        List<Integer> employeeIds = List.of(1, 2, 3);
        int projectId = 100;
        project.setProjectId(projectId);  // Set the expected project id

        // Mock the repository method insertProject to return the expected project id
        when(projectRepository.insertProject(
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate()
        )).thenReturn(projectId);

        // Call the createProject method on the service
        int result = projectService.createProject(project, employeeIds);

        // Verify the project id returned is correct
        assertEquals(projectId, result);

        // Verify that insertProject was called exactly once with the correct parameters
        verify(projectRepository, times(1)).insertProject(
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate()
        );

        // Verify that insertProjectEmployee was called for each employee id
        verify(projectRepository, times(1)).insertProjectEmployee(projectId, 1);
        verify(projectRepository, times(1)).insertProjectEmployee(projectId, 2);
        verify(projectRepository, times(1)).insertProjectEmployee(projectId, 3);
    }

    // Test ensures that when the project creation fails repository returns -1
    @Test
    public void testCreateProject_FailedInsert() {
        // Create a new project and a list of employee id's
        Project project = new Project(11, "New Project2", "Description of the project", new Date(), new Date());
        List<Integer> employeeIds = List.of(1, 2, 3);

        // Mock insertProject to return -1, indicating failure
        when(projectRepository.insertProject(
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate()
        )).thenReturn(-1);

        // Call the createProject method, expecting it to return -1
        int result = projectService.createProject(project, employeeIds);

        // Verify the result is -1
        assertEquals(-1, result);

        // Verify that insertProject was called exactly once with the correct parameters
        verify(projectRepository, times(1)).insertProject(
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate()
        );

        // Verify that insertProjectEmployee was NOT called (since project insertion failed)
        verify(projectRepository, times(0)).insertProjectEmployee(0, 0);  // No employee-project relationship should be created
    }

    // Test ensures that the service throws an IllegalArgumentException with the appropriate error message when the project name is empty
    @Test
    public void testCreateProject_EmptyName() {
        // Create a project with an empty name
        Project project = new Project(12, "", "Description of the project", new Date(), new Date());
        List<Integer> employeeIds = List.of(1);

        // Verify that an IllegalArgumentException is thrown with the correct message
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project, employeeIds);
        });
        assertEquals("Project name is required.", exception.getMessage());
    }

    // Test ensures that the service throws an IllegalArgumentException with the appropriate error message when the project description is empty
    @Test
    public void testCreateProject_EmptyDescription() {
        // Create a project with an empty description
        Project project = new Project(13, "New Project3", "", new Date(), new Date());
        List<Integer> employeeIds = List.of(1);

        // Verify that an IllegalArgumentException is thrown with the correct message
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project, employeeIds);
        });
        assertEquals("Project description is required.", exception.getMessage());
    }

    //This test ensures that the service throws an IllegalArgumentException with the appropriate error message when the project start date is null
    @Test
    public void testCreateProject_NullStartDate() {
        // Create a project with a null start date
        Project project = new Project(14, "New Project4", "Description of the project", null, new Date());
        List<Integer> employeeIds = List.of(1);

        // Verify that an IllegalArgumentException is thrown with the correct message
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project, employeeIds);
        });
        assertEquals("Project start date is required.", exception.getMessage());
    }

    // Test ensures that the service throws an IllegalArgumentException with the appropriate error message when the project end date is null
    @Test
    public void testCreateProject_NullEndDate() {
        // Create a project with a null end date
        Project project = new Project(15, "New Project5", "Description of the project", new Date(), null);
        List<Integer> employeeIds = List.of(1);

        // Verify that an IllegalArgumentException is thrown with the correct message
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project, employeeIds);
        });
        assertEquals("Project end date is required.", exception.getMessage());
    }

    // Test ensures that the service throws an IllegalArgumentException with the appropriate error message when the project end date is earlier than the start date
    @Test
    public void testCreateProject_EndDateBeforeStartDate() {
        // Create a project with the end date before the start date
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() - 1000);  // End date is 1 second before the start date
        Project project = new Project(16, "New Project6", "Description of the project", startDate, endDate);
        List<Integer> employeeIds = List.of(1);

        // Verify that an IllegalArgumentException is thrown with the correct message
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project, employeeIds);
        });
        assertEquals("Project end date cannot be before project start date.", exception.getMessage());
    }

    // Test ensures that the service throws an IllegalArgumentException with the appropriate error message when an invalid employee id is provided
    @Test
    public void testCreateProject_InvalidEmployeeId() {
        // Create a project with an invalid employee id
        Project project = new Project(17, "New Project7", "Description of the project", new Date(), new Date());
        List<Integer> employeeIds = List.of(0); // Invalid employee ID

        // Verify that an IllegalArgumentException is thrown with the correct message
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(project, employeeIds);
        });
        assertEquals("Invalid employee ID: 0", exception.getMessage());
    }
}
