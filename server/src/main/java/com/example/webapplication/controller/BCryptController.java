package com.example.webapplication.controller;

import com.example.webapplication.dto.request.BCryptRequest;
import com.example.webapplication.dto.response.BCryptResponse;
import com.example.webapplication.dto.response.TaskCancelResponse;
import com.example.webapplication.dto.response.TaskCreationResponse;
import com.example.webapplication.dto.response.UserTasksResponse;
import com.example.webapplication.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class BCryptController {
    private final TaskService taskService;

    @GetMapping("/result")
    public List<BCryptResponse> findAllBCrypt(@RequestParam String taskId) {
        return taskService.showTaskResult(taskId);
    }

    @GetMapping("/cancel")
    public TaskCancelResponse cancel(@RequestParam String taskId) {
        return taskService.cancel(taskId);
    }

    @GetMapping("/progress")
    public ResponseEntity<?> progress(@RequestParam String taskId) {
        return taskService.progress(taskId);
    }

    @PostMapping("/res")
    public TaskCreationResponse acceptTask(@RequestBody @Valid BCryptRequest bcryptRequest) {
        return taskService.accept(bcryptRequest);
    }

    @GetMapping("/history")
    public List<UserTasksResponse> userHistory() {
        return taskService.showAllUserTasks();
    }
}