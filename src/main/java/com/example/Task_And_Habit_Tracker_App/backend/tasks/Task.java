package com.example.Task_And_Habit_Tracker_App.backend.tasks;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table // Creates table in database for this object.
public class Task {

    @Id
    @SequenceGenerator(
            name = "task_sequence",
            sequenceName = "task_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_sequence"
    )
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private boolean completed;

    public Task() {
    }

    public Task(Long id, String title, String description, LocalDate dueDate, Priority priority, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    public Task(String title, String description, LocalDate dueDate, Priority priority, boolean completed) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Task: " + '\'' +
                "  - Id = " + id +
                "  - Title = '" + title + '\'' +
                "  - Description = '" + description + '\'' +
                "  - Due Date = " + dueDate +
                "  - Priority = " + priority +
                "  - Completed = " + completed;
    }
}

