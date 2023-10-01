package com.example.webapplication.repository;

import com.example.webapplication.entity.TaskEntity;
import com.example.webapplication.entity.TaskStatus;
import com.example.webapplication.model.ProgressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findByTaskIdAndStatusIsNot(String taskId, TaskStatus status);

    @Query("""
            select
                new com.example.webapplication.model.ProgressModel(task.quantity, size(task.bCryptEntities))
            from TaskEntity task
            where task.taskId = :taskId and task.status <> :taskStatus
            """)
    ProgressModel getProgress(String taskId, TaskStatus taskStatus);

    @Query("""
            select task from TaskEntity task
                where task.taskId in (:activeTaskIds) and task.status = :taskStatus
            """)
    List<TaskEntity> getTaskIdsForCancel(Set<String> activeTaskIds, TaskStatus taskStatus);
}
