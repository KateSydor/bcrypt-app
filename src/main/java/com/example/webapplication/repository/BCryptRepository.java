package com.example.webapplication.repository;

import com.example.webapplication.entity.BCryptEntity;
import com.example.webapplication.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BCryptRepository extends JpaRepository<BCryptEntity, Long> {

    List<BCryptEntity> findAllByTaskEntity(TaskEntity taskEntity);
}
