package com.example.webapplication.model;

import lombok.Data;

@Data

public class ProgressModel {
    private int totalHash;
    private int doneHash;

    public ProgressModel(int totalHash, int doneHash) {
        this.totalHash = totalHash;
        this.doneHash = doneHash;
    }


    public void increase() {
        doneHash++;
    }
}
