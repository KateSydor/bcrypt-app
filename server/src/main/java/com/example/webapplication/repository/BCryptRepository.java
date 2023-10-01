package com.example.webapplication.repository;

import com.example.webapplication.entity.BCryptEntity;
import com.example.webapplication.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BCryptRepository extends JpaRepository<BCryptEntity, Long> {

    List<BCryptEntity> findAllByTaskEntity(TaskEntity taskEntity);

    @Modifying
    @Query(value = "delete from bcyptdata where task_id in (select id from task where status = 'CANCELED')", nativeQuery = true)
    int deleteAllCanceledTasksItems();
}
