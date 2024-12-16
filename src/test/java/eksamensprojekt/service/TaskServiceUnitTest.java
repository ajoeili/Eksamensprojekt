package eksamensprojekt.service;

import eksamensprojekt.model.SubProject;
import eksamensprojekt.model.Task;
import eksamensprojekt.repository.SubProjectRepository;
import eksamensprojekt.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceUnitTest {

    // Mocking the repositories
    @Mock
    private SubProjectRepository subProjectRepository;

    @Mock
    private TaskRepository taskRepository;

    // Injecting the mocked repositories into the TaskService for testing
    @InjectMocks
    private TaskService taskService;

    // This method is run before each test method to set up the necessary test environment
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes mock objects
    }

    // Test ensures that when the task creation is successful, the returned result is true, and the repository's method is called once with correct parameters
    @Test
    public void testCreateTask_Success() {
        // Create a new task with specific details
        Task task = new Task(1, "Task 1", "Description of Task 1", 1, 10, new Date());
        int employeeId = 2;
        int subProjectId = 3;

        // Mock the repository's insertTask method to return true, indicating successful task creation
        when(taskRepository.insertTask(
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                task.getEstimatedHours(),
                task.getDeadline(),
                employeeId,
                subProjectId
        )).thenReturn(true);

        // Call the service method to create a task
        boolean result = taskService.createTask(task, employeeId, subProjectId);

        // Verify the result is true (task creation was successful)
        assertTrue(result);

        // Verify that insertTask was called once with the correct parameters
        verify(taskRepository, times(1)).insertTask(
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                task.getEstimatedHours(),
                task.getDeadline(),
                employeeId,
                subProjectId
        );
    }

    // Test ensures that when the repository method fails (returns false), the service method returns false and handles the failure properly
    @Test
    public void testCreateTask_Failure() {
        // Create a new task
        Task task = new Task(1, "Task 1", "Description of Task 1", 1, 10, new Date());
        int employeeId = 2;
        int subProjectId = 3;

        // Mock insertTask to return false, indicating a failure
        when(taskRepository.insertTask(
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                task.getEstimatedHours(),
                task.getDeadline(),
                employeeId,
                subProjectId
        )).thenReturn(false);

        // Call the service method to create a task
        boolean result = taskService.createTask(task, employeeId, subProjectId);

        // Verify the result is false
        assertFalse(result);

        // Verify that insertTask was called once
        verify(taskRepository, times(1)).insertTask(
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                task.getEstimatedHours(),
                task.getDeadline(),
                employeeId,
                subProjectId
        );
    }

    // Test verifies that the method correctly sums the estimated hours of tasks within subprojects
    @Test
    public void testCalculateTotalEstimatedHoursForProject() {
        // Create subprojects and tasks with estimated hours
        SubProject subProject1 = new SubProject(1, "SubProject 1", "Test description", new Date(), new Date());
        SubProject subProject2 = new SubProject(2, "SubProject 2", "Test description", new Date(), new Date());

        List<SubProject> subProjects = Arrays.asList(subProject1, subProject2);
        when(subProjectRepository.getSubProjectsByProjectId(1)).thenReturn(subProjects);

        Task task1 = new Task(1, "Task 1", "Description 1", 1, 5, null);
        Task task2 = new Task(2, "Task 2", "Description 2", 1, 3, null);
        Task task3 = new Task(3, "Task 3", "Description 3", 2, 4, null);
        Task task4 = new Task(4, "Task 4", "Description 4", 2, 6, null);

        when(taskRepository.getTasksBySubProjectId(1)).thenReturn(Arrays.asList(task1, task2));
        when(taskRepository.getTasksBySubProjectId(2)).thenReturn(Arrays.asList(task3, task4));

        // Calculate total estimated hours for the project
        int totalEstimatedHours = taskService.calculateTotalEstimatedHoursForProject(1);

        // Verify that the total estimated hours equals 18 (5 + 3 + 4 + 6)
        assertEquals(18, totalEstimatedHours);
    }

    // Test ensures that the method correctly sums the estimated hours of tasks within a specific subproject
    @Test
    public void testCalculateTotalEstimatedHoursForSubProject() {
        // Create tasks and mock repository response for subproject 1
        Task task1 = new Task(1, "Task 1", "Description 1", 1, 5, null);
        Task task2 = new Task(2, "Task 2", "Description 2", 1, 3, null);

        when(taskRepository.getTasksBySubProjectId(1)).thenReturn(Arrays.asList(task1, task2));

        // Calculate total estimated hours for subproject 1
        int totalEstimatedHours = taskService.calculateTotalEstimatedHoursForSubProject(1);

        // Verify that the total estimated hours equals 8 (5 + 3)
        assertEquals(8, totalEstimatedHours);
    }

    //Test ensures that the correct number of workdays is calculated based on the total estimated hours
    @Test
    public void testCalculateWorkdays() {
        // Set the total estimated hours
        int totalEstimatedHours = 18;

        // Calculate the workdays required for 18 hours
        int workdays = taskService.calculateWorkdays(totalEstimatedHours);

        // Verify that the number of workdays is calculated correctly (18 hours / 7.5 hours per day = 3 workdays)
        assertEquals(3, workdays);
    }

    // Test ensures that when a task is successfully marked as completed, the repository method is called with the correct parameters and the result is true
    @Test
    public void testMarkTaskAsCompleted_Success() {
        // Set the task id
        int taskId = 1;

        // Mock the repository's updateTaskCompletion method to return true (task marked as completed)
        when(taskRepository.updateTaskCompletion(taskId, true)).thenReturn(true);

        // Call the method to mark the task as completed
        boolean result = taskService.markTaskAsCompleted(taskId);

        // Verify that the task was successfully marked as completed
        assertTrue(result);

        // Verify that updateTaskCompletion was called once with the correct parameters
        verify(taskRepository, times(1)).updateTaskCompletion(taskId, true);
    }

    // Test ensures that if the task marking fails, the service handles it appropriately
    @Test
    public void testMarkTaskAsCompleted_Failure() {
        // Set the task id
        int taskId = 1;

        // Mock the repository's updateTaskCompletion method to return false (task marking failed)
        when(taskRepository.updateTaskCompletion(taskId, true)).thenReturn(false);

        // Call the method to mark the task as completed
        boolean result = taskService.markTaskAsCompleted(taskId);

        // Verify that the task was not marked as completed
        assertFalse(result);

        // Verify that updateTaskCompletion was called once
        verify(taskRepository, times(1)).updateTaskCompletion(taskId, true);
    }
}
