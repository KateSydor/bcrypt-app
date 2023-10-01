package com.example.webapplication.job;

import com.example.webapplication.entity.TaskEntity;
import com.example.webapplication.entity.TaskStatus;
import com.example.webapplication.repository.BCryptRepository;
import com.example.webapplication.repository.TaskRepository;
import com.example.webapplication.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskCancelJob {

    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final BCryptRepository bCryptRepository;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void cleanUpCanceledTasks() {
        log.info("cleanUpCanceledTasks()");
        var canceledTasksIds = taskRepository.getTaskIdsForCancel(taskService.getActiveTaskIds(), TaskStatus.CANCELED)
                .stream()
                .peek(task -> taskService.cancelTaskLocal(task.getTaskId()))
                .map(TaskEntity::getId)
                .collect(toSet());

        int numberOfRemovedItems = bCryptRepository.deleteAllCanceledTasksItems();
        log.info("{} items were removed", numberOfRemovedItems);

        log.info("cleanUpCanceledTasks({}) finished", canceledTasksIds);
    }

}
