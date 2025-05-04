package com.example.Task_And_Habit_Tracker_App.backend.habits;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface HabitRepository extends JpaRepository<Habit, Long> {
}
