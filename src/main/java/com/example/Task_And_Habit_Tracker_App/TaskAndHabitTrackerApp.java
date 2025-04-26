package com.example.Task_And_Habit_Tracker_App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.SwingUtilities;

@SpringBootApplication
public class TaskAndHabitTrackerApp {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        // Start the Spring Boot Server.
        context = SpringApplication.run(TaskAndHabitTrackerApp.class, args);

        // Start the Java Swing User Interface.
        SwingUtilities.invokeLater(() -> {
            // Add ui window here.
        });

        // Add shutdown hook to cleanly stop Spring context when the app exits.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (context != null) {
                context.close();
            }
        }));
    }

}
