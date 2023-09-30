package com.example.webapplication.dto.response;

public record TaskCancelResponse(String taskId, boolean canceled, String message) {
}
