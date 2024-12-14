package eksamensprojekt.repository;

import eksamensprojekt.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert new task into the database
    public int insertTask(int subProjectId, Task task) {
        String query = "INSERT INTO TASKS (subproject_id, name, description, priority, estimated_hours, deadline) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(query, subProjectId, task.getName(), task.getDescription(),
                task.getPriority(), task.getEstimatedHours(), task.getDeadline());
    }

    // Select task details
    public Task getTaskDetails(int taskId) {
        String query = "SELECT * FROM TASKS WHERE task_id = ?";
        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Task.class), taskId);
    }

    // Get tasks by subproject ID using BeanPropertyRowMapper
    public List<Task> getTasksBySubProjectId(int subProjectId) {
        System.out.println("Fetching tasks for subproject with ID: " + subProjectId);
        String query = "SELECT * FROM TASKS WHERE SUBPROJECT_ID = ?";
        List<Task> tasks = jdbcTemplate.query(query, new Object[]{subProjectId}, new BeanPropertyRowMapper<>(Task.class));
        System.out.println("Found " + tasks.size() + " tasks for subproject ID " + subProjectId);
        return tasks;
    }

}
