package com.example.Task_And_Habit_Tracker_App.frontend.view;

import com.example.Task_And_Habit_Tracker_App.backend.tasks.Priority;
import com.example.Task_And_Habit_Tracker_App.backend.tasks.Task;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.List;

public class TaskTableModel extends AbstractTableModel {
    private final List<Task> taskList;
    private final String[] columnNames = {
            "Id",
            "Title",
            "Description",
            "Due Date",
            "Priority",
            "Completed"};

    public TaskTableModel(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public int getRowCount() {
        return taskList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = taskList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> task.getId();
            case 1 -> task.getTitle();
            case 2 -> task.getDescription();
            case 3 -> task.getDueDate();
            case 4 -> task.getPriority();
            case 5 -> task.isCompleted();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Long.class;
            case 1 -> String.class;
            case 2 -> String.class;
            case 3 -> LocalDate.class;
            case 4 -> Priority.class;
            case 5 -> Boolean.class;
            default -> Object.class;
        };
    }


}
