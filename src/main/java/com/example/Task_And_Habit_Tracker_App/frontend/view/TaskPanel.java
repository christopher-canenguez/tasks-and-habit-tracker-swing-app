package com.example.Task_And_Habit_Tracker_App.frontend.view;

import com.example.Task_And_Habit_Tracker_App.backend.tasks.Priority;
import com.example.Task_And_Habit_Tracker_App.frontend.controller.TaskController;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
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
import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;

public class TaskPanel extends JPanel {

    private final TaskController controller;
    private JTable taskTable;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton unselectButton;

    public TaskPanel() {
        super(new MigLayout("alignx center", "", ""));
        this.controller = new TaskController();
        initUi();
        initButtonListeners();
        initGridListeners();
    }

    private void initUi() {
        var taskList = controller.fetchTasks();

        var taskTableModel = new TaskTableModel(taskList);
        taskTable = new JTable(taskTableModel);
        taskTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        taskTable.setFillsViewportHeight(true);
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
        addButton.addActionListener(e -> createAddTaskDialog());
        unselectButton.addActionListener(e -> taskTable.clearSelection());
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

    private void createAddTaskDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create Task");
        dialog.setLayout(new BorderLayout());
        dialog.setModal(true);

        JPanel addTaskPanel = new JPanel(new MigLayout("", "15[]15", "15[][][][][][][][]15"));

        JTextField titleField = new JTextField(20);
        int titleCharLimit = 25;
        var titleCharLimitLabel = createCharacterLimitLabel(titleCharLimit);
        titleCharLimitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleField.getDocument().addDocumentListener(createDocumentListener(titleField, titleCharLimitLabel, titleCharLimit));
        ((AbstractDocument) titleField.getDocument()).setDocumentFilter(createDocumentFilter(titleCharLimit));

        JTextField descriptionField = new JTextField(40);
        int descriptionCharLimit = 50;
        var descriptionCharLimitLabel = createCharacterLimitLabel(descriptionCharLimit);
        descriptionCharLimitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionField.getDocument().addDocumentListener(createDocumentListener(descriptionField, descriptionCharLimitLabel, descriptionCharLimit));
        ((AbstractDocument) descriptionField.getDocument()).setDocumentFilter(createDocumentFilter(descriptionCharLimit));

        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        JComboBox<Priority> priorityDropdown = new JComboBox<>(Priority.values());
        JButton submitTaskButton = new JButton("Submit");

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

        dialog.add(addTaskPanel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
}
