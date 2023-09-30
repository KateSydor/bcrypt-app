package com.example.webapplication.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bcyptdata")
public class BCryptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bc_id")
    private Long id;

    @Column(name = "Original_password")
    private String originalPassword;
    @Column(name = "hashed_password")
    private String hashedPassword;
    @Column(name = "verify_result")
    private Boolean verifyResult;

    @Column(name = "rounds")
    private Integer rounds;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskEntity taskEntity;

}
