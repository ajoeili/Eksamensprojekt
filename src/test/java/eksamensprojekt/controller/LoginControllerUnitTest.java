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

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;
    private MockHttpSession mockSession;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        mockSession = new MockHttpSession();
    }

    @Test
    public void testProcessLoginForm_success() throws Exception {
        // Arrange
        Employee mockEmployee = new Employee(12, "Jens", "Hansen", "test@email.com", "password", "Tester", true);

        // Act and assert
        when(employeeService.findByEmail("test@email.com")).thenReturn(mockEmployee);

        mockMvc.perform(post("/calculation-tool/login")
                .param("email", "test@email.com")
                .param("password", "password")
                .session(mockSession))
                .andExpect(redirectedUrl("/calculation-tool/project-manager-dashboard"));
    }

    @Test
    public void testProcessLoginForm_failure() throws Exception {

        when(employeeService.findByEmail("invalid@email.com")).thenReturn(null);

        mockMvc.perform(post("/calculation-tool/login")
                .param("email", "invalid@email.com")
                .param("password", "invalidPassword")
                .session(mockSession))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/calculation-tool/login"))
                .andExpect(request().sessionAttribute("errorMessage", "Invalid email or password"));
    }

}
