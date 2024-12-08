package eksamensprojekt.service;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Project;
import eksamensprojekt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Create new project
    public void createProject(Project project) {
        if (project != null) {
            if (project.getName().isEmpty()) {
                throw new IllegalArgumentException(
                        "Project name cannot be empty");
            }
            if (project.getDescription().isEmpty()) {
                throw new IllegalArgumentException(
                        "Project description cannot be empty");
            }
            if (project.getStartDate() == null) {
                throw new IllegalArgumentException(
                        "Project start date cannot be empty");
            }
            if (project.getEndDate() == null) {
                throw new IllegalArgumentException(
                        "Project end date cannot be empty");
            }
            if (project.getEndDate().before(project.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }

           int projectId = projectRepository.insertProject(
                   project.getName(),
                   project.getDescription(),
                   project.getStartDate(),
                   project.getEndDate());

            if (project.getEmployees() != null && !project.getEmployees().isEmpty()) {
                for (Employee employee : project.getEmployees()) {
                    if (employee.getEmployeeId() <= 0) {
                        throw new IllegalArgumentException("Invalid employee ID: " + employee.getEmployeeId());
                    }
                    projectRepository.insertProjectEmployee(projectId, employee.getEmployeeId());
                }
            }
        }
    }
}
