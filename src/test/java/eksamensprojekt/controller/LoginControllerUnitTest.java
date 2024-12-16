package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoginControllerUnitTest {

    // Mocking EmployeeService to simulate the behavior of the real service
    @Mock
    private EmployeeService employeeService;

    // Injecting the mock service into the LoginController
    @InjectMocks
    private LoginController loginController;

    // MockMvc instance to simulate HTTP requests and verify the results
    private MockMvc mockMvc;
    private MockHttpSession mockSession;

    // This method sets up the test environment before each test method
    @BeforeEach
    public void setUp() {
        // Initialize the mocks and inject them into the controller
        MockitoAnnotations.openMocks(this);
        // Set up MockMvc to test the LoginController
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        // Create a mock session to simulate a user session during tests
        mockSession = new MockHttpSession();
    }

    // Test simulates a successful login attempt with valid credentials
    @Test
    public void testProcessLoginForm_success() throws Exception {
        // Mock the employee data to simulate a successful login
        Employee mockEmployee = new Employee(12, "Lise", "Hansen", "test6@email.com", "password", "Tester", true);

        // Mock the service method to return the mock employee when searching by email
        when(employeeService.findByEmail("test6@email.com")).thenReturn(mockEmployee);

        // Perform a POST request to the login endpoint with valid credentials
        // Verify the user is redirected to the project manager dashboard upon successful login
        mockMvc.perform(post("/calculation-tool/login")
                        .param("email", "test6@email.com") // Simulate entering email in the login form
                        .param("password", "password")    // Simulate entering password in the login form
                        .session(mockSession))                          // Attach the mock session to simulate user login
                .andExpect(redirectedUrl("/calculation-tool/project-manager-dashboard")); // Expect a redirect to the project manager dashboard
    }


     // Test simulates a failed login attempt with invalid credentials
    @Test
    public void testProcessLoginForm_failure() throws Exception {
        // Mock the employee service to return null when searching with an invalid email
        when(employeeService.findByEmail("invalid@email.com")).thenReturn(null);

        // Perform a POST request to the login endpoint with invalid credentials
        // Verify the following:
        // 1. The response status is HTTP 302 (Found) indicating a redirect
        // 2. The user is redirected back to the login page
        // 3. An error message is set in the session indicating invalid credentials
        mockMvc.perform(post("/calculation-tool/login")
                        .param("email", "invalid@email.com")   // Simulate entering an invalid email in the login form
                        .param("password", "invalidPassword")  // Simulate entering an incorrect password
                        .session(mockSession))                               // Attach the mock session
                .andExpect(status().isFound())                               // Expect HTTP 302 redirect status
                .andExpect(redirectedUrl("/calculation-tool/login"))  // Expect redirect to the login page
                .andExpect(request().sessionAttribute("errorMessage", "Invalid email or password")); // Verify the error message in the session
    }
}
