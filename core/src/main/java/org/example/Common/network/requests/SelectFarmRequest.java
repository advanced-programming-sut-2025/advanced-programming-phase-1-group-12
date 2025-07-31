package org.example.Common.network.requests;

public class SelectFarmRequest {
    private String username;
    private int farmId;
    private String lobbyId;
    
    public SelectFarmRequest() {}
    
    public SelectFarmRequest(String username, int farmId, String lobbyId) {
        this.username = username;
        this.farmId = farmId;
        this.lobbyId = lobbyId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getFarmId() {
        return farmId;
    }
    
    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }
    
    public String getLobbyId() {
        return lobbyId;
    }
    
    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }
}