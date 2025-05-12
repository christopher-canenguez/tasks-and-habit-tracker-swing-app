package com.example.Task_And_Habit_Tracker_App.backend.tasks;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
class TaskConfig {

    @Bean
    CommandLineRunner taskCommandLineRunner(TaskRepository taskRepository) {
        return args -> {
            Task groceryTask = new Task(
                    "Groceries",
                    "Go shopping for groceries for Christmas.",
                    LocalDate.now(),
                    Priority.HIGH,
                    true
            );

            Task trashTask = new Task(
                    "Trash",
                    "Take out trash Thursday.",
                    LocalDate.now().plusDays(5),
                    Priority.LOW,
                    false
            );

            taskRepository.saveAll(
                    List.of(groceryTask, trashTask)
            );
        };
    }

}
