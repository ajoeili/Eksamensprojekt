package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calculation-tool")
public class DashboardController {

    private final EmployeeService employeeService;

    @Autowired
    public DashboardController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Project manager dashboard
    @GetMapping("/project-manager-dashboard")
    public String projectManagerDashboard(HttpSession session, Model model) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");

        if (employee == null || !employee.getIsProjectManager()) {
            return "redirect:/calculation-tool/login";
        }
        model.addAttribute("employeeName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("projects", employeeService.getProjectsForEmployee(employee.getEmployeeId()));
        return "project-manager-dashboard-view";
    }

    // Employee dashboard
    @GetMapping("/employee-dashboard")
    public String employeeDashboard(HttpSession session, Model model) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");

        if (employee == null || employee.getIsProjectManager()) { // TODO tjek om det er bedre med en hjælpemetode
            return "redirect:/calculation-tool/login";
        }

        // Log employee information
        System.out.println("DEBUG: Logged in employee ID: " + employee.getEmployeeId());
        System.out.println("DEBUG: Employee Name: " + employee.getFirstName() + " " + employee.getLastName());

        // Log projects retrieved
        System.out.println("DEBUG: Fetching projects for employee...");
        employeeService.getProjectsForEmployee(employee.getEmployeeId()).forEach(project ->
                System.out.println("DEBUG: Project ID: " + project.getProjectId() + ", Name: " + project.getName())
        );

        model.addAttribute("employeeName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("projects", employeeService.getProjectsForEmployee(employee.getEmployeeId()));
        return "employee-dashboard-view";
    }



}
