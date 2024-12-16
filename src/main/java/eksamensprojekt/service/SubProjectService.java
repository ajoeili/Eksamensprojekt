package eksamensprojekt.service;

import eksamensprojekt.model.SubProject;
import eksamensprojekt.model.Task;
import eksamensprojekt.repository.SubProjectRepository;
import eksamensprojekt.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {

    private final SubProjectRepository subProjectRepository;
    private final TaskRepository taskRepository;

    // Dependency injection in constructor
    @Autowired
    public SubProjectService(SubProjectRepository subProjectRepository, TaskRepository taskRepository) {
        this.subProjectRepository = subProjectRepository;
        this.taskRepository = taskRepository;
    }

    // Create new subproject
    public int createSubProject(SubProject subProject, int projectId) {

        // Service level validation ensures the data integrity of the subproject object
        if (subProject.getName() == null || subProject.getName().isEmpty()) {
            throw new IllegalArgumentException("Subproject name is required");
        }
        if (subProject.getDescription() == null || subProject.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Subproject description is required");
        }
        if (subProject.getStartDate() == null) {
            throw new IllegalArgumentException("Subproject start date is required");
        }
        if (subProject.getEndDate() == null) {
            throw new IllegalArgumentException("Subproject end date is required");
        }
        if (subProject.getEndDate().before(subProject.getStartDate())) {
            throw new IllegalArgumentException("Subproject start date must be before subproject end date");
        }

        // Insert subproject into the database
        return subProjectRepository.insertSubProject(
                subProject.getName(),
                subProject.getDescription(),
                subProject.getStartDate(),
                subProject.getEndDate(),
                projectId
        );
    }



    // Get subproject by id
    public SubProject getSubProjectById(int subProjectId) {
        return subProjectRepository.getSubProjectDetails(subProjectId);
    }

    // Get subprojects with tasks for specific project id
    public List<SubProject> getSubProjectsWithTasksByProjectId(int projectId) {

        // Fetch list of subprojects for project id
        List<SubProject> subProjects = subProjectRepository.getSubProjectsByProjectId(projectId);

        // For each subproject, fetch associated tasks and set them
        for (SubProject subProject : subProjects) {
            List<Task> tasks = taskRepository.getTasksBySubProjectId(subProject.getSubprojectId());
            subProject.setTasks(tasks);  // Setting tasks in each subproject
        }

        return subProjects;
    }

    // Get project id by subproject id
    public int getProjectIdBySubProjectId(int subProjectId) {
        return subProjectRepository.getProjectIdBySubProjectId(subProjectId);
    }



}
