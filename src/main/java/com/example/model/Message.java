package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("message")
    private String message;
}
