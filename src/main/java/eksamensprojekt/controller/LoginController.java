package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/calculation-tool")
public class LoginController {

    private final EmployeeService employeeService;

    // Dependency injection in the constructor
    @Autowired
    public LoginController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {

        // Clears error message in case there is one stored in the session
        session.removeAttribute("errorMessage");

        // Show login form
        return "login-form";
    }

    // Handle login submission
    @PostMapping("/login")
    public String processLoginForm(String email, String password, HttpSession session) {

        // Check if the employee login details are correct and save in session
        Employee employee = employeeService.findByEmail(email);

        if (employee != null && employee.getPassword().equals(password)) {
            session.setAttribute("loggedInEmployee", employee);

            // Check if employee is project manager or regular employee
            if (employee.getIsProjectManager()) {
                return "redirect:/calculation-tool/project-manager-dashboard"; // Show project manager dashboard
            } else {
                return "redirect:/calculation-tool/employee-dashboard"; // Show employee dashboard
            }
        }
        session.setAttribute("errorMessage", "Invalid email or password");

        return "redirect:/calculation-tool/login";
    }

    // Handle logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        // Invalidate session when logging out
        session.invalidate();

        // Redirect back to login page
        return "redirect:/calculation-tool/login";
    }


}
