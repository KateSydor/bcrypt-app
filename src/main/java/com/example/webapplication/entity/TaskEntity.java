package com.example.webapplication.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "task")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false)
    private String taskId;

    private LocalDateTime started;
    private LocalDateTime finished;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

}
