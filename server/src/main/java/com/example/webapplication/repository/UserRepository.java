package com.example.webapplication.repository;

import com.example.webapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsernameOrEmail(String username, String email);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}