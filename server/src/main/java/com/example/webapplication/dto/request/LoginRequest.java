package com.example.webapplication.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
