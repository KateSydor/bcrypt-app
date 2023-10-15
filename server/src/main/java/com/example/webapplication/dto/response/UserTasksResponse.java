package com.example.webapplication.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserTasksResponse {
    Long taskId;
    LocalDateTime started;
    LocalDateTime finished;
    List<BCryptResponse> bCryptResponseList;

    public UserTasksResponse(Long taskId, LocalDateTime started, LocalDateTime finished, List<BCryptResponse> bCryptResponseList) {
        this.taskId = taskId;
        this.started = started;
        this.finished = finished;
        this.bCryptResponseList = bCryptResponseList;
    }
}
