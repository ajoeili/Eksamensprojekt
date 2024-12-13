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

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private DashboardController dashboardController;

    private MockMvc mockMvc;
    private MockHttpSession mockSession;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
        mockSession = new MockHttpSession();
    }

    @Test
    public void testProjectManagerDashboard() throws Exception {
        Employee mockEmployee = new Employee(12, "Jens", "Hansen", "test@email.com", "password", "Tester", true);

        when(employeeService.getProjectsForEmployee(mockEmployee.getEmployeeId())).thenReturn(List.of(new Project(123, "Test project", "Test description", new Date(), new Date())));

        mockSession.setAttribute("loggedInEmployee", mockEmployee);

        mockMvc.perform(get("/calculation-tool/project-manager-dashboard")
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(view().name("project-manager-dashboard-view"))
                .andExpect(model().attributeExists("projects"));
    }

    @Test
    public void testEmployeeDashboard() throws Exception {
        Employee mockEmployee = new Employee(12, "Jens", "Hansen", "test@email.com", "password", "Tester", false);

        when(employeeService.getProjectsForEmployee(mockEmployee.getEmployeeId())).thenReturn(List.of(new Project(123, "Test project", "Test description",new Date(), new Date())));

        mockSession.setAttribute("loggedInEmployee", mockEmployee);

        mockMvc.perform(get("/calculation-tool/employee-dashboard")
                        .session(mockSession))
                        .andExpect(status().isOk())
                        .andExpect(view().name("employee-dashboard-view"))
                        .andExpect(model().attributeExists("projects"));
    }

    @Test
    public void testProjectManagerDashboard_Unauthorized() throws Exception {
        mockMvc.perform(get("/calculation-tool/project-manager-dashboard"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/calculation-tool/login"));
    }

    @Test
    public void testProjectManagerDashboard_NoProjects() throws Exception {
        Employee mockEmployee = new Employee(12, "Jens", "Hansen", "test@email.com", "password", "Tester", true);

        when(employeeService.getProjectsForEmployee(mockEmployee.getEmployeeId())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/calculation-tool/project-manager-dashboard")
                        .sessionAttr("loggedInEmployee", mockEmployee))
                        .andExpect(status().isOk())
                        .andExpect(view().name("project-manager-dashboard-view"))
                        .andExpect(model().attribute("projects", new ArrayList<>()));
    }

    @Test
    public void testEmployeeDashboard_Unauthorized() throws Exception {
        mockMvc.perform(get("/calculation-tool/employee-dashboard"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/calculation-tool/login"));
    }

    @Test
    public void testEmployeeDashboard_NoProjects() throws Exception {
        Employee mockEmployee = new Employee(12, "Jens", "Hansen", "test@email.com", "password", "Tester", false);

        when(employeeService.getProjectsForEmployee(mockEmployee.getEmployeeId())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/calculation-tool/employee-dashboard")
                        .sessionAttr("loggedInEmployee", mockEmployee))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-dashboard-view"))
                .andExpect(model().attribute("projects", new ArrayList<>()));
    }
}
