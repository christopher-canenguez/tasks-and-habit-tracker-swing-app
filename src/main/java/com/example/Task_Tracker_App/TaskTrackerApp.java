package com.example.Task_Tracker_App;

import com.example.Task_Tracker_App.frontend.view.TaskPanel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SpringBootApplication
public class TaskTrackerApp {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");

        // Start the Spring Boot Server.
        context = SpringApplication.run(TaskTrackerApp.class, args);

        // Start the Java Swing User Interface.
        SwingUtilities.invokeLater(() -> {
            // Add ui window here.
            JFrame frame = new JFrame("Task Tracker");
            TaskPanel taskPanel = new TaskPanel();
            frame.setContentPane(taskPanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // Add shutdown hook to cleanly stop Spring context when the app exits.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (context != null) {
                context.close();
            }
        }));
    }

}
