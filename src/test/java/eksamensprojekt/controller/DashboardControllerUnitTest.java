package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DashboardControllerUnitTest {

    // Mocking the EmployeeService
    @Mock
    private EmployeeService employeeService;

    // Injecting the mock service into the controller
    @InjectMocks
    private DashboardController dashboardController;

    // Creating a MockMvc instance to simulate HTTP requests
    private MockMvc mockMvc;
    private MockHttpSession mockSession;

    // This method is executed before each test to set up the testing environment
    @BeforeEach
    public void setUp() {
        // Initializing mocks for the current test class
        MockitoAnnotations.openMocks(this);
        // Setting up MockMvc testing
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
        // Creating a mock session to simulate user authentication
        mockSession = new MockHttpSession();
    }


    //Test simulates a logged-in project manager with valid project data
    @Test
    public void testProjectManagerDashboard() throws Exception {
        // Mock employee representing a project manager
        Employee mockEmployee = new Employee(12, "Jens", "Hansen", "test@email.com", "password", "Tester", true);

        // Mock the service call to return a list of projects for the mocked employee
        when(employeeService.getProjectsForEmployee(mockEmployee.getEmployeeId())).thenReturn(List.of(new Project(123, "Test project", "Test description", new Date(), new Date())));

        // Set the mock employee as the logged-in user in the session
        mockSession.setAttribute("loggedInEmployee", mockEmployee);

        // Perform a GET request to the project manager dashboard and check for the following:
        // - HTTP status is OK (200)
        // - The view name should be "project-manager-dashboard-view"
        // - The model should contain the "projects" attribute
        mockMvc.perform(get("/calculation-tool/project-manager-dashboard")
                        .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(view().name("project-manager-dashboard-view"))
                .andExpect(model().attributeExists("projects"));
    }

    // Test simulates a logged-in employee with valid project data
    @Test
    public void testEmployeeDashboard() throws Exception {
        // Mock employee representing an employee (not a project manager)
        Employee mockEmployee = new Employee(12, "Jens", "Hansen", "test@email.com", "password", "Tester", false);

        // Mock the service call to return a list of projects for the mocked employee
        when(employeeService.getProjectsForEmployee(mockEmployee.getEmployeeId())).thenReturn(List.of(new Project(123, "Test project", "Test description", new Date(), new Date())));

        // Set the mock employee as the logged-in user in the session
        mockSession.setAttribute("loggedInEmployee", mockEmployee);

        // Perform a GET request to the employee dashboard and check for the following:
        // - HTTP status is OK (200)
        // - The view name should be "employee-dashboard-view"
        // - The model should contain the "projects" attribute
        mockMvc.perform(get("/calculation-tool/employee-dashboard")
                        .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-dashboard-view"))
                .andExpect(model().attributeExists("projects"));
    }

    // Test simulates an unauthorized user attempting to access the project manager's dashboard
    @Test
    public void testProjectManagerDashboard_Unauthorized() throws Exception {
        // Perform a GET request to the project manager's dashboard without being logged in
        mockMvc.perform(get("/calculation-tool/project-manager-dashboard"))
                .andExpect(status().isFound()) // Expect HTTP status 302
                .andExpect(header().string("Location", "/calculation-tool/login")); // Expect redirection to login page
    }

    // Test simulates a logged-in project manager with no projects assigned
    @Test
    public void testProjectManagerDashboard_NoProjects() throws Exception {
        // Mock employee representing a project manager
        Employee mockEmployee = new Employee(12, "Jens", "Hansen", "test@email.com", "password", "Tester", true);

        // Mock the service call to return an empty list of projects for the mocked employee
        when(employeeService.getProjectsForEmployee(mockEmployee.getEmployeeId())).thenReturn(new ArrayList<>());

        // Set the mock employee as the logged-in user in the session
        mockMvc.perform(get("/calculation-tool/project-manager-dashboard")
                        .sessionAttr("loggedInEmployee", mockEmployee))
                .andExpect(status().isOk())
                .andExpect(view().name("project-manager-dashboard-view"))
                .andExpect(model().attribute("projects", new ArrayList<>())); // Expect an empty list of projects in the model
    }

     // Test simulates an unauthorized user attempting to access the employee's dashboard.
    @Test
    public void testEmployeeDashboard_Unauthorized() throws Exception {
        // Perform a GET request to the employee's dashboard without being logged in
        mockMvc.perform(get("/calculation-tool/employee-dashboard"))
                .andExpect(status().isFound()) // Expect HTTP status 302
                .andExpect(header().string("Location", "/calculation-tool/login")); // Expect redirection to login page
    }

    //Test simulates a logged-in employee with no projects assigned
    @Test
    public void testEmployeeDashboard_NoProjects() throws Exception {
        // Mock employee representing an employee (not a project manager)
        Employee mockEmployee = new Employee(12, "Jens", "Hansen", "test@email.com", "password", "Tester", false);

        // Mock the service call to return an empty list of projects for the mocked employee
        when(employeeService.getProjectsForEmployee(mockEmployee.getEmployeeId())).thenReturn(new ArrayList<>());

        // Set the mock employee as the logged-in user in the session
        mockMvc.perform(get("/calculation-tool/employee-dashboard")
                        .sessionAttr("loggedInEmployee", mockEmployee))
                .andExpect(status().isOk()) // Expect HTTP status OK (200)
                .andExpect(view().name("employee-dashboard-view")) // Expect the correct view to be rendered
                .andExpect(model().attribute("projects", new ArrayList<>())); // Expect an empty list of projects in the model
    }
}
