package com.example.webapplication.model;

import lombok.Data;

import java.util.concurrent.Future;

@Data

public class TaskModel {
    private Future<?> future;
    private ProgressModel progress;

    public TaskModel(Future<?> future, ProgressModel progress) {
        this.future = future;
        this.progress = progress;
    }

    public void cancel() {
        if (future != null) {
            future.cancel(true);
        }
    }

}
