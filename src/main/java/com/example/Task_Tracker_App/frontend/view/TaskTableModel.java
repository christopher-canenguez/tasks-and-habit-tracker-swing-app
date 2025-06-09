package com.example.Task_Tracker_App.frontend.view;

import com.example.Task_Tracker_App.backend.Priority;
import com.example.Task_Tracker_App.backend.Task;
import com.example.Task_Tracker_App.frontend.controller.TaskController;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TaskTableModel extends AbstractTableModel {
    private final List<Task> taskList;
    private final String[] columnNames = {
            "Title",
            "Description",
            "Due Date",
            "Priority",
            "Completed"};
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final TaskController controller;

    public TaskTableModel(List<Task> taskList, TaskController controller) {
        this.taskList = taskList;
        this.controller = controller;
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
            case 0 -> task.getTitle();
            case 1 -> task.getDescription();
            case 2 -> task.getDueDate().format(dateFormatter);
            case 3 -> task.getPriority();
            case 4 -> task.isComplete();
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
            case 0, 1 -> String.class;
            case 2 -> LocalDate.class;
            case 3 -> Priority.class;
            case 4 -> Boolean.class;
            default -> Object.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 4; // Only checkbox column is editable
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex != 4) {
            return;
        }

        var task = taskList.get(rowIndex);
        var newStatus = (Boolean) aValue;

        if (task.isComplete() != newStatus) {
            task.setComplete(newStatus); // Update the local task list.
            fireTableCellUpdated(rowIndex, columnIndex); // Repaint UI.

            new SwingWorker<Task, Void>() {
                @Override
                protected Task doInBackground() throws Exception {
                    return controller.updateTask(task);
                }

                @Override
                protected void done() {
                    try {
                        var updatedTask = get();
                        if (updatedTask != null) {
                            taskList.set(rowIndex, updatedTask);
                            fireTableCellUpdated(rowIndex, columnIndex); // Repaint UI.
                        } else {
                            displayErrorDialog("Update failed, no response from server.");
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                        displayErrorDialog("Failed to update task: " + e.getMessage());
                    }
                }
            }.execute();
        }
    }

    public void addTask(Task task) {
        taskList.add(task);
        fireTableRowsInserted(taskList.size() - 1, taskList.size() - 1);
    }

    public void removeTaskAt(int index) {
        taskList.remove(index);
        fireTableRowsUpdated(index, index);
    }

    public Task getSelectedTask(int index) {
        return taskList.get(index);
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < taskList.size(); i++) {
            var currentTask = taskList.get(i);
            if (currentTask.getId().equals(updatedTask.getId())) {
                taskList.set(i, updatedTask);
                fireTableRowsUpdated(i, i);
                return;
            }
        }
    }

    private void displayErrorDialog(String errorString) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
        });
    }
}
