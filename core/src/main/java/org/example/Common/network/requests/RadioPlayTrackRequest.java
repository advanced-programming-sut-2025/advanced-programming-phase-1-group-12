package org.example.Common.network.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RadioPlayTrackRequest {
    @JsonProperty("stationId")
    private String stationId;
    
    @JsonProperty("trackId")
    private String trackId;
    
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("action")
    private String action; // "play", "pause", "stop", "next", "previous"

    public RadioPlayTrackRequest() {}

    public RadioPlayTrackRequest(String stationId, String trackId, String userId, String action) {
        this.stationId = stationId;
        this.trackId = trackId;
        this.userId = userId;
        this.action = action;
    }

    // Getters and setters
    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }
    
    public String getTrackId() { return trackId; }
    public void setTrackId(String trackId) { this.trackId = trackId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
