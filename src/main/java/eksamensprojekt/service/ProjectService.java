package eksamensprojekt.service;

import eksamensprojekt.model.Project;
import eksamensprojekt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

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
            projectRepository.insert(project.getName(), project.getDescription(), project.getStartDate(), project.getEndDate());
        }
    }
}
