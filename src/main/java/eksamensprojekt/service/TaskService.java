package eksamensprojekt.service;

import eksamensprojekt.model.Task;
import eksamensprojekt.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Create new task
    public int createTask(int subprojectId, Task task) {
        return taskRepository.insertTask(subprojectId, task);
    }

    // Get task
    public Task getTaskById(int taskId) {
        return taskRepository.getTaskDetails(taskId);
    }
}
