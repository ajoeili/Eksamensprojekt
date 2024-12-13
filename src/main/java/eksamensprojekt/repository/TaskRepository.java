package eksamensprojekt.repository;

import eksamensprojekt.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
