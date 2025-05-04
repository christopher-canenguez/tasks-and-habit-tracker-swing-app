package com.example.Task_And_Habit_Tracker_App.backend.habits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
class HabitService {
    private final HabitRepository habitRepository;

    @Autowired
    HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    List<Habit> getHabits() {
        return habitRepository.findAll();
    }

    Habit getHabitById(Long habitId) {
        Optional<Habit> habitOptional = habitRepository.findById(habitId);

        if (habitOptional.isEmpty()) {
            throw new IllegalStateException("Habit with Id " + habitId + " doesn't exist.");
        }

        return habitOptional.get();
    }

    void createNewHabit(Habit habit) {
        habitRepository.save(habit);
    }

    void deleteHabit(Long habitId) {
        boolean habitExists = habitRepository.existsById(habitId);

        if (!habitExists) {
            throw new IllegalStateException("Habit with Id " + habitId + " doesn't exist.");
        }

        habitRepository.deleteById(habitId);
    }

    @Transactional
    void updateHabit(Long habitId, String name, String description, Frequency frequency, int streak) {
        Habit habit = habitRepository
                .findById(habitId)
                .orElseThrow(() -> new IllegalStateException(
                        "Habit with id " + habitId + " doesn't exist."
                ));

        if (isNameValid(name, habit)) {
            habit.setName(name);
        }

        if (isDescriptionValid(description, habit)) {
            habit.setDescription(description);
        }

        if (isFrequencyValid(frequency, habit)) {
            habit.setFrequency(frequency);
        }

        if (isStreakValid(streak, habit)) {
            habit.setStreak(streak);
        }

    }

    private boolean isNameValid(String name, Habit habit) {
        return name != null && !name.isEmpty() && !Objects.equals(habit.getName(), name);
    }

    private boolean isDescriptionValid(String description, Habit habit) {
        return description != null && !description.isEmpty() && !Objects.equals(habit.getDescription(), description);
    }

    private boolean isFrequencyValid(Frequency frequency, Habit habit) {
        return frequency != null && !Objects.equals(habit.getFrequency(), frequency);
    }

    private boolean isStreakValid(int streak, Habit habit) {
        return !Objects.equals(habit.getStreak(), streak);
    }
}
