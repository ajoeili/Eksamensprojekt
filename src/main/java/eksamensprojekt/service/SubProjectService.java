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


    @Autowired
    public SubProjectService(SubProjectRepository subProjectRepository, TaskRepository taskRepository) {
        this.subProjectRepository = subProjectRepository;
        this.taskRepository = taskRepository;
    }

    // Create new subproject
    public int createSubProject(int projectId, SubProject subProject) {
        return subProjectRepository.insertSubProject(projectId, subProject);
    }

    // Get subproject
    public SubProject getSubProjectById(int subProjectId) {
        return subProjectRepository.getSubProjectDetails(subProjectId);
    }

    public List<SubProject> getSubProjectsWithTasksByProjectId(int projectId) {
        List<SubProject> subProjects = subProjectRepository.getSubProjectsByProjectId(projectId);

        // For each subproject, fetch associated tasks and set them
        for (SubProject subProject : subProjects) {
            System.out.println("Fetching tasks for subproject with ID: " + subProject.getSubprojectId());
            List<Task> tasks = taskRepository.getTasksBySubProjectId(subProject.getSubprojectId());
            System.out.println("Found " + tasks.size() + " tasks for subproject: " + subProject.getName());
            subProject.setTasks(tasks);  // Setting tasks in each subproject
        }

        return subProjects;
    }



}
