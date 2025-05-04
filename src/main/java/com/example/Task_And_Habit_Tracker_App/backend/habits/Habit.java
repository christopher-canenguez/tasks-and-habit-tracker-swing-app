package com.example.Task_And_Habit_Tracker_App.backend.habits;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table // Creates table in database for this object.
class Habit {
    @Id
    @SequenceGenerator(
            name = "habit_sequence",
            sequenceName = "habit_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "habit_sequence"
    )
    private Long id;
    private String name;
    private String description;
    private Frequency frequency;
    private int streak;

    Habit() {
    }

    Habit(Long id, String name, String description, Frequency frequency, int streak) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.streak = streak;
    }

    Habit(String name, String description, Frequency frequency, int streak) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.streak = streak;
    }

    @Override
    public String toString() {
        return "Habit: " + '\'' +
                "  - Id = " + id +
                "  - Name = '" + name + '\'' +
                "  - Description = '" + description + '\'' +
                "  - Frequency = " + frequency +
                "  - Streak = " + streak;
    }
}
