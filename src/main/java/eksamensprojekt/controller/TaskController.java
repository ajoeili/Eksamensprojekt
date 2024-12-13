package eksamensprojekt.controller;

import eksamensprojekt.model.Employee;
import eksamensprojekt.model.Task;
import eksamensprojekt.service.SubProjectService;
import eksamensprojekt.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final SubProjectService subProjectService;

    @Autowired
    public TaskController(TaskService taskService, SubProjectService subProjectService) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
    }

    // Show create task form
    @GetMapping("/create/{subProjectId}")
    public String showCreateTaskForm(@PathVariable int subProjectId,
                                     @ModelAttribute("task") Task task,
                                     Model model, HttpSession session) {
        String checkLoggedIn = checkLoggedIn(model, session);
        if (checkLoggedIn != null) {
            return checkLoggedIn;
        }

        model.addAttribute("task", new Task());
        return "create-task-form"; // TODO: Husk at ændre create-subproject og create-project
    }

    // Handle create task form submission
    @PostMapping("/create/{subProjectId}")
    public String createTask(@PathVariable int subProjectId,
                             @ModelAttribute("task") Task task,
                             Model model, HttpSession session) {
        String checkLoggedIn = checkLoggedIn(model, session);
        if (checkLoggedIn != null) {
            return checkLoggedIn;
        }

        // Input validation
        if (task.getName() == null || task.getName().isEmpty()) {
            model.addAttribute("errorMessage", "Name is required.");
            return "create-task-form";
        }

        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            model.addAttribute("errorMessage", "Description is required.");
            return "create-task-form";
        }

        if (task.getPriority() == 0) {
            model.addAttribute("errorMessage", "Priority is required.");
            return "create-task-form";
        }

        if (task.getDeadline() == null) {
            model.addAttribute("errorMessage", "Deadline is required.");
            return "create-task-form";
        }

        if (task.getEstimatedHours() == 0) {
            model.addAttribute("errorMessage", "Estimated hours is required.");
            return "create-task-form";
        }

        int taskId = taskService.createTask(subProjectId, task);
        if (taskId == -1) {
            model.addAttribute("errorMessage", "Error: Could not create task. Please try again.");
            return "error-view";
        }

        return "redirect:/subprojects/" + subProjectId; // Redirects to subproject details
    }

    @GetMapping("/{id}")
    public String getTaskDetails(@PathVariable int id, Model model, HttpSession session) {
        String checkLoggedIn = checkLoggedIn(model, session);
        if (checkLoggedIn != null) {
            return checkLoggedIn;
        }
        Task task = taskService.getTaskById(id);
        if (task == null) {
            model.addAttribute("errorMessage", "Task not found.");
            return "error-view";
        }
        model.addAttribute("task", task);
        return "project-details-view"; // TODO overvej om der skal redirect'es et andet sted hen
    }

    private String checkLoggedIn(Model model, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        if (loggedInEmployee == null) {
            return "redirect:/calculation-tool/login";
        }
        return null;
    }


}
