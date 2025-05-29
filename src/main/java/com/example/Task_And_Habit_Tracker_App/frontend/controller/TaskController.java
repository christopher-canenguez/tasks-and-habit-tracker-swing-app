package com.example.Task_And_Habit_Tracker_App.frontend.controller;

import com.example.Task_And_Habit_Tracker_App.backend.tasks.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    public Task createTask(Task task) throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = mapper.writeValueAsString(task);

        // Send request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            System.out.println("Task created successfully!");
        } else {
            System.err.println("Failed to create task. HTTP " + responseCode);
        }

        // Always try to read the response body regardless of Content-Length
        try (InputStream inputStream = conn.getInputStream()) {
            return mapper.readValue(inputStream, Task.class);
        } catch (IOException ex) {
            // If an error stream exists, read and log it
            InputStream errorStream = conn.getErrorStream();
            if (errorStream != null) {
                String errorMsg = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
                System.err.println("Error response: " + errorMsg);
            }
            throw ex;
        }
    }

    public Task updateTask(Task task) throws IOException {
        if (task.getId() == null) {
            throw new IllegalArgumentException("Task ID must not be null for update.");
        }

        URL url = new URL(BASE_URL + "/" + task.getId());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = mapper.writeValueAsString(task);

        try (OutputStream outputStream = conn.getOutputStream()) {
            outputStream.write(json.getBytes());
            outputStream.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            try (InputStream is = conn.getInputStream()) {
                return mapper.readValue(is, Task.class);
            }
        } else {
            InputStream errorStream = conn.getErrorStream();
            if (errorStream != null) {
                String errorMsg = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
                System.err.println("Error updating task: " + errorMsg);
            }
            throw new RuntimeException("Failed to update task: " + responseCode);
        }
    }

}
