package eksamensprojekt.repository;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    // Dependency injection in constructur
    @Autowired
    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert new task into the database
    public boolean insertTask(String name, String description, int priority, int estimatedHours,
                              Date deadline, int employeeId, int subprojectId) {
        String query = "INSERT INTO TASKS (name, description, priority, estimated_hours, deadline, employee_id, subproject_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Lambda function to create and configure prepared statement
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, priority);
            ps.setInt(4, estimatedHours);
            ps.setDate(5, new java.sql.Date(deadline.getTime()));
            ps.setInt(6, employeeId);
            ps.setInt(7, subprojectId);
            return ps;
        });

        return rowsAffected > 0; // Confirm that rows were affected
    }

    // Get task details
    public Task getTaskById(int taskId) {
        String query = "SELECT * FROM TASKS WHERE task_id = ?";

        // Maps rows to task object and returns object
        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Task.class), taskId);
    }

    // Get employee assigned to task
    public Employee getEmployeeForTask(int taskId) {
        String query = "SELECT E.* FROM EMPLOYEES E JOIN TASKS T ON T.EMPLOYEE_ID = E.EMPLOYEE_ID " +
                        "WHERE T.TASK_ID = ?";

            // Map the rows to an employee object and return object
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Employee.class), taskId);
    }


    // Get tasks for specific subproject id
    public List<Task> getTasksBySubProjectId(int subProjectId) {
        String query = "SELECT * FROM TASKS WHERE SUBPROJECT_ID = ?";

        // Maps rows to task objects and returns objects
        return jdbcTemplate.query(query, new Object[]{subProjectId}, new BeanPropertyRowMapper<>(Task.class));
    }

    // Update task completion status
    public boolean updateTaskCompletion(int taskId, boolean isCompleted) {
        String query = "UPDATE TASKS SET is_completed = ? WHERE task_id = ?";

        // Lambda function to create and configure prepared statement
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setBoolean(1, isCompleted);
            ps.setInt(2, taskId);
            return ps;
        });

        return rowsAffected > 0; // Confirm that rows were affected
    }
}
