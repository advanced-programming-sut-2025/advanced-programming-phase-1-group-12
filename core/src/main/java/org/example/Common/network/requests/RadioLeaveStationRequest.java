package org.example.Common.network.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RadioLeaveStationRequest {
    @JsonProperty("stationId")
    private String stationId;
    
    @JsonProperty("userId")
    private String userId;

    public RadioLeaveStationRequest() {}

    public RadioLeaveStationRequest(String stationId, String userId) {
        this.stationId = stationId;
        this.userId = userId;
    }

    // Getters and setters
    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
