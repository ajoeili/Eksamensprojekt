package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.model.SubProject;
import eksamensprojekt.service.ProjectService;
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
    private final ProjectService projectService;

    @Autowired
    public SubProjectController(SubProjectService subProjectService, ProjectService projectService) {
        this.subProjectService = subProjectService;
        this.projectService = projectService;
    }

    // Show create subproject form
    @GetMapping("/create/{projectId}")
    public String showCreateSubProjectForm(@PathVariable int projectId,
                                           @ModelAttribute("subProject") SubProject subProject,
                                           Model model, HttpSession session) {
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }
        model.addAttribute("subProject", subProject);
        return "create-subproject";
        }

    // Handle create subproject form submission
    @PostMapping("/create/{projectId}")
    public String createSubProject(@PathVariable int projectId,
                                   @ModelAttribute("subProject") SubProject subProject,
                                   Model model, HttpSession session) {
        String checkLoggedInAndPermission = checkLoggedInAndPermission(model, session);
        if (checkLoggedInAndPermission != null) {
            return checkLoggedInAndPermission;
        }

        // Input validation
        if (subProject.getName() == null || subProject.getName().isEmpty()) {
            model.addAttribute("errorMessage", "Subproject name cannot be empty.");
            return "create-subproject";
        }
        if (subProject.getStartDate() == null || subProject.getEndDate() == null) {
            model.addAttribute("errorMessage", "Subproject dates cannot be null.");
            return "create-subproject";
        }
        if (subProject.getEndDate().before(subProject.getStartDate())) {
            model.addAttribute("errorMessage", "Subproject end date cannot be before the start date.");
            return "create-subproject";
        }

        int subProjectId = subProjectService.createSubProject(projectId, subProject);
        if (subProjectId == -1) {
            model.addAttribute("errorMessage", "Error: Could not create subproject. Please try again.");
            return "error-view";
        }

        return "redirect:/projects/" + projectId;
    }

    // Get subproject details
    @GetMapping("/{id}")
    public String getSubProjectDetails(@PathVariable int id, Model model, HttpSession session) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        if (employee == null) {
            return "redirect:/calculation-tool/login";
        }
        SubProject subProject = subProjectService.getSubProjectById(id);
        if (subProject == null) {
            model.addAttribute("errorMessage", "Subproject not found.");
            return "error-view";
        }
        model.addAttribute("subProject", subProject);
        return "project-details-view";
    }


    private String checkLoggedInAndPermission(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/calculation-tool/login";
        }
        if (!loggedInEmployee.getIsProjectManager()) {
            model.addAttribute("errorMessage", "You do not have permission to create subprojects.");
            return "error-view";
        }
        return null;
    }
}
