package eksamensprojekt.service;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.model.IModel;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Create new project
    public int createProject(Project project) {

        // Insert project into database
        int projectId = projectRepository.insertProject(
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate());

        if (projectId == -1) {
            return -1;
    }
        // Add employees to the project
        if (project.getEmployees() != null && !project.getEmployees().isEmpty()) {
            for (Employee employee : project.getEmployees()) {
                if (employee.getEmployeeId() <= 0) {
                    throw new IllegalArgumentException("Invalid employee ID: " + employee.getEmployeeId());
                }
                projectRepository.insertProjectEmployee(projectId, employee.getEmployeeId());
            }
        }
        return projectId;
    }

    // Get project
    public Project getProjectById(int projectId) {
        return projectRepository.getProjectDetails(projectId);
    }

}


