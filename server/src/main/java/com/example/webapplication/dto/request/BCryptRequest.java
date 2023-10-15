package com.example.webapplication.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

public class BCryptRequest implements Serializable {

    @NotNull
    @Size(min = 1)
    private List<String> originalPasswords;
    @NotNull
    @Min(4)
    @Max(30)
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
