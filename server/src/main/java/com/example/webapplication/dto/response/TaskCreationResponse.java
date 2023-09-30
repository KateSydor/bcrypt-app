package com.example.webapplication.dto.response;

public record TaskCreationResponse(String taskId, boolean accepted, String message) {
}
