package com.example.Task_Tracker_App.backend;

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
import org.springframework.web.bind.annotation.RestController;

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
    ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskService.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping(path = "{taskId}")
    ResponseEntity<Task> getTaskById(@PathVariable("taskId") Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    ResponseEntity<Task> registerNewTask(@RequestBody Task task) {
        Task createdTask = taskService.createNewTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @DeleteMapping(path = "{taskId}")
    ResponseEntity<Void> deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build(); // 204 - No content.
    }

    @PutMapping(path = "{taskId}")
    ResponseEntity<Task> updateTask(@PathVariable("taskId") Long taskId, @RequestBody Task updatedTask) {
        Task savedTask = taskService.updateTask(taskId, updatedTask);
        return ResponseEntity.ok(savedTask); // 200 - OK.
    }
}
