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

    @Autowired
    public LoginController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        session.removeAttribute("errorMessage"); // Clears error message in case there is one stored in the session
        return "login-form"; // Returns login-form.html
    }

    // Handle login submission
    @PostMapping("/login")
    public String processLoginForm(String email, String password, HttpSession session) {
        Employee employee = employeeService.findByEmail(email);

        if (employee != null && employee.getPassword().equals(password)) {
            session.setAttribute("loggedInEmployee", employee);

            // Debugging the role
            System.out.println("DEBUG: Employee Role: " + employee.getRole());
            System.out.println("DEBUG: Is Project Manager: " + employee.getIsProjectManager());

            if (employee.getIsProjectManager()) {
                return "redirect:/calculation-tool/project-manager-dashboard"; // Redirects to project-manager-dashboard-view.html
            } else {
                return "redirect:/calculation-tool/employee-dashboard"; // Redirects to employee dashboard.html
            }
        }
        session.setAttribute("errorMessage", "Invalid email or password");

        return "redirect:/calculation-tool/login";
    }

    // Handle logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/calculation-tool/login"; // Redirects to login-form.html
    }


}
