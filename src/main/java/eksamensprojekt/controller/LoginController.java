package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calculationtool")
public class LoginController {

    private final EmployeeService employeeService;
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class); // Debugging

    @Autowired
    public LoginController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Show landing page
    @GetMapping("")
    public String showLandingPage() {
        return "landing-page"; // Returns landing-page.html
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "login-form"; // Returns login-form.html
    }

    // Handle login submission



}
