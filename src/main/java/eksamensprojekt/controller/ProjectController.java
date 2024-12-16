package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.model.SubProject;
import eksamensprojekt.service.EmployeeService;
import eksamensprojekt.service.ProjectService;
import eksamensprojekt.service.SubProjectService;
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

    @Autowired
    public ProjectController(ProjectService projectService, EmployeeService employeeService, SubProjectService subProjectService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.subProjectService = subProjectService;
    }

    // Show create project form
    @GetMapping("/create")
    public String showCreateProjectForm(Model model, HttpSession session) {
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }
        model.addAttribute("project", new Project());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "create-project"; // Returns create-project.html
    }

    // Handle create project form submission
    @PostMapping("/create")
    public String createProject(@ModelAttribute("project") Project project,
                                @RequestParam ("employees") List<Integer> employeeIds,
                                Model model, HttpSession session) {
        // Debug logging
        System.out.println("Received Project: " + project);
        System.out.println("Selected Employees: " + employeeIds);

        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }

        // Input validation
        if (project.getName() == null || project.getName().isEmpty()) {
            model.addAttribute("errorMessage", "Project name is required.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project";
        }

        if (project.getDescription() == null || project.getDescription().isEmpty()) {
            model.addAttribute("errorMessage", "Project description is required.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project";
        }

        if (project.getStartDate() == null) {
            model.addAttribute("errorMessage", "Project start date is required.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project";
        }

        if (project.getEndDate() == null) {
            model.addAttribute("errorMessage", "Project end date is required.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project";
        }

        if (project.getEndDate().before(project.getStartDate())) {
            model.addAttribute("errorMessage", "Project end date cannot be before project start date.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project";
        }

        try {
            int projectId = projectService.createProject(project, employeeIds);

            if (projectId == -1) {
                model.addAttribute("errorMessage", "Error: Could not create project. Please try again.");
                model.addAttribute("employees", employeeService.getAllEmployees());
                return "create-project";
            }

            return "redirect:/calculation-tool/project-manager-dashboard";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "error-view";
        }
    }

    @GetMapping("/{id}")
    public String getProjectDetails(@PathVariable int id,
                                    Model model,
                                    HttpSession session) {

        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        if (employee == null) {
            return "redirect:/calculation-tool/login";
        }

        if (id <= 0) {
            model.addAttribute("errorMessage", "Invalid project ID.");
            return "error-view";
        }

        Project project = projectService.getProjectById(id);
        System.out.println("Project retrieved: " + project); // Logging for debug

        if (project == null) {
            model.addAttribute("errorMessage", "The requested project does not exist.");
            return "error-view";
        }

        List<SubProject> subProjects = subProjectService.getSubProjectsWithTasksByProjectId(id);

        model.addAttribute("project", project);
        model.addAttribute("subProjects", subProjects);
        model.addAttribute("isProjectManager", employee.getIsProjectManager());
        return "project-details-view";
    }

    private String checkLoggedInAndPermission(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/calculation-tool/login";
        }
        if (!loggedInEmployee.getIsProjectManager()) {
            model.addAttribute("errorMessage", "You do not have permission to create projects.");
            return "error-view";
        }
        return null;
    }

}
