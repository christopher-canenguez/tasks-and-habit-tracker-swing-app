package com.example.Task_And_Habit_Tracker_App.backend.habits;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class HabitConfig {
    @Bean
    CommandLineRunner habitCommandLineRunner(HabitRepository habitRepository) {
        return args -> {
            Habit stretchHabit = new Habit(
                    "Stretch",
                    "Stretch in the morning everyday.",
                    Frequency.DAILY,
                    3
            );

            Habit financesHabit = new Habit(
                    "Check Finances",
                    "Go through finances and make sure everything looks good.",
                    Frequency.MONTHLY,
                    10
            );

            habitRepository.saveAll(
                    List.of(stretchHabit, financesHabit)
            );
        };
    }
}
