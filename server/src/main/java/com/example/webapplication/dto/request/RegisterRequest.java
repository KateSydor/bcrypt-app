package com.example.webapplication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    @Size(min = 4, max = 30)
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 4, max = 30)
    private String password;
}
