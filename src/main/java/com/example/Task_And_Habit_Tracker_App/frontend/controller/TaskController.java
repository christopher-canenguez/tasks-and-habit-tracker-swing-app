package com.example.Task_And_Habit_Tracker_App.frontend.controller;

import com.example.Task_And_Habit_Tracker_App.backend.tasks.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TaskController {

    private static final String BASE_URL = "http://localhost:8080/api/v1/task";
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public List<Task> fetchTasks() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            InputStream input = urlConnection.getInputStream();
            return mapper.readValue(input, new TypeReference<>() {
            });
        } catch (Exception e) {
            System.out.println("Unable to retrieve tasks.");
            e.printStackTrace();
            return List.of(); // Return empty list if error
        }
    }

    public void deleteTask(long taskId) throws IOException {
        URL url = new URL(BASE_URL + "/" + taskId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to delete task. HTTP code: " + conn.getResponseCode());
        }
    }

}
