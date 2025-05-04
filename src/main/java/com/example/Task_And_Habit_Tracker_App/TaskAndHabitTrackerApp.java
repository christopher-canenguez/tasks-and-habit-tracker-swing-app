package com.example.Task_And_Habit_Tracker_App;

import com.example.Task_And_Habit_Tracker_App.frontend.HabitPanel;
import com.example.Task_And_Habit_Tracker_App.frontend.TaskPanel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

@SpringBootApplication
public class TaskAndHabitTrackerApp {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        JTabbedPane tabbedPane = new JTabbedPane();

        // Start the Spring Boot Server.
        //context = SpringApplication.run(TaskAndHabitTrackerApp.class, args);

        // Start the Java Swing User Interface.
        SwingUtilities.invokeLater(() -> {
            // Add ui window here.
            JFrame frame = new JFrame("Task & Habit Tracker");

            TaskPanel taskPanel = new TaskPanel();
            HabitPanel habitPanel = new HabitPanel();
            tabbedPane.add("Task Tracker", taskPanel);
            tabbedPane.add("Habit Tracker", habitPanel);

            frame.setContentPane(tabbedPane);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // Add shutdown hook to cleanly stop Spring context when the app exits.
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            if (context != null) {
//                context.close();
//            }
//        }));
    }

}
