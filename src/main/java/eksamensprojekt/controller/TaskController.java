package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.SubProject;
import eksamensprojekt.model.Task;
import eksamensprojekt.service.EmployeeService;
import eksamensprojekt.service.SubProjectService;
import eksamensprojekt.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final SubProjectService subProjectService;
    private final EmployeeService employeeService;

    // Dependency injection in constructor
    @Autowired
    public TaskController(TaskService taskService, SubProjectService subProjectService, EmployeeService employeeService) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
        this.employeeService = employeeService;
    }

    // Show create task form
    @GetMapping("/create/{subprojectId}")
    public String showCreateTaskForm(@PathVariable("subprojectId") int subprojectId,
                                     Model model, HttpSession session) {

        // Check if user is logged in and is project manager
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }

        // Error handling in case subproject id is invalid
        if (subprojectId <= 0) {
            model.addAttribute("errorMessage", "Invalid subproject ID.");
            return "error-view";
        }

        // Prepare the Task model for form binding and pass task, subproject id and employees to the model
        Task task = new Task();
        model.addAttribute("task", task);
        model.addAttribute("subProjectId", subprojectId);
        model.addAttribute("employees", employeeService.getAllEmployees());

        // Return the view for the task creation form
        return "create-task-form";
    }

    // Handle create task form submission
    @PostMapping("/create/{subprojectId}")
    public String createTask(@ModelAttribute("task") Task task,
                             @RequestParam("employeeId") int employeeId,
                             @PathVariable("subprojectId") int subprojectId,
                             Model model, HttpSession session) {

        // Check if user is logged in and is project manager
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }

        // Input validation for every field
        if (task.getName() == null || task.getName().isEmpty()) {
            model.addAttribute("errorMessage", "Name is required.");
            return "create-task-form";
        }

        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            model.addAttribute("errorMessage", "Description is required.");
            return "create-task-form";
        }

        if (task.getPriority() <= 0) {
            model.addAttribute("errorMessage", "Priority is required and must be above 0.");
            return "create-task-form";
        }

        if (task.getDeadline() == null) {
            model.addAttribute("errorMessage", "Deadline is required.");
            return "create-task-form";
        }

        if (task.getEstimatedHours() <= 0) {
            model.addAttribute("errorMessage", "Estimated hours are required and must be above 0.");
            return "create-task-form";
        }

        if (employeeId <= 0) {
            model.addAttribute("errorMessage", "Invalid employee selected.");
            return "create-task-form";
        }

        // Error handling in case creation fails or illegal argument is provided
        try {
            boolean success = taskService.createTask(task, employeeId, subprojectId);

            if (!success) {
                model.addAttribute("errorMessage", "Error: Could not create task. Please try again.");
                return "create-task-form";
            }

            // Get project id to redirect back to project details view
            int projectId = subProjectService.getProjectIdBySubProjectId(subprojectId);

            // Return user to project details view
            return "redirect:/projects/" + projectId;

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());

            // Return error view to display error message
            return "error-view";
        }
    }

    // Get all task details
    @GetMapping("/projects/{id}")
    public String getTaskDetails(@PathVariable int id, Model model, HttpSession session) {

        // Check if user is logged in
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/calculation-tool/login";
        }

        // Fetch task by task id
        Task task = taskService.getTaskById(id);

        // Error handling in case task does not exist
        if (task == null) {
            model.addAttribute("errorMessage", "Task not found.");
            return "error-view";
        }

        // Fetch employee details associated with the task
        Employee employee = taskService.getEmployeeForTask(id);

        // Error handling in case employee does not exist
        if (employee == null) {
            model.addAttribute("errorMessage", "Employee not found.");
            return "error-view";
        }

        // Add the task and employee to the model
        model.addAttribute("task", task);
        model.addAttribute("employee", employee);

        // Return the view for the task details page
        return "project-details-view";
    }


    // Handle updating task completion
    @PostMapping("/projects/{projectId}/{taskId}/completion")
    public String updateTaskCompletion(@PathVariable("taskId") int taskId,
                                       @PathVariable("projectId") int projectId,
                                       @RequestParam("isCompleted") boolean isCompleted,
                                       Model model, HttpSession session) {

        // Check user is logged in and is project manager
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }

        try {
            // Call service to update task completion status
            boolean success = taskService.updateTaskCompletionStatus(taskId, isCompleted);

            // Error handling in case completion update fails
            if (!success) {
                model.addAttribute("errorMessage", "Error: Could not update task completion status. Please try again.");
                return "error-view";
            }

            // Fetch subprojects with tasks for the project
            List<SubProject> subProjectsWithTasks = subProjectService.getSubProjectsWithTasksByProjectId(projectId);

            // Add the subprojects and tasks to the model
            model.addAttribute("subProjects", subProjectsWithTasks);

            // Redirect back to the project page with updated subprojects and tasks
            return "redirect:/projects/" + projectId;

            // Error handling in case of illegal argument
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());

            // Return error view to display error message
            return "error-view";
        }
    }

    // Helper method to check login status and role
    private String checkLoggedInAndPermission(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {

            // Return to login page if user is not logged in
            return "redirect:/calculation-tool/login";
        }
        if (!loggedInEmployee.getIsProjectManager()) {
            model.addAttribute("errorMessage", "You do not have permission to create subprojects.");

            // Return error view if user is not project manager
            return "error-view";
        }
        return null;
    }





}
