package eksamensprojekt.service;

import eksamensprojekt.model.SubProject;
import eksamensprojekt.repository.SubProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubProjectServiceUnitTest {

    // Mocking the SubProjectRepository
    @Mock
    private SubProjectRepository subProjectRepository;

    // Injecting the mocked SubProjectRepository into the SubProjectService
    @InjectMocks
    private SubProjectService subProjectService;

    // This method sets up the test environment before each test method is executed
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes mock objects
    }

    // Test ensures that when a subproject is created successfully, the returned subproject id is correct, and the appropriate methods in the repository are called
    @Test
    public void testCreateSubProject_Success() {
        // Create a new subproject and define the expected subproject id and project id
        int subprojectId = 101;  // The subproject id we're expecting to return
        SubProject subProject = new SubProject(subprojectId, "SubProject 1", "Description of SubProject", new Date(), new Date());
        int projectId = 1;

        // Mock the repository's insertSubProject method to return the subprojectId
        when(subProjectRepository.insertSubProject(
                subProject.getName(),
                subProject.getDescription(),
                subProject.getStartDate(),
                subProject.getEndDate(),
                projectId
        )).thenReturn(subprojectId);

        // Call the createSubProject method on the service
        int result = subProjectService.createSubProject(subProject, projectId);

        // Verify the subproject id returned is correct
        assertEquals(subprojectId, result);

        // Verify that insertSubProject was called once with the correct parameters
        verify(subProjectRepository, times(1)).insertSubProject(
                subProject.getName(),
                subProject.getDescription(),
                subProject.getStartDate(),
                subProject.getEndDate(),
                projectId
        );
    }

    // Test ensures that when the subproject creation fails, the service method returns the failure code (-1), and the appropriate actions are taken
    @Test
    public void testCreateSubProject_Failure() {
        // Create a new subproject and define the expected subproject id and project id
        int subprojectId = 102; // Expected subproject id
        SubProject subProject = new SubProject(subprojectId, "SubProject 2", "Description of SubProject", new Date(), new Date());
        int projectId = 1;

        // Mock insertSubProject to return -1, indicating failure
        when(subProjectRepository.insertSubProject(
                subProject.getName(),
                subProject.getDescription(),
                subProject.getStartDate(),
                subProject.getEndDate(),
                projectId
        )).thenReturn(-1);  // Simulate failure

        // Call the createSubProject method, expecting it to return -1
        int result = subProjectService.createSubProject(subProject, projectId);

        // Verify the result is -1
        assertEquals(-1, result);

        // Verify that insertSubProject was called once with the correct parameters
        verify(subProjectRepository, times(1)).insertSubProject(
                subProject.getName(),
                subProject.getDescription(),
                subProject.getStartDate(),
                subProject.getEndDate(),
                projectId
        );
    }
}
