package com.example.webapplication.service;

import com.example.webapplication.dto.mapper.BCryptMapper;
import com.example.webapplication.dto.request.BCryptRequest;
import com.example.webapplication.dto.response.ApiError;
import com.example.webapplication.dto.response.BCryptResponse;
import com.example.webapplication.dto.response.TaskCancelResponse;
import com.example.webapplication.dto.response.TaskCreationResponse;
import com.example.webapplication.entity.BCryptEntity;
import com.example.webapplication.entity.TaskEntity;
import com.example.webapplication.entity.TaskStatus;
import com.example.webapplication.model.ProgressModel;
import com.example.webapplication.model.TaskModel;
import com.example.webapplication.repository.BCryptRepository;
import com.example.webapplication.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final Map<String, TaskModel> ASYNC_TASKS = new ConcurrentHashMap<>();
    private final ExecutorService TASK_EXECUTORS = Executors.newCachedThreadPool();

    private final BCryptRepository bCryptRepository;
    private final TaskRepository taskRepository;
    private final BCryptService bCryptService;


    public TaskCreationResponse accept(BCryptRequest bcryptRequest) {
        try {
            var task = saveNewTask();
            var progress = new ProgressModel(bcryptRequest.getOriginalPasswords().size(), 0);
            var submittedTask = TASK_EXECUTORS.submit(() -> processTask(task, bcryptRequest));
            ASYNC_TASKS.put(task.getTaskId(), new TaskModel(submittedTask, progress));
            return new TaskCreationResponse(task.getTaskId(), true, "Accepted");
        } catch (Exception exception) {
            return new TaskCreationResponse(null, false, exception.getMessage());
        }
    }

    public TaskCancelResponse cancel(String taskId) {
        TaskModel taskModel = ASYNC_TASKS.get(taskId);
        if (taskModel != null) {
            taskModel.cancel();
            ASYNC_TASKS.remove(taskId);
            cancelTask(taskId);
            return new TaskCancelResponse(taskId, true, "Task " + taskId + " canceled.");
        } else {
            return new TaskCancelResponse(taskId, false, "Task was not found");
        }
    }

    public ResponseEntity<?> progress(String taskId) {
        TaskModel taskModel = ASYNC_TASKS.get(taskId);
        if (taskModel != null) {
            return ResponseEntity.ok(taskModel.getProgress());
        } else {
            return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST.value(), "Task was not found"));
        }
    }

    private void processTask(TaskEntity taskEntity, BCryptRequest bCryptRequest) {
        var task = startTask(taskEntity.getId());

        var bcryptEntities = bCryptRequest.getOriginalPasswords()
                .stream()
                .map(pass -> bCryptService.encode(pass, bCryptRequest.getRounds()))
                .peek(bCryptEntity -> {
                    bCryptEntity.setTaskEntity(task);
                    ASYNC_TASKS.get(taskEntity.getTaskId()).getProgress().increase();
                })
                .toList();

        bCryptRepository.saveAll(bcryptEntities);

        finishTask(taskEntity.getId());
    }

    private TaskEntity saveNewTask() {
        var task = new TaskEntity();
        task.setTaskId(UUID.randomUUID().toString());
        task.setStatus(TaskStatus.ACCEPT);
        taskRepository.save(task);
        return task;
    }

    private void cancelTask(String taskId) {
        var task = taskRepository.findByTaskId(taskId).orElseThrow(() -> new IllegalStateException(
                String.format("Task with taskId=%s is not saved", taskId)));
        task.setStatus(TaskStatus.CANCELED);
        task.setFinished(LocalDateTime.now());
        taskRepository.save(task);
    }

    private void finishTask(Long id) {
        var task = getTask(id);
        task.setStatus(TaskStatus.FINISHED);
        task.setFinished(LocalDateTime.now());
        taskRepository.save(task);
    }

    private TaskEntity startTask(Long id) {
        var task = getTask(id);
        task.setStatus(TaskStatus.STARTED);
        task.setStarted(LocalDateTime.now());
        taskRepository.save(task);
        return task;
    }

    private TaskEntity getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                String.format("Task with id=%s is not saved", id)));
    }

    public List<BCryptResponse> showTaskResult(Long Id) {
        var task = taskRepository.findById(Id).orElseThrow(() -> new IllegalStateException(
                String.format("Task with id=%s is not saved", Id)));
        var result = bCryptRepository.findAllByTaskEntity(task);
        List<BCryptResponse> responses = new ArrayList<>();
        for (BCryptEntity entity : result
        ) {
            responses.add(BCryptMapper.MAPPER.fromEntityToResponse(entity));
        }
        return responses;
    }

}
