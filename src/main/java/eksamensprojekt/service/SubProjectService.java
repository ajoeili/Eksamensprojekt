package eksamensprojekt.service;

import eksamensprojekt.model.SubProject;
import eksamensprojekt.repository.SubProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubProjectService {

    private final SubProjectRepository subProjectRepository;

    @Autowired
    public SubProjectService(SubProjectRepository subProjectRepository) {
        this.subProjectRepository = subProjectRepository;
    }

    // Create new subproject
    public int createSubProject(int projectId, SubProject subProject) {
        return subProjectRepository.insertSubProject(projectId, subProject);
    }

    // Get subproject
    public SubProject getSubProjectById(int subProjectId) {
        return subProjectRepository.getSubProjectDetails(subProjectId);
    }


}
