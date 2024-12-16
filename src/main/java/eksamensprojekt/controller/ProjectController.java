package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.model.SubProject;
import eksamensprojekt.service.EmployeeService;
import eksamensprojekt.service.ProjectService;
import eksamensprojekt.service.SubProjectService;
import eksamensprojekt.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;
    private final SubProjectService subProjectService;
    private final TaskService taskService;

    // Dependency injection in the constructor
    @Autowired
    public ProjectController(ProjectService projectService, EmployeeService employeeService,
                             SubProjectService subProjectService, TaskService taskService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.subProjectService = subProjectService;
        this.taskService = taskService;
    }

    // Show create project form
    @GetMapping("/create")
    public String showCreateProjectForm(Model model, HttpSession session) {

        // Check if employee is logged in and if they are project manager
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }

        // Add project and employees to the model
        model.addAttribute("project", new Project());
        model.addAttribute("employees", employeeService.getAllEmployees());

        // Returns the view for the create-project form
        return "create-project-form";
    }

    // Handle create project form submission
    @PostMapping("/create")
    public String createProject(@ModelAttribute("project") Project project,
                                @RequestParam ("employees") List<Integer> employeeIds,
                                Model model, HttpSession session) {

        // Check if the employee is logged in and if they are project manager
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }

        // Input validation for every field
        if (project.getName() == null || project.getName().isEmpty()) {
            model.addAttribute("errorMessage", "Project name is required.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project-form";
        }

        if (project.getDescription() == null || project.getDescription().isEmpty()) {
            model.addAttribute("errorMessage", "Project description is required.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project-form";
        }

        if (project.getStartDate() == null) {
            model.addAttribute("errorMessage", "Project start date is required.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project-form";
        }

        if (project.getEndDate() == null) {
            model.addAttribute("errorMessage", "Project end date is required.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project-form";
        }

        if (project.getEndDate().before(project.getStartDate())) {
            model.addAttribute("errorMessage", "Project end date cannot be before project start date.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project-form";
        }

        // Error handling in case creation fails or an illegal argument is provided
        try {
            int projectId = projectService.createProject(project, employeeIds);

            if (projectId == -1) {
                model.addAttribute("errorMessage", "Error: Could not create project. Please try again.");
                model.addAttribute("employees", employeeService.getAllEmployees());
                return "create-project-form";
            }

            // Return user back to project manager dashboard
            return "redirect:/calculation-tool/project-manager-dashboard";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("employees", employeeService.getAllEmployees());

            // Return and error view to display the error message
            return "error-view";
        }
    }

    // Get all project details
    @GetMapping("/{id}")
    public String getProjectDetails(@PathVariable int id,
                                    Model model,
                                    HttpSession session) {

        // Check if employee is logged in
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        if (employee == null) {
            return "redirect:/calculation-tool/login";
        }

        // Error handling in case project id is invalid
        if (id <= 0) {
            model.addAttribute("errorMessage", "Invalid project ID.");
            return "error-view";
        }

        // Fetch project by project id
        Project project = projectService.getProjectById(id);

        // Error handling in case project is not found
        if (project == null) {
            model.addAttribute("errorMessage", "The requested project does not exist.");
            return "error-view";
        }

        // Get all subprojects with tasks for the project
        List<SubProject> subProjects = subProjectService.getSubProjectsWithTasksByProjectId(id);

        // Calculate the total estimated hours for the whole project
        int totalEstimatedHours = taskService.calculateTotalEstimatedHoursForProject(id);

        // Calculate the total workdays required based on total estimated hours
        int totalWorkdays = taskService.calculateWorkdays(totalEstimatedHours);

        // Add calculated values to the model
        model.addAttribute("totalEstimatedHours", totalEstimatedHours);
        model.addAttribute("totalWorkdays", totalWorkdays);

        // Add project, subprojects (with tasks) and project manager role to the model
        model.addAttribute("project", project);
        model.addAttribute("subProjects", subProjects);
        model.addAttribute("isProjectManager", employee.getIsProjectManager());

        // Show project details
        return "project-details-view";
    }

    // Helper method to check login status and role
    private String checkLoggedInAndPermission(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {

            // Return to login page if employee is not logged in
            return "redirect:/calculation-tool/login";
        }
        if (!loggedInEmployee.getIsProjectManager()) {
            model.addAttribute("errorMessage", "You do not have permission to create projects.");

            // Return error-view if user is not project manager
            return "error-view";
        }
        return null;
    }

}
