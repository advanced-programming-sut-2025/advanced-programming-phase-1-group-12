package org.example.Common.network.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

public class LoadStatusResponse {
    private boolean allPlayersReady;
    private String message;

    // Constructors
    public LoadStatusResponse() {}

    public LoadStatusResponse(boolean allPlayersReady, String message) {
        this.allPlayersReady = allPlayersReady;
        this.message = message;
    }

    // Getters and setters
    public boolean isAllPlayersReady() { return allPlayersReady; }
    public String getMessage() { return message; }
    public void setAllPlayersReady(boolean allPlayersReady) { this.allPlayersReady = allPlayersReady; }
    public void setMessage(String message) { this.message = message; }
}
