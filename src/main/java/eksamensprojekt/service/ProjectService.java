package eksamensprojekt.service;

import eksamensprojekt.model.Project;
import eksamensprojekt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    // Dependency injection in constructor
    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Create new project
    public int createProject(Project project, List<Integer> employeeIds) {

        // Service level validation ensures the data integrity of the project object
        if (project.getName() == null || project.getName().isEmpty()) {
            throw new IllegalArgumentException("Project name is required.");
        }
        if (project.getDescription() == null || project.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Project description is required.");
        }
        if (project.getStartDate() == null) {
            throw new IllegalArgumentException("Project start date is required.");
        }
        if (project.getEndDate() == null) {
            throw new IllegalArgumentException("Project end date is required.");
        }
        if (project.getEndDate().before(project.getStartDate())) {
            throw new IllegalArgumentException("Project end date cannot be before project start date.");
        }

        // Insert the project into the database
        int projectId = projectRepository.insertProject(
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate());

        if (projectId == -1) {
            return -1;  // Return -1 if project creation fails
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
        return projectId;  // Return the project id if successful
    }


    // Get project by ID
    public Project getProjectById(int projectId) {
        return projectRepository.getProjectDetails(projectId);
    }

}


