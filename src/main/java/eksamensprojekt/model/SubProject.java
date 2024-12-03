package eksamensprojekt.model;

import java.util.Date;
import java.util.List;

public class SubProject {
    private int subProjectId;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private List<Task> tasks;

    public SubProject() {}

    public SubProject(int subProjectId, String name, String description, Date startDate, Date endDate) {
        this.subProjectId = subProjectId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(int subProjectId) {
        this.subProjectId = subProjectId;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
