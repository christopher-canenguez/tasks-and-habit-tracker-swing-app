package com.example.Task_And_Habit_Tracker_App.frontend.view;

import com.example.Task_And_Habit_Tracker_App.backend.habits.Frequency;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class HabitPanel extends JPanel {

    private JTable habitTable;

    public HabitPanel() {
        super(new MigLayout("debug", "", ""));
        initUi();
    }

    private void initUi() {
        var createHabitPanel = new JPanel(new MigLayout());
        var nameLabel = new JLabel("Name: ");
        var descriptionLabel = new JLabel("Description: ");
        var frequencyLabel = new JLabel("Frequency: ");
        var nameTextField = new JTextField();
        nameTextField.setColumns(20);
        var descriptionTextField = new JTextField();
        descriptionTextField.setColumns(40);
        var frequencyDropdown = new JComboBox<>(Frequency.values());
        var addHabitButton = new JButton("Add Habit");
        createHabitPanel.add(nameLabel, "split 2");
        createHabitPanel.add(nameTextField);
        createHabitPanel.add(descriptionLabel, "split 2");
        createHabitPanel.add(descriptionTextField);
        createHabitPanel.add(frequencyLabel, "split 2");
        createHabitPanel.add(frequencyDropdown);
        createHabitPanel.add(addHabitButton, "grow");

        String[] columnNames = {
                "Name",
                "Description",
                "Frequency",
                "Streak"};
        Object[][] exampleData = {
                {"Stretch", "Stretch in the morning.", Frequency.DAILY, 5},
                {"Finances", "Review finances.", Frequency.MONTHLY, 3}
        };
        habitTable = new JTable(exampleData, columnNames);

        add(new JScrollPane(habitTable), "center");
        add(createHabitPanel, "north");

    }
}
