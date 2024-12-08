package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.service.EmployeeService;
import eksamensprojekt.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;

    @Autowired
    public ProjectController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    // Show create project form
    @GetMapping("/create")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "create-project"; // Returns create-project.html
    }

    // Handle create project form submission
    @PostMapping("/create")
    public String createProject(@ModelAttribute("project") Project project,
                                @RequestParam ("employees") List<Integer> employeeIds, Model model) {

        List<Employee> employees = new ArrayList<>();

        for (Integer employeeId : employeeIds) {
            Employee employee = new Employee();
            employee.setEmployeeId(employeeId);
            employees.add(employee);
        }
        project.setEmployees(employees);

        try {
            projectService.createProject(project);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "create-project";
        }
        return "redirect:/projects";
    }


}
