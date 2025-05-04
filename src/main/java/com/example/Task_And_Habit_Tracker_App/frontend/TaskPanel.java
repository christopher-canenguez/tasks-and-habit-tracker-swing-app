package com.example.Task_And_Habit_Tracker_App.frontend;

import com.example.Task_And_Habit_Tracker_App.backend.tasks.Priority;
import com.example.Task_And_Habit_Tracker_App.backend.tasks.Task;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.time.LocalDate;

public class TaskPanel extends JPanel {

    private JTable taskTable;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    public TaskPanel() {
        super(new MigLayout("debug", "", ""));
        initUi();
    }

    private void initUi() {
        String[] columnNames = {
                "Title",
                "Description",
                "Due Date",
                "Priority",
                "Completed"};

        Object[][] exampleData = {
                {"Groceries", "Go shopping for groceries.", LocalDate.now(), Priority.HIGH, true},
                {"Groceries", "Go shopping for groceries.", LocalDate.now(), Priority.HIGH, false}
        };
        taskTable = new JTable(exampleData, columnNames);

        var buttonPanel = new JPanel(new MigLayout("fill, debug", "[]15[]15[]", "[]"));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(addButton, "grow");
        buttonPanel.add(updateButton, "grow");
        buttonPanel.add(deleteButton, "grow");

        add(new JScrollPane(taskTable), "center");
        add(buttonPanel, "south");
    }
}
