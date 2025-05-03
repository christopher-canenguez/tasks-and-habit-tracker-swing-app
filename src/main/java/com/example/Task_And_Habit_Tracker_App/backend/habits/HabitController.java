package com.example.Task_And_Habit_Tracker_App.backend.habits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/habit")
public class HabitController {
    private final HabitService habitService;

    @Autowired
    HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping()
    List<Habit> getHabits() {
        return habitService.getHabits();
    }

    @GetMapping(path = "{habitId}")
    Habit getHabitById(@PathVariable("habitId") Long habitId) {
        return habitService.getHabitById(habitId);
    }

    @PostMapping
    void registerNewHabit(@RequestBody Habit habit) {
        habitService.createNewHabit(habit);
    }

    @DeleteMapping(path = "{habitId}")
    void deleteHabit(@PathVariable("habitId") Long habitId) {
        habitService.deleteHabit(habitId);
    }

    @PutMapping(path = "{habitId}")
    void updateHabit(@PathVariable("habitId") Long habitId,
                     @RequestParam(required = false) String name,
                     @RequestParam(required = false) String description,
                     @RequestParam(required = false) Frequency frequency,
                     @RequestParam(required = false) int streak) {
        habitService.updateHabit(habitId, name, description, frequency, streak);
    }
}
