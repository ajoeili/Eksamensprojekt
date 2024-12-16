package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.SubProject;
import eksamensprojekt.service.SubProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subprojects")
public class SubProjectController {

    private final SubProjectService subProjectService;

    // Dependency injection in constructor
    @Autowired
    public SubProjectController(SubProjectService subProjectService) {
        this.subProjectService = subProjectService;

    }

    // Create new subproject
    @GetMapping("/create/{projectId}")
    public String showCreateSubProjectForm(@PathVariable("projectId") int projectId,
                                           Model model,
                                           HttpSession session) {

        // Check user is logged in and is project manager
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }

        // Error handling in case project id is invalid
        if (projectId <= 0) {
            model.addAttribute("errorMessage", "Invalid project ID.");
            return "error-view";
        }

        // Add a new subproject object to the model for form binding
        SubProject subProject = new SubProject();
        model.addAttribute("subProject", subProject);

        // Pass the project id to the model so it can be included in the form
        model.addAttribute("projectId", projectId);

        // Return the view for the create-subproject form
        return "create-subproject-form";
    }


    // Handle create subproject form submission
    @PostMapping("/create/{projectId}")
    public String createSubProject(@ModelAttribute("subproject") SubProject subProject,
                                   @PathVariable("projectId") int projectId,
                                   Model model, HttpSession session) {

        // Check if user is logged in and is project manager
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }

        // Input validation for every field
        if (subProject.getName() == null || subProject.getName().isEmpty()) {
            model.addAttribute("errorMessage", "Subproject name is required.");
            return "create-subproject-form";
        }

        if (subProject.getDescription() == null || subProject.getDescription().isEmpty()) {
            model.addAttribute("errorMessage", "Subproject description is required.");
            return "create-subproject-form";
        }

        if (subProject.getStartDate() == null) {
            model.addAttribute("errorMessage", "Subproject start date is required.");
            return "create-subproject-form";
        }

        if (subProject.getEndDate() == null) {
            model.addAttribute("errorMessage", "Subproject end date is required.");
            return "create-subproject-form";
        }

        if (subProject.getEndDate().before(subProject.getStartDate())) {
            model.addAttribute("errorMessage", "Subproject end date cannot be before the start date.");
            return "create-subproject-form";
        }

        // Error handling in case creation fails or illegal argument is provided
        try {
            int subProjectId = subProjectService.createSubProject(subProject, projectId);

            if (subProjectId == -1) {
                model.addAttribute("errorMessage", "Error: Could not create subproject. Please try again.");
                return "error-view";
            }
            // Returns user to project details view
            return "redirect:/projects/" + projectId;

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());

            // Returns an error view to display error message
            return "error-view";
        }
    }

    // Get subproject details
    @GetMapping("/{id}")
    public String getSubProjectDetails(@PathVariable int id, Model model, HttpSession session) {

        // Check if user is logged in
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        if (employee == null) {
            return "redirect:/calculation-tool/login";
        }

        // Fetch subproject by subproject id
        SubProject subProject = subProjectService.getSubProjectById(id);

        // Error handling in case subproject does not exist
        if (subProject == null) {
            model.addAttribute("errorMessage", "Subproject not found.");
            return "error-view";
        }

        // Add subproject to the model
        model.addAttribute("subProject", subProject);

        // Show project details in the view
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
            model.addAttribute("errorMessage", "You do not have permission to create subprojects.");

            // Return error-view if user is not project manager
            return "error-view";
        }
        return null;
    }
}
