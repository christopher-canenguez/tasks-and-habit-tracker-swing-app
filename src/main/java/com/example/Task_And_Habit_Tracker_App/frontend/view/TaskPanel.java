package com.example.Task_And_Habit_Tracker_App.frontend.view;

import com.example.Task_And_Habit_Tracker_App.backend.tasks.Task;
import com.example.Task_And_Habit_Tracker_App.frontend.controller.TaskController;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

public class TaskPanel extends JPanel {

    private final TaskController controller;
    private JTable taskTable;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton unselectButton;
    private List<Task> taskList;

    public TaskPanel() {
        super(new MigLayout("alignx center", "", ""));
        this.controller = new TaskController();
        initUi();
        initButtonListeners();
        initGridListeners();
    }

    private void initUi() {
        taskList = controller.fetchTasks();

        var taskTableModel = new TaskTableModel(taskList);
        taskTable = new JTable(taskTableModel);
        taskTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        taskTable.setFillsViewportHeight(true);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        var tableColumnModel = taskTable.getColumnModel();
        tableColumnModel.getColumn(0).setPreferredWidth(25);
        tableColumnModel.getColumn(1).setPreferredWidth(100);
        tableColumnModel.getColumn(2).setPreferredWidth(200);
        tableColumnModel.getColumn(3).setPreferredWidth(100);
        tableColumnModel.getColumn(4).setPreferredWidth(100);
        tableColumnModel.getColumn(5).setPreferredWidth(100);

        var buttonPanel = new JPanel(new MigLayout("fill", "[]15[]15[]", "[]"));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        updateButton.setEnabled(false);
        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        unselectButton = new JButton("Unselect");
        buttonPanel.add(addButton, "grow");
        buttonPanel.add(updateButton, "grow");
        buttonPanel.add(deleteButton, "grow");
        buttonPanel.add(unselectButton, "grow");

        add(new JScrollPane(taskTable), "center");
        add(buttonPanel, "south");
    }

    private void initButtonListeners() {
        addButton.addActionListener(e -> addTask());
        updateButton.addActionListener(e -> updateTask());
        deleteButton.addActionListener(e -> deleteTask());
        unselectButton.addActionListener(e -> taskTable.clearSelection());
    }

    private void addTask() {
        var createDialog = new TaskDetailsDialog(null);
        createDialog.setLocationRelativeTo(this);
        createDialog.setVisible(true);
    }

    private void updateTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        var selectedTask = taskList.get(selectedRow);
        var updateDialog = new TaskDetailsDialog(selectedTask);
        updateDialog.setLocationRelativeTo(this);
        updateDialog.setVisible(true);
    }

    private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        var tableModel = (TaskTableModel) taskTable.getModel();
        if (selectedRow == -1) {
            return;
        }

        var selectedTask = tableModel.getSelectedTask(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this task?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deleteTask(selectedTask.getId());
                tableModel.removeTaskAt(selectedRow); // update UI
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting task: " + ex.getMessage());
            }
        }
        taskTable.repaint();
    }

    private void initGridListeners() {
        taskTable.getSelectionModel().addListSelectionListener(e -> {
            var isRowSelected = taskTable.getSelectedRow() != -1;
            enableButtons(isRowSelected);
        });
    }

    private void enableButtons(boolean enable) {
        deleteButton.setEnabled(enable);
        updateButton.setEnabled(enable);
    }

}
