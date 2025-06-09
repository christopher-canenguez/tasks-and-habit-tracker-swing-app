package com.example.Task_And_Habit_Tracker_App.frontend.view;

import com.example.Task_And_Habit_Tracker_App.backend.tasks.Priority;
import com.example.Task_And_Habit_Tracker_App.backend.tasks.Task;
import com.example.Task_And_Habit_Tracker_App.frontend.controller.TaskController;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class TaskDetailsDialog extends JDialog {

    Task task;
    JTextField titleField;
    JTextField descriptionField;
    JSpinner dateSpinner;
    JComboBox<Priority> priorityDropdown;
    TaskController taskController;
    JButton submitTaskButton;
    TaskPanel panel;

    public TaskDetailsDialog(TaskPanel panel, Task task) {
        super((Frame) null, true);
        this.task = task;
        this.taskController = new TaskController();
        this.panel = panel;

        setTitle(task == null ? "Create Task" : "Edit Task");
        setLayout(new BorderLayout());
        initUi();
        initButtonListener();
        if (task != null) {
            fillFormWithTaskData(task);
        }
    }

    private void initUi() {
        JPanel addTaskPanel = new JPanel(new MigLayout("", "15[]15", "15[][][][][][][][]15"));

        titleField = new JTextField(20);
        int titleCharLimit = 25;
        var titleCharLimitLabel = createCharacterLimitLabel(titleCharLimit);
        titleCharLimitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleField.getDocument().addDocumentListener(createDocumentListener(titleField, titleCharLimitLabel, titleCharLimit));
        ((AbstractDocument) titleField.getDocument()).setDocumentFilter(createDocumentFilter(titleCharLimit));

        descriptionField = new JTextField(40);
        int descriptionCharLimit = 50;
        var descriptionCharLimitLabel = createCharacterLimitLabel(descriptionCharLimit);
        descriptionCharLimitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionField.getDocument().addDocumentListener(createDocumentListener(descriptionField, descriptionCharLimitLabel, descriptionCharLimit));
        ((AbstractDocument) descriptionField.getDocument()).setDocumentFilter(createDocumentFilter(descriptionCharLimit));

        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy");
        dateSpinner.setEditor(editor);

        priorityDropdown = new JComboBox<>(Priority.values());
        submitTaskButton = new JButton("Submit");

        addTaskPanel.add(new JLabel("Title"), "wrap");
        addTaskPanel.add(titleField, "split 2");
        addTaskPanel.add(titleCharLimitLabel, "wrap");
        addTaskPanel.add(new JLabel("Description"), "wrap");
        addTaskPanel.add(descriptionField, "split 2");
        addTaskPanel.add(descriptionCharLimitLabel, "wrap");
        addTaskPanel.add(new JLabel("Due Date"), "wrap");
        addTaskPanel.add(dateSpinner, "wrap");
        addTaskPanel.add(new JLabel("Priority"), "wrap");
        addTaskPanel.add(priorityDropdown);
        addTaskPanel.add(submitTaskButton, "south");

        add(addTaskPanel, BorderLayout.CENTER);
        pack();
    }

    private void initButtonListener() {
        submitTaskButton.addActionListener(e -> {
            if (task == null) {
                createNewTask();
            } else {
                updateExistingTask();
            }
        });
    }

    private void updateExistingTask() {
        task = getTask();
        try {
            Task updatedTask = taskController.updateTask(task);

            if (updatedTask != null && updatedTask.getId() != null) {
                System.out.println("Task updated: " + updatedTask.getId());

                var model = (TaskTableModel) panel.getTaskTable().getModel();
                model.updateTask(updatedTask);

                // Dispose the dialog
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update task. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewTask() {
        var taskToBeCreated = getTask();
        try {
            var newTask = taskController.createTask(taskToBeCreated);
            if (wasTaskCreatedSuccessfully(newTask)) {
                var model = (TaskTableModel) panel.getTaskTable().getModel();
                model.addTask(newTask);
                dispose();
                System.out.println("Task created: " + newTask.getId()); // <-- use backend-generated ID

            } else {
                JOptionPane.showMessageDialog(this, "Failed to create task. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean wasTaskCreatedSuccessfully(Task newTask) {
        return newTask != null && newTask.getId() != null;
    }

    private void fillFormWithTaskData(Task task) {
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        var localDate = task.getDueDate();
        var defaultZoneId = ZoneId.systemDefault();
        var date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        dateSpinner.setValue(date);
        priorityDropdown.setSelectedItem(task.getPriority());
    }

    private JLabel createCharacterLimitLabel(int charLimit) {
        return new JLabel("0 / " + charLimit);
    }

    private DocumentListener createDocumentListener(JTextField textField, JLabel characterCountLabel, int characterLimit) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCharacterCount(textField, characterCountLabel, characterLimit);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCharacterCount(textField, characterCountLabel, characterLimit);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCharacterCount(textField, characterCountLabel, characterLimit);
            }
        };
    }

    private DocumentFilter createDocumentFilter(int characterLimit) {
        return new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) {
                    return;
                }

                var currentStringLength = fb.getDocument().getLength();
                var overCharacterLimit = (currentStringLength + string.length()) - characterLimit;
                if (overCharacterLimit <= 0) {
                    super.insertString(fb, offset, string, attr);
                } else if (currentStringLength < characterLimit) {
                    String cutString = string.substring(0, string.length() - overCharacterLimit);
                    super.insertString(fb, offset, cutString, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    return;
                }
                var currentStringLength = fb.getDocument().getLength();
                var overCharacterLimit = (currentStringLength - length + text.length()) - characterLimit;

                if (overCharacterLimit <= 0) {
                    super.replace(fb, offset, length, text, attrs);
                } else if (currentStringLength < characterLimit) {
                    String cutText = text.substring(0, text.length() - overCharacterLimit);
                    super.replace(fb, offset, length, cutText, attrs);
                }
            }
        };
    }

    private void updateCharacterCount(JTextField textField, JLabel characterCountLabel, int characterLimit) {
        var currentLength = textField.getText().length();
        characterCountLabel.setText(currentLength + " / " + characterLimit);
    }

    private Task getTask() {
        var selectedDate = (Date) dateSpinner.getValue();
        var localDate = selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (task == null) {
            task = new Task(
                    null,
                    titleField.getText(),
                    descriptionField.getText(),
                    localDate,
                    (Priority) priorityDropdown.getSelectedItem(),
                    false);
        } else {
            task.setTitle(titleField.getText());
            task.setDescription(descriptionField.getText());
            task.setDueDate(localDate);
            task.setPriority((Priority) priorityDropdown.getSelectedItem());
        }
        return task;
    }
}
