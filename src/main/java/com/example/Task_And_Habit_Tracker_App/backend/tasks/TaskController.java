package com.example.Task_And_Habit_Tracker_App.backend.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/task")
class TaskController {

    private final TaskService taskService;

    @Autowired
    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    List<Task> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping(path = "{taskId}")
    Task getTaskById(@PathVariable("taskId") Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @PostMapping
    Task registerNewTask(@RequestBody Task task) {
        return taskService.createNewTask(task);
    }

    @DeleteMapping(path = "{taskId}")
    void deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);
    }

    @PutMapping(path = "{taskId}")
    void updateTask(@PathVariable("taskId") Long taskId,
                    @RequestParam(required = false) String title,
                    @RequestParam(required = false) String description,
                    @RequestParam(required = false) LocalDate date,
                    @RequestParam(required = false) Priority priority,
                    @RequestParam(required = false) boolean completed) {
        taskService.updateTask(taskId, title, description, date, priority, completed);
    }
}
