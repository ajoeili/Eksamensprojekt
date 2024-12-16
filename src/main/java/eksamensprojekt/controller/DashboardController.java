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

        // Check if employee is logged in and if they are the project manager
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");

        if (employee == null || !employee.getIsProjectManager()) {
            return "redirect:/calculation-tool/login";
        }

        // Add employee name and projects to the model
        model.addAttribute("employeeName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("projects", employeeService.getProjectsForEmployee(employee.getEmployeeId()));

        // Show project manager dashboard
        return "project-manager-dashboard-view";
    }

    // Employee dashboard
    @GetMapping("/employee-dashboard")
    public String employeeDashboard(HttpSession session, Model model) {

        // Check if employee is logged in and is not project manager
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");

        if (employee == null || employee.getIsProjectManager()) {
            return "redirect:/calculation-tool/login";
        }

        // Add employee name and projects to the model
        model.addAttribute("employeeName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("projects", employeeService.getProjectsForEmployee(employee.getEmployeeId()));

        // Show employee dashboard
        return "employee-dashboard-view";
    }



}
