package eksamensprojekt.model;

import java.util.Date;

public class Task {
    private int taskId;
    private String name;
    private String description;
    private int priority; // TODO: Kunne være enum
    private int estimatedHours;
    private Date deadline;

    public Task() {}

    public Task(int taskId, String name, String description, int priority, int estimatedHours, Date deadline) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.estimatedHours = estimatedHours;
        this.deadline = deadline;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
