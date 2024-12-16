package eksamensprojekt.service;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.SubProject;
import eksamensprojekt.model.Task;
import eksamensprojekt.repository.SubProjectRepository;
import eksamensprojekt.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubProjectRepository subProjectRepository;

    // Dependency injection in constructor
    @Autowired
    public TaskService(TaskRepository taskRepository, SubProjectRepository subProjectRepository) {
        this.taskRepository = taskRepository;
        this.subProjectRepository = subProjectRepository;
    }

    // Create new task
    public boolean createTask(Task task, int employeeId, int subprojectId) {

        // Service level validation ensures the data integrity of the task object
        if (task.getName() == null || task.getName().isEmpty()) {
            throw new IllegalArgumentException("Task name is required.");
        }
        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Task description is required.");
        }
        if (task.getEstimatedHours() <= 0) {
            throw new IllegalArgumentException("Estimated hours must be a positive number.");
        }
        if (task.getDeadline() == null) {
            throw new IllegalArgumentException("Task deadline is required.");
        }
        if (task.getPriority() <= 0) {
            throw new IllegalArgumentException("Priority cannot be negative.");
        }
        if (employeeId <= 0) {
            throw new IllegalArgumentException("Invalid employee ID.");
        }
        if (subprojectId <= 0) {
            throw new IllegalArgumentException("Invalid subproject ID.");
        }

        // Insert the task into the database
        return taskRepository.insertTask(
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                task.getEstimatedHours(),
                task.getDeadline(),
                employeeId,
                subprojectId
        );
    }

    // Get employee assigned for specific task
    public Employee getEmployeeForTask(int taskId) {
        return taskRepository.getEmployeeForTask(taskId);
    }

    // Get task by id
    public Task getTaskById(int taskId) {
        return taskRepository.getTaskById(taskId);
    }

    // Calculate total estimated hours for specific project
    public int calculateTotalEstimatedHoursForProject(int projectId) {

        // Fetch subprojects for project id
        List<SubProject> subProjects = subProjectRepository.getSubProjectsByProjectId(projectId);

        int totalEstimatedHours = 0;

        // Calculate hours based on total hours for subprojects
        for (SubProject subProject : subProjects) {
            totalEstimatedHours += calculateTotalEstimatedHoursForSubProject(subProject.getSubprojectId());
        }
        return totalEstimatedHours;
    }

    // Calculate total estimated hours for subproject
    public int calculateTotalEstimatedHoursForSubProject(int subprojectId) {

        // Fetch tasks for subproject id
        List<Task> tasks = taskRepository.getTasksBySubProjectId(subprojectId);

        int totalEstimatedHours = 0;

        // Calculate hours based on total hours for tasks
        for (Task task : tasks) {
            if (!task.getIsCompleted()) {
                totalEstimatedHours += task.getEstimatedHours();
            }
        }
        return totalEstimatedHours;
    }

    // Calculate total amount of workdays
    public int calculateWorkdays(int totalEstimatedHours) {
        double workdayHours = (8 - 0.5); // 8-hour work day minus half an hour for lunch

        return (int) Math.ceil(totalEstimatedHours / workdayHours); // Return amount of workdays rounded up to nearest number
    }

    // Mark task as completed
    public boolean markTaskAsCompleted(int taskId) {
        return taskRepository.updateTaskCompletion(taskId, true);
    }

    // Update task completion status
    public boolean updateTaskCompletionStatus(int taskId, boolean isCompleted) {

        //Error handling if task id is invalid
        if (taskId <= 0) {
            throw new IllegalArgumentException("Invalid Task ID.");
        }

        return taskRepository.updateTaskCompletion(taskId, isCompleted);
    }


}
