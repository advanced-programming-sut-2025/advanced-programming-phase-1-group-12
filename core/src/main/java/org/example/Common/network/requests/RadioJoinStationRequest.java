package org.example.Common.network.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RadioJoinStationRequest {
    @JsonProperty("stationId")
    private String stationId;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("userName")
    private String userName;

    public RadioJoinStationRequest() {}

    public RadioJoinStationRequest(String stationId, String password, String userId, String userName) {
        this.stationId = stationId;
        this.password = password;
        this.userId = userId;
        this.userName = userName;
    }

    // Getters and setters
    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}


