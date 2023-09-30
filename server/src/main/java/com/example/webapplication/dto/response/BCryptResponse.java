package com.example.webapplication.dto.response;

import java.io.Serializable;

public class BCryptResponse implements Serializable {
    private Long id;
    private String originalPassword;
    private String hashedPassword;
    private Boolean verifyResult;
    private Integer rounds;

    public BCryptResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalPassword() {
        return originalPassword;
    }

    public void setOriginalPassword(String originalPassword) {
        this.originalPassword = originalPassword;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Boolean getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(Boolean verifyResult) {
        this.verifyResult = verifyResult;
    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

}
