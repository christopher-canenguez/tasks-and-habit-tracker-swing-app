package com.example.Task_Tracker_App.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    List<Task> getTasks() {
        return taskRepository.findAll();
    }

    Task getTaskById(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isEmpty()) {
            throw new IllegalStateException("Task with Id " + taskId + " doesn't exist.");
        }

        return taskOptional.get();
    }

    Task createNewTask(Task task) {
        return taskRepository.save(task);
    }

    void deleteTask(Long taskId) {
        boolean taskExists = taskRepository.existsById(taskId);

        if (!taskExists) {
            throw new IllegalStateException("Task with Id " + taskId + " doesn't exist.");
        }

        taskRepository.deleteById(taskId);
    }

    @Transactional
    Task updateTask(Long taskId, Task updatedTask) {
        Task existingTask = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new IllegalStateException(
                        "Task with id " + taskId + " doesn't exist."
                ));

        if (isTitleValid(updatedTask.getTitle(), existingTask)) {
            existingTask.setTitle(updatedTask.getTitle());
        }

        if (isDescriptionValid(updatedTask.getDescription(), existingTask)) {
            existingTask.setDescription(updatedTask.getDescription());
        }

        if (isDueDateValid(updatedTask.getDueDate(), existingTask)) {
            existingTask.setDueDate(updatedTask.getDueDate());
        }

        if (isPriorityValid(updatedTask.getPriority(), existingTask)) {
            existingTask.setPriority(updatedTask.getPriority());
        }

        if (isCompletionStatusValid(updatedTask.isComplete(), existingTask)) {
            existingTask.setComplete(updatedTask.isComplete());
        }

        return taskRepository.save(existingTask);
    }

    private boolean isTitleValid(String title, Task task) {
        return title != null && !title.isEmpty() && !Objects.equals(task.getTitle(), title);
    }

    private boolean isDescriptionValid(String description, Task task) {
        return description != null && !description.isEmpty() && !Objects.equals(task.getDescription(), description);
    }

    private boolean isDueDateValid(LocalDate date, Task task) {
        return date != null && !Objects.equals(task.getDueDate(), date);
    }

    private boolean isPriorityValid(Priority priority, Task task) {
        return priority != null && !Objects.equals(task.getPriority(), priority);
    }

    private boolean isCompletionStatusValid(boolean completed, Task task) {
        return !Objects.equals(task.isComplete(), completed);
    }

}
