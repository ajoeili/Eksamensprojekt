package eksamensprojekt.service;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.model.IModel;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Create new project
    public int createProject(Project project, List<Integer> employeeIds) {

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
        if (employeeIds != null && !employeeIds.isEmpty()) {
            for (Integer employeeId : employeeIds) {
                if (employeeId <= 0) {
                    throw new IllegalArgumentException("Invalid employee ID: " + employeeId);
                }
                projectRepository.insertProjectEmployee(projectId, employeeId);
            }
        }
        return projectId;
    }

    // Get project by ID
    public Project getProjectById(int projectId) {
        return projectRepository.getProjectDetails(projectId);
    }

}


