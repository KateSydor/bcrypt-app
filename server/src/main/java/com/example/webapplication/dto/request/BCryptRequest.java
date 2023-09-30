package com.example.webapplication.dto.request;

import java.io.Serializable;
import java.util.List;

public class BCryptRequest implements Serializable {
    private List<String> originalPasswords;
    private Integer rounds;

    public BCryptRequest() {
    }

    public List<String> getOriginalPasswords() {
        return originalPasswords;
    }

    public void setOriginalPasswords(List<String> originalPasswords) {
        this.originalPasswords = originalPasswords;
    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }
}
