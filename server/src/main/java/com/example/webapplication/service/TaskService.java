package com.example.webapplication.service;

import com.example.webapplication.dto.mapper.BCryptMapper;
import com.example.webapplication.dto.request.BCryptRequest;
import com.example.webapplication.dto.response.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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


    public Set<String> getActiveTaskIds() {
        return ASYNC_TASKS.keySet();
    }

    public TaskCreationResponse accept(BCryptRequest bcryptRequest) {
        log.info("accept({})", bcryptRequest);
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            List<TaskEntity> userStartedTasks = new ArrayList<>(taskRepository.findAllByUserNameAndStatus(username, TaskStatus.STARTED));
            List<TaskEntity> userAcceptedTasks =  new ArrayList<>(taskRepository.findAllByUserNameAndStatus(username, TaskStatus.ACCEPT));
            if (userStartedTasks.size()+userAcceptedTasks.size() >= 2){
                return new TaskCreationResponse(null, false, "Request limit exceeded!");
            }
            var task = saveNewTask(bcryptRequest.getOriginalPasswords().size(), username);
            var progress = new ProgressModel(bcryptRequest.getOriginalPasswords().size(), 0);
            var submittedTask = TASK_EXECUTORS.submit(() -> processTask(task, bcryptRequest));
            ASYNC_TASKS.put(task.getTaskId(), new TaskModel(submittedTask, progress));
            return new TaskCreationResponse(task.getTaskId(), true, "Accepted");
        } catch (Exception exception) {
            return new TaskCreationResponse(null, false, exception.getMessage());
        }
    }

    public TaskCancelResponse cancel(String taskId) {
        cancelTask(taskId);
        cancelTaskLocal(taskId);
        return new TaskCancelResponse(taskId, true, "Task " + taskId + " canceled.");
    }

    public void cancelTaskLocal(String taskId) {
        log.info("cancelTaskLocal({})", taskId);
        TaskModel taskModel = ASYNC_TASKS.get(taskId);
        if (taskModel != null) {
            taskModel.cancel();
            ASYNC_TASKS.remove(taskId);
        } else {
            log.info("Task with id={} doesn't present on current instance", taskId);
        }
    }

    public ResponseEntity<?> progress(String taskId) {
        log.info("progress({})", taskId);
        ProgressModel progressModel = taskRepository.getProgress(taskId, TaskStatus.CANCELED);
        if (progressModel != null) {
            return ResponseEntity.ok(progressModel);
        } else {
            return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST.value(), "Task was not found"));
        }
    }

    private void processTask(TaskEntity taskEntity, BCryptRequest bCryptRequest) {
        var task = startTask(taskEntity.getId());

        bCryptRequest.getOriginalPasswords()
                .stream()
                .map(pass -> bCryptService.encode(pass, bCryptRequest.getRounds()))
                .forEach(bCryptEntity -> {
                    bCryptEntity.setTaskEntity(task);
                    var progress = ASYNC_TASKS.get(taskEntity.getTaskId()).getProgress();
                    progress.increase();
                    log.info("TaskId={}, progress={}", task.getTaskId(), progress);
                    bCryptService.save(bCryptEntity);
                });

        finishTask(taskEntity.getId());
    }

    private TaskEntity saveNewTask(int quantity, String username) {
        var task = new TaskEntity();
        task.setTaskId(UUID.randomUUID().toString());
        task.setStatus(TaskStatus.ACCEPT);
        task.setQuantity(quantity);
        task.setUserName(username);
        taskRepository.save(task);
        return task;
    }

    private void cancelTask(String taskId) {
        var task = taskRepository.findByTaskIdAndStatusIsNot(taskId, TaskStatus.CANCELED).orElseThrow(() -> new IllegalStateException(
                String.format("Task with taskId=%s is not saved or was already canceled", taskId)));
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

    public List<BCryptResponse> showTaskResult(String taskId) {
        TaskEntity task = taskRepository.findByTaskId(taskId).orElseThrow(() -> new IllegalStateException(
                String.format("Task with taskId=%s is not saved or was already canceled", taskId)));
        var result = bCryptRepository.findAllByTaskEntity(task);
        List<BCryptResponse> responses = new ArrayList<>();
        for (BCryptEntity entity : result
        ) {
            responses.add(BCryptMapper.MAPPER.fromEntityToResponse(entity));
        }
        return responses;
    }

    public List<UserTasksResponse> showAllUserTasks(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserTasksResponse> responses = new ArrayList<>();
        var tasks = taskRepository.findAllByUserName(username);
        List<BCryptEntity> bCryptEntities;
        List<BCryptResponse> bCryptResponses;
        for (TaskEntity task:tasks
             ) {
            bCryptEntities = new ArrayList<>();
            bCryptResponses = new ArrayList<>();
            bCryptEntities.addAll(bCryptRepository.findAllByTaskEntity(task));
            for (BCryptEntity entity : bCryptEntities
            ) {
                bCryptResponses.add(BCryptMapper.MAPPER.fromEntityToResponse(entity));
            }
            responses.add(new UserTasksResponse(task.getId(), task.getStarted(), task.getFinished(), bCryptResponses));
        }

        return responses;

    }

}
